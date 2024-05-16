package com.forum.mantoi.config;

import com.forum.mantoi.sys.quartz.CacheHotPosts;
import com.forum.mantoi.sys.quartz.RankRefresh;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * Quartz配置文件
 *
 * @author DELL
 */
@Configuration
public class QuartzConfig {

    private static final String COMMUNITY_GROUP = "CommunityGroup";

    @Bean(name = "rankRefreshJobTrigger")
    public SimpleTriggerFactoryBean simpleTriggerFactoryBean(@Qualifier("rankRefreshJobDetail") JobDetail jobDetail) {
        SimpleTriggerFactoryBean bean = new SimpleTriggerFactoryBean();
        bean.setJobDetail(jobDetail);
        bean.setName("rankRefreshJobTrigger");
        bean.setGroup(COMMUNITY_GROUP);
        bean.setRepeatInterval(1000L * 60 * 15);
        bean.setJobDataMap(new JobDataMap());
        return bean;
    }

    @Bean(name = "rankRefreshJobDetail")
    public JobDetailFactoryBean rankRefreshJobDetail() {
        JobDetailFactoryBean jobDetailFactoryBean = new JobDetailFactoryBean();
        jobDetailFactoryBean.setJobClass(RankRefresh.class);
        jobDetailFactoryBean.setName("RankRefresh");
        jobDetailFactoryBean.setGroup(COMMUNITY_GROUP);
        jobDetailFactoryBean.setDurability(true);
        jobDetailFactoryBean.setRequestsRecovery(true);
        return jobDetailFactoryBean;
    }


    @Bean(name = "cacheHotPostDetail")
    public JobDetailFactoryBean cacheHotPostDetail() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setJobClass(CacheHotPosts.class);
        bean.setName("cacheHotPostDetail");
        bean.setGroup(COMMUNITY_GROUP);
        bean.setDurability(true);
        bean.setRequestsRecovery(true);
        return bean;
    }

    @Bean(name = "cacheHotPostTrigger")
    public SimpleTriggerFactoryBean cacheHotPostTrigger(@Qualifier("cacheHotPostDetail") JobDetail jobDetail) {
        SimpleTriggerFactoryBean bean = new SimpleTriggerFactoryBean();
        bean.setJobDetail(jobDetail);
        bean.setName("cacheHotPostTrigger");
        bean.setGroup(COMMUNITY_GROUP);
        bean.setRepeatInterval(1000L * 60 * 40);
        bean.setJobDataMap(new JobDataMap());
        return bean;
    }
}
