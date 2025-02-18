package com.dev.blog_platform.services;

import com.dev.blog_platform.domain.entities.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagService {
    List<Tag> findAllTagsWithPostCount();
    List<Tag> createTags(Set<String> tagNames);
    void deleteTag(UUID tagId);
}
