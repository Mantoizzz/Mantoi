package com.forum.mantoi.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Mybatis配置类
 *
 * @author Mantoi
 */
@Configuration
public class MybatisPlusConfig {

    /**
     * 设置分页interceptor
     *
     * @return MybatisPlusInterceptor
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        PaginationInnerInterceptor innerInterceptor = new PaginationInnerInterceptor();
        innerInterceptor.setOverflow(false);
        innerInterceptor.setMaxLimit(500L);
        innerInterceptor.setDbType(DbType.MYSQL);
        interceptor.addInnerInterceptor(innerInterceptor);
        return interceptor;
    }

}
