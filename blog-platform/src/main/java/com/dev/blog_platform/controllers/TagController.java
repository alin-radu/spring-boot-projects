package com.dev.blog_platform.controllers;

import com.dev.blog_platform.domain.dtos.CreateTagsRequestDto;
import com.dev.blog_platform.domain.dtos.TagResponseDto;
import com.dev.blog_platform.domain.entities.Tag;
import com.dev.blog_platform.mappers.TagMapper;
import com.dev.blog_platform.services.TagService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/tags")
@RequiredArgsConstructor
public class TagController {

    private final TagService tagService;
    private final TagMapper tagMapper;

    @GetMapping
    public ResponseEntity<List<TagResponseDto>> findAllTags() {

        List<TagResponseDto> tagsResponseDto =
                tagService.findAllTagsWithPostCount().stream()
                        .map(tagMapper::toTagResponseDto)
                        .toList();

        return ResponseEntity.ok(tagsResponseDto);
    }

    @PostMapping
    public ResponseEntity<List<TagResponseDto>> createTags(
            @Valid @RequestBody CreateTagsRequestDto createTagsRequestDto
    ) {
        List<Tag> savedTags = tagService.createTags(createTagsRequestDto.getNames());

        List<TagResponseDto> tagsResponseDto = savedTags.stream()
                .map(tagMapper::toTagResponseDto)
                .toList();

        return ResponseEntity.ok(tagsResponseDto);
    }

    @DeleteMapping(path = "/{tagId}")
    public ResponseEntity<Void> deleteTag(@PathVariable UUID tagId) {
        tagService.deleteTag(tagId);

        return new ResponseEntity<>(
                HttpStatus.NO_CONTENT
        );
    }

}
