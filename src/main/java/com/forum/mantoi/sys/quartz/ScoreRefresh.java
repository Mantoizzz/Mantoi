package com.forum.mantoi.sys.quartz;

import com.forum.mantoi.common.CommonResultStatus;
import com.forum.mantoi.sys.entity.Post;
import com.forum.mantoi.sys.exception.BusinessException;
import com.forum.mantoi.sys.model.Entity;
import com.forum.mantoi.sys.services.LikeService;
import com.forum.mantoi.sys.services.PostService;
import com.forum.mantoi.utils.RedisKeys;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.springframework.data.redis.core.BoundSetOperations;
import org.springframework.data.redis.core.RedisTemplate;

import java.time.Duration;
import java.time.Instant;
import java.util.function.Supplier;

/**
 * Quartz定时刷新帖子任务类
 *
 * @author Mantoizzz
 */
@AllArgsConstructor
@Slf4j
public class ScoreRefresh implements Job {

    private static final int PEAK_TIME = 72;

    private static final double BASE_SCORE = 40;

    private static final double DECAY_RATE = 0.05;

    private static final double LIKE_WEIGHT = 1.0;

    private static final double COMMENT_WEIGHT = 2.0;

    private final PostService postService;

    private final LikeService likeService;

    private final RedisTemplate<String, Object> redisTemplate;

    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        String redisKey = RedisKeys.getPostScoreSet();
        BoundSetOperations<String, Object> operations = redisTemplate.boundSetOps(redisKey);
        if (operations.size() == 0) {
            log.info("Quartz:没有执行的任务(Set为空)");
            return;
        }
        log.info("Quartz:任务开始");
        while (operations.size() > 0) {
            try {
                refreshScore((Long) operations.pop());
            } catch (Throwable e) {
                throw new RuntimeException(e);
            }
        }
        log.info("Quartz:任务结束");
    }

    /**
     * 刷新帖子方法
     *
     * @param postId 帖子ID
     * @throws Throwable BusinessException
     */
    private void refreshScore(Long postId) throws Throwable {
        Post post = postService.findById(postId).orElseThrow(
                (Supplier<Throwable>) () -> new BusinessException(CommonResultStatus.RECORD_NOT_EXIST, "Post does not exist")
        );
        long likeCount = likeService.viewLikes(Entity.POST, postId);
        int commentCount = post.getCommentPosts().size();
        Instant publishTime = post.getPublishTime().toInstant();
        long hours = getTimeDuration(publishTime);
        double score = calculateScore(likeCount, commentCount, hours);
        postService.updateScore(postId, score);
        //TODO 更新elasticsearch的score

    }

    /**
     * 计算时间跨度
     *
     * @param publish 发布时间
     * @return 时间跨度
     */
    private long getTimeDuration(Instant publish) {
        Instant now = Instant.now();
        Duration duration = Duration.between(publish, now);
        return duration.toHours();
    }

    /**
     * 计算帖子分数算法
     *
     * @param likes            点赞数
     * @param commentCount     评论数
     * @param hoursSincePosted 经过的时间
     * @return 分数
     */
    private double calculateScore(long likes, int commentCount, long hoursSincePosted) {
        double score;
        double weightedSum = (likes * LIKE_WEIGHT) + (commentCount * COMMENT_WEIGHT);
        if (hoursSincePosted <= 2 * PEAK_TIME) {
            score = weightedSum * Math.sin(Math.PI * hoursSincePosted / (2 * PEAK_TIME));
        } else {
            double decayFactor = Math.exp((-1) * DECAY_RATE * (hoursSincePosted - 2 * PEAK_TIME));
            score = BASE_SCORE * decayFactor;
        }
        return Math.max(0, score);
    }

}
