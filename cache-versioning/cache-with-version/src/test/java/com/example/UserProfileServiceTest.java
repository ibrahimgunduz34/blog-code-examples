package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.redis.core.StringRedisTemplate;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class UserProfileServiceTest {
    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    StringRedisTemplate redisTemplate;

    @Test
    void shouldFailToInstantiateUserProfile() {
        userProfileService.getProfile(1L);

        Set<String> keys = redisTemplate.keys("*");

        assertThat(keys)
                .anyMatch(key -> key.matches(".+:userProfile::1"));
    }
}