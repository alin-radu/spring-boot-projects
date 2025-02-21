package com.dev.blog_platform.services;

import com.dev.blog_platform.domain.entities.User;

import java.util.UUID;

public interface UserService {
    User findUserById(UUID userId);
}
