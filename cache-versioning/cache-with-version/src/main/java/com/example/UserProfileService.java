package com.example;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

@Service
public class UserProfileService {
    private final UserRepository userRepository;

    public UserProfileService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Cacheable("userProfile")
    public UserProfile getProfile(Long userId) {
        User user = userRepository.findById(userId).orElseThrow();
        return new UserProfile(
                user.getId(),
                user.getFirstName(),
                user.getLastName()
        );
    }
}
