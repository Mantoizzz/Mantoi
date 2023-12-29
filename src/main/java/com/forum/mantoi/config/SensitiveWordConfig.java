package com.forum.mantoi.config;

import com.forum.mantoi.sys.handler.MyWordReplaceHandler;
import com.github.houbb.sensitive.word.bs.SensitiveWordBs;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SensitiveWordConfig {

    @Bean
    public SensitiveWordBs sensitiveWordBs() {
        return SensitiveWordBs.newInstance()
                .init();
    }

    @Bean
    public MyWordReplaceHandler wordReplace() {
        return MyWordReplaceHandler.getInstance().init();
    }
}
