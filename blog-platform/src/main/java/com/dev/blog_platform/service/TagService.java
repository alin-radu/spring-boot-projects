package com.dev.blog_platform.service;

import com.dev.blog_platform.domain.entities.Tag;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface TagService {

    List<Tag> createTags(Set<String> tagNames);
    List<Tag> findAllTagsWithPostCount();
    List<Tag> findTagByIds(Set<UUID> ids);
    Tag findTagById(UUID tagId);
    void deleteTag(UUID tagId);
}
