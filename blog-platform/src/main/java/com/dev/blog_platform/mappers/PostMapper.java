package com.dev.blog_platform.mappers;

import com.dev.blog_platform.domain.CreatePostRequest;
import com.dev.blog_platform.domain.UpdatePostRequest;
import com.dev.blog_platform.domain.dtos.CreatePostRequestDto;
import com.dev.blog_platform.domain.dtos.PostResponseDto;
import com.dev.blog_platform.domain.dtos.UpdatePostRequestDto;
import com.dev.blog_platform.domain.entities.Post;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface PostMapper {

    @Mapping(target = "status", source = "status")
    @Mapping(target = "author", source = "author")
    @Mapping(target = "category", source = "category")
    @Mapping(target = "tags", source = "tags")
    PostResponseDto toPostResponseDto(Post post);

    CreatePostRequest toCreatePostRequest(CreatePostRequestDto createPostRequestDto);

    UpdatePostRequest toUpdatePostRequest(UpdatePostRequestDto updatePostRequestDto);
}
