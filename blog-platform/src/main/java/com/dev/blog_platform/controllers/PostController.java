package com.dev.blog_platform.controllers;

import com.dev.blog_platform.domain.CreatePostRequest;
import com.dev.blog_platform.domain.UpdatePostRequest;
import com.dev.blog_platform.domain.dtos.CreatePostRequestDto;
import com.dev.blog_platform.domain.dtos.PostResponseDto;
import com.dev.blog_platform.domain.dtos.UpdatePostRequestDto;
import com.dev.blog_platform.domain.entities.Post;
import com.dev.blog_platform.domain.entities.User;
import com.dev.blog_platform.mappers.PostMapper;
import com.dev.blog_platform.services.PostService;
import com.dev.blog_platform.services.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
    private final UserService userService;
    private final PostMapper postMapper;

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> findAllPosts(
            @RequestParam(required = false) UUID categoryId,
            @RequestParam(required = false) UUID tagId
    ) {
        List<PostResponseDto> postResponse = postService.findAll(categoryId, tagId).stream()
                .map(postMapper::toPostResponseDto).toList();

        return ResponseEntity.ok(postResponse);
    }

    @GetMapping(path = "/drafts")
    public ResponseEntity<List<PostResponseDto>> findAllDrafts(@RequestAttribute UUID userId) {
        User loggedInUser = userService.findUserById(userId);

        List<Post> draftPostsByUser = postService.findAllDraftPostsByUser(loggedInUser);

        List<PostResponseDto> postResponseDto = draftPostsByUser.stream()
                .map(postMapper::toPostResponseDto)
                .toList();

        return ResponseEntity.ok(postResponseDto);
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> createPost(
            @Valid @RequestBody CreatePostRequestDto createPostRequestDto,
            @RequestAttribute UUID userId
    ) {
        User loggedInUser = userService.findUserById(userId);

        CreatePostRequest createPostRequest = postMapper.toCreatePostRequest(createPostRequestDto);

        Post savedPost = postService.createPost(loggedInUser, createPostRequest);

        return ResponseEntity.ok(postMapper.toPostResponseDto(savedPost));
    }

    @GetMapping(path = "/{postId}")
    public ResponseEntity<PostResponseDto> findPost(
            @PathVariable UUID postId
    ) {
        Post post = postService.findPostByPostId(postId);

        return ResponseEntity.ok(postMapper.toPostResponseDto(post));
    }

    @PutMapping(path = "/{postId}")
    public ResponseEntity<PostResponseDto> updatePost(
            @PathVariable UUID postId,
            @Valid @RequestBody UpdatePostRequestDto updatePostRequestDto) {

        UpdatePostRequest updatePostRequest = postMapper.toUpdatePostRequest(updatePostRequestDto);

        Post updatedPost = postService.updatePost(postId, updatePostRequest);

        return ResponseEntity.ok(postMapper.toPostResponseDto(updatedPost));
    }

    @DeleteMapping(path = "/{postId}")
    public ResponseEntity<Void> deletePost(
            @PathVariable UUID postId
    ) {
        postService.deletePostById(postId);

        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );

    }

}
