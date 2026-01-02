package com.example;

import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.info.BuildProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;

import java.util.Properties;

@Configuration
public class CacheConfiguration {
    @Bean
    public RedisCacheConfiguration redisCacheConfiguration(ObjectProvider<BuildProperties> buildPropertiesProvider) {
        String version = buildPropertiesProvider.getIfAvailable(this::getDefaultBuildProperties).getVersion();
        
        return RedisCacheConfiguration.defaultCacheConfig()
                .prefixCacheNameWith(version + ":");
    }

    private BuildProperties getDefaultBuildProperties() {
        Properties props = new Properties();
        props.setProperty("version", "dev");
        return new BuildProperties(props);
    }
}
