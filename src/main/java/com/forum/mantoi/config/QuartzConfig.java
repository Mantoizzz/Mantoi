package com.forum.mantoi.config;

import com.forum.mantoi.sys.quartz.ScoreRefresh;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.JobDetailFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

/**
 * Quartz配置文件
 */
@Configuration
public class QuartzConfig {

    private final String COMMUNITY_GROUP = "CommunityGroup";


    @Bean(name = "scoreRefreshJobDetail")
    public JobDetailFactoryBean scoreRefreshJobDetail() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setJobClass(ScoreRefresh.class);
        bean.setName("ScoreRefreshJob");
        bean.setGroup(COMMUNITY_GROUP);
        bean.setDurability(true);
        bean.setRequestsRecovery(true);
        return bean;
    }

    @Bean(name = "scoreRefreshTrigger")
    public SimpleTriggerFactoryBean scoreRefreshTrigger(@Qualifier("scoreRefreshJobDetail") JobDetail jobDetail) {
        SimpleTriggerFactoryBean bean = new SimpleTriggerFactoryBean();
        bean.setJobDetail(jobDetail);
        bean.setName("ScoreRefreshTrigger");
        bean.setGroup(COMMUNITY_GROUP);
        bean.setRepeatInterval(1000 * 60 * 5);
        bean.setJobDataMap(new JobDataMap());
        return bean;
    }

    @Bean(name = "topListRefreshJobDetail")
    public JobDetailFactoryBean topListRefreshJobDetail() {
        JobDetailFactoryBean bean = new JobDetailFactoryBean();
        bean.setJobClass(ScoreRefresh.class);
        bean.setName("TopListRefreshJob");
        bean.setGroup(COMMUNITY_GROUP);
        bean.setDurability(true);
        bean.setRequestsRecovery(true);
        return bean;
    }

    @Bean(name = "topListRefreshTrigger")
    public SimpleTriggerFactoryBean topListRefreshTrigger(@Qualifier("topListRefreshJobDetail") JobDetail jobDetail) {
        SimpleTriggerFactoryBean bean = new SimpleTriggerFactoryBean();
        bean.setJobDetail(jobDetail);
        bean.setName("TopListRefreshJob");
        bean.setGroup(COMMUNITY_GROUP);
        bean.setRepeatInterval(1000 * 60 * 10);
        bean.setJobDataMap(new JobDataMap());
        return bean;
    }
}
