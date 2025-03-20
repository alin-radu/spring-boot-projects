package com.dev.blog_platform.service;

import com.dev.blog_platform.domain.CreatePostRequest;
import com.dev.blog_platform.domain.UpdatePostRequest;
import com.dev.blog_platform.domain.entities.Post;
import com.dev.blog_platform.domain.entities.User;

import java.util.List;
import java.util.UUID;

public interface PostService {

    Post createPost(User user, CreatePostRequest createPostRequest);
    List<Post> findAllDraftPostsByUser(User user);
    List<Post> findAll(UUID categoryId, UUID tagId);
    Post findPostByPostId(UUID postId);
    Post updatePost(UUID postId, UpdatePostRequest updatePostRequest);
    void deletePostById(UUID postId);
}
