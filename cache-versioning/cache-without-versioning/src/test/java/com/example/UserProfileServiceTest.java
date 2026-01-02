package com.example;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.redis.core.RedisTemplate;

import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
//@Import(RedisTemplateTestConfig.class)
class UserProfileServiceTest {
    @Autowired
    private UserProfileService userProfileService;

    @Autowired
    RedisTemplate<String, Object> redisTemplate;

    @Test
    void shouldFailToInstantiateUserProfile() {
        redisTemplate.opsForValue()
                        .set("userProfile:1", new OldUserProfile(1L, "Ibrahim", "Gunduz"));

        assertThatThrownBy(() -> userProfileService.getProfile(1L))
                .isInstanceOf(Exception.class);
    }
}