package com.dev.blog_platform.services;

import com.dev.blog_platform.domain.CreatePostRequest;
import com.dev.blog_platform.domain.UpdatePostRequest;
import com.dev.blog_platform.domain.entities.Post;
import com.dev.blog_platform.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {
    Post findPostByPostId(UUID postId);

    List<Post> findAll(UUID categoryId, UUID tagId);

    List<Post> findAllDraftPostsByUser(User user);

    Post createPost(User user, CreatePostRequest createPostRequest);

    Post updatePost(UUID postId, UpdatePostRequest updatePostRequest);

    void deletePostById(UUID postId);
}
