package com.dev.blog_platform.services;

import com.dev.blog_platform.domain.entities.Post;

import java.util.List;
import java.util.UUID;

public interface PostService {
    List<Post> findAll(UUID categoryId, UUID tagId);
}
