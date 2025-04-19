package com.game.backend.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.client.ClientConfiguration;
import org.springframework.data.elasticsearch.client.elc.ElasticsearchConfiguration;

@Configuration
public class ElasticSearchConfig extends ElasticsearchConfiguration {
    @Value("${ELASTICSEARCH_URIS}")
    private String elasticSearchUris;

    @Value("${ELASTICSEARCH_USERNAME}")
    private String elasticSearchUsername;

    @Value("${ELASTICSEARCH_PASSWORD}")
    private String elasticSearchPassword;
    @Override
    public ClientConfiguration clientConfiguration() {
        return ClientConfiguration.builder()
                .connectedTo(elasticSearchUris)
                .withBasicAuth(elasticSearchUsername, elasticSearchPassword)
                .build();
    }
}
