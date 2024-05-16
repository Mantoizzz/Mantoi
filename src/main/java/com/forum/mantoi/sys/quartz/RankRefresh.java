package com.forum.mantoi.sys.quartz;

import com.forum.mantoi.utils.CommunityUtils;
import com.forum.mantoi.utils.RedisKeys;
import com.forum.mantoi.utils.RedisUtils;
import lombok.extern.slf4j.Slf4j;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.springframework.data.redis.core.ZSetOperations;

import java.util.Objects;
import java.util.Set;

/**
 * 1.定时更新zset的定时任务，每隔5分钟更新一次zset
 *
 * @author DELL
 */
@Slf4j
public class RankRefresh implements Job {

    //TODO 因为时效性而导致帖子热度降低的功能
    @Override
    public void execute(JobExecutionContext jobExecutionContext) {
        String zsetKey = RedisKeys.getRankSet();
        Set<ZSetOperations.TypedTuple<Object>> typedTuples = RedisUtils.zsetGetAll(zsetKey);
        if (Objects.nonNull(typedTuples)) {
            for (ZSetOperations.TypedTuple<Object> typedTuple : typedTuples) {
                Object postId = typedTuple.getValue();
                if (Objects.nonNull(postId)) {
                    CommunityUtils.processScores(((long) postId));
                }

            }

        }
    }

}
