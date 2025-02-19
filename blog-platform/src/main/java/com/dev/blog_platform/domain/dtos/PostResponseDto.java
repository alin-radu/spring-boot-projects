package com.dev.blog_platform.domain.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.UUID;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PostResponseDto {

    private UUID id;
    private String title;
    private String content;
    private AuthResponseDto author;
    private CategoryResponseDto category;
    private Set<TagResponseDto> tags;
    private Integer readingTime;
    private LocalDateTime createdDate;
    private LocalDateTime updatedDate;
}
