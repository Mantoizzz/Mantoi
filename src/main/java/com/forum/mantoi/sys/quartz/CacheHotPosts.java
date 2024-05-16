package com.forum.mantoi.sys.quartz;

import com.forum.mantoi.common.pojo.dto.request.PostInformationDto;
import com.forum.mantoi.common.pojo.vo.CommentVO;
import com.forum.mantoi.sys.dao.entity.Post;
import com.forum.mantoi.sys.dao.entity.PostContent;
import com.forum.mantoi.sys.services.CommentService;
import com.forum.mantoi.sys.services.PostService;
import com.forum.mantoi.utils.CaffeineUtils;
import com.forum.mantoi.utils.RedisKeys;
import com.forum.mantoi.utils.RedisUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Set;

/**
 * 刷新Redis zset里面的最新Post到Caffeine和Redis
 *
 * @author DELL
 */
@Slf4j
@AllArgsConstructor
public class CacheHotPosts implements Job {

    private final PostService postService;

    private final CommentService commentService;

    private final Random random = new Random();

    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = RedisUtils.zsetGetAll(RedisKeys.getRankSet());
        if (Objects.nonNull(typedTuples)) {
            int cnt = 0;
            for (ZSetOperations.TypedTuple<Object> typedTuple : typedTuples) {
                Object postId = typedTuple.getValue();
                if (Objects.nonNull(postId)) {
                    Post post = postService.findById(((long) postId));
                    PostContent postContent = postService.getContent(post);
                    if (Objects.isNull(postContent) || Objects.isNull(post)) {
                        continue;
                    }
                    List<CommentVO> commentVOList = commentService.getPostComments(post);
                    PostInformationDto dto = new PostInformationDto();
                    dto.setPost(post);
                    dto.setPostContent(postContent);
                    dto.setCommentList(commentVOList);
                    //把Dto放到Caffeine里面
                    CaffeineUtils.put(post.getId().toString(), dto);
                    //设置到Redis里面，然后过期时间设置为4天+随机值
                    RedisUtils.set(post.getId().toString(), dto, 60L * 60 * 24 * 4 + random.nextInt(6) * 60L * 60);
                    cnt++;
                    if (cnt == Math.min(typedTuples.size(), 60)) {
                        break;
                    }
                }
            }
        }
    }
}
