package com.dev.blog_platform.controllers;

import com.dev.blog_platform.domain.dtos.PostResponseDto;
import com.dev.blog_platform.mappers.PostMapper;
import com.dev.blog_platform.services.PostService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/posts")
@RequiredArgsConstructor
public class PostController {

    private final PostService postService;
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
    public ResponseEntity<List<PostResponseDto>> findAllDrafts(
            @RequestAttribute UUID userId
    ){


    }

}
