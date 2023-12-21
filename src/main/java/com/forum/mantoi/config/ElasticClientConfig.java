package com.forum.mantoi.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticClientConfig extends ElasticsearchConfiguration {

    @Value(value = "${spring.elasticsearch.username}")
    String username;

    @Value(value = "${spring.elasticsearch.password}")
    String password;

    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder().connectedToLocalhost().withBasicAuth(username, password).build();
    }
}
