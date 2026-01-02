package com.example;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

@Configuration
public class CacheConfiguration {
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(ObjectProvider<BuildProperties> buildPropertiesProvider) {
        BuildProperties buildProperties = buildPropertiesProvider.getIfAvailable();

        String name = buildProperties != null ? buildProperties.getName() : "application";
        String version = buildProperties != null ? buildProperties.getVersion() : "dev";

        return RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith(String.format("%s:%s:", name, version));
    }
}
