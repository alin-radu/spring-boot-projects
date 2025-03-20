package com.dev.blog_platform.service;

import com.dev.blog_platform.domain.entities.User;

import java.util.UUID;

public interface UserService {

    User findUserById(UUID userId);
}
