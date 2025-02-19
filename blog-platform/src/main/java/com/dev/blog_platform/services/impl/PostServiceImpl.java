package com.dev.blog_platform.services.impl;

import com.dev.blog_platform.domain.PostStatus;
import com.dev.blog_platform.domain.entities.Category;
import com.dev.blog_platform.domain.entities.Post;
import com.dev.blog_platform.domain.entities.Tag;
import com.dev.blog_platform.repositories.PostRepository;
import com.dev.blog_platform.services.CategoryService;
import com.dev.blog_platform.services.PostService;
import com.dev.blog_platform.services.TagService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    @Transactional(readOnly = true)
    @Override
    public List<Post> findAll(UUID categoryId, UUID tagId) {
        if (categoryId != null && tagId != null) {
            Category category = categoryService.findCategoryById(categoryId);
            Tag tag = tagService.findTagById(tagId);
            return postRepository.findAllByStatusAndCategoryAndTagsContaining(
                    PostStatus.PUBLISHED, category, tag);
        }

        if (categoryId != null) {
            Category category = categoryService.findCategoryById(categoryId);
            return postRepository.findAllByStatusAndCategory(
                    PostStatus.PUBLISHED, category
            );
        }

        if (tagId != null) {
            Tag tag = tagService.findTagById(tagId);
            return postRepository.findAllByStatusAndTagsContaining(
                    PostStatus.PUBLISHED, tag
            );
        }

        return postRepository.findAllByStatus(PostStatus.PUBLISHED);
    }
}
