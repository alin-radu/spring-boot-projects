package com.dev.blog_platform.services.impl;

import com.dev.blog_platform.domain.entities.Tag;
import com.dev.blog_platform.repositories.TagRepository;
import com.dev.blog_platform.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TagServiceImpl implements TagService {

    private final TagRepository tagRepository;

    @Override
    public List<Tag> findAllTagsWithPostCount() {
        return tagRepository.findAllWithPostCount();
    }

    @Transactional
    @Override
    public List<Tag> createTags(Set<String> tagNames) {
        List<Tag> existingTags = tagRepository.findByNameIn(tagNames);
        Set<String> existingTagNames = existingTags.stream()
                .map(Tag::getName)
                .collect(Collectors.toSet());

        List<Tag> newTags = tagNames.stream()
                .filter(name -> !existingTagNames.contains(name))
                .map(name -> Tag.builder()
                        .name(name)
                        .posts(new HashSet<>())
                        .build()
                )
                .toList();

        List<Tag> savedTags = new ArrayList<>();
        if (!newTags.isEmpty()) {
            savedTags = tagRepository.saveAll(newTags);
        }

        savedTags.addAll(existingTags);

        return savedTags;
    }

    @Transactional
    @Override
    public void deleteTag(UUID tagId) {

        // v1
        tagRepository.findById(tagId).ifPresent(tag -> {
            if (!tag.getPosts().isEmpty()) {
                throw new IllegalStateException("Tag has posts associated with it.");
            }

            tagRepository.deleteById(tagId);
        });

        // v2
//        Optional<Tag> tag = tagRepository.findById
//        if (tag.isPresent()) {
//            if (!tag.get().getPosts().isEmpty()) {
//                throw new IllegalStateException("Tag has posts associated with it.");
//            }
//
//            tagRepository.deleteById(tagId);
//        }
    }

    @Override
    public Tag findTagById(UUID tagId) {

        return tagRepository.findById(tagId)
                .orElseThrow(() -> new EntityNotFoundException("Tag not found with id: " + tagId + "."));
    }

    @Override
    public List<Tag> findTagByIds(Set<UUID> ids) {
        List<Tag> foundTags = tagRepository.findAllById(ids);
        if (foundTags.size() != ids.size()) {
            throw new EntityNotFoundException("Not all specified tag IDs exist.");
        }

        return foundTags;
    }
}
