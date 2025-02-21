package com.dev.blog_platform.services.impl;

import com.dev.blog_platform.domain.CreatePostRequest;
import com.dev.blog_platform.domain.PostStatus;
import com.dev.blog_platform.domain.UpdatePostRequest;
import com.dev.blog_platform.domain.entities.Category;
import com.dev.blog_platform.domain.entities.Post;
import com.dev.blog_platform.domain.entities.Tag;
import com.dev.blog_platform.domain.entities.User;
import com.dev.blog_platform.repositories.PostRepository;
import com.dev.blog_platform.services.CategoryService;
import com.dev.blog_platform.services.PostService;
import com.dev.blog_platform.services.TagService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;
    private final CategoryService categoryService;
    private final TagService tagService;

    private static final int WORDS_PER_MINUTE = 200;

    @Override
    public Post findPostByPostId(UUID postId) {
        return postRepository.findById(postId).orElseThrow(
                () -> new EntityNotFoundException("Post does not exist with id: " + postId + ".")
        );
    }

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

    @Override
    public List<Post> findAllDraftPostsByUser(User user) {
        return postRepository.findAllByAuthorAndStatus(user, PostStatus.DRAFT);
    }

    @Override
    @jakarta.transaction.Transactional
    public Post createPost(User user, CreatePostRequest createPostRequest) {

        Post newPost = new Post();
        newPost.setTitle(createPostRequest.getTitle());
        newPost.setContent(createPostRequest.getContent());
        newPost.setStatus(createPostRequest.getStatus());
        newPost.setAuthor(user);
        newPost.setReadingTime(calculateReadingTime(createPostRequest.getContent()));
        newPost.setCategory(categoryService.findCategoryById(createPostRequest.getCategoryId()));
        newPost.setTags(new HashSet<>(tagService.findTagByIds(createPostRequest.getTagIds())));

        return postRepository.save(newPost);
    }

    @Override
    @jakarta.transaction.Transactional
    public Post updatePost(UUID postId, UpdatePostRequest updatePostRequest) {

        Post existingPost = findPostByPostId(postId);

        existingPost.setTitle(updatePostRequest.getTitle());
        existingPost.setContent(updatePostRequest.getContent());
        existingPost.setStatus(updatePostRequest.getStatus());
        existingPost.setReadingTime(calculateReadingTime(updatePostRequest.getContent()));

        if (!existingPost.getCategory().getId().equals(updatePostRequest.getCategoryId())) {
            existingPost.setCategory(categoryService.findCategoryById(updatePostRequest.getCategoryId()));
        }

        Set<UUID> existingPostTagsIds = existingPost.getTags()
                .stream()
                .map(Tag::getId)
                .collect(Collectors.toSet());

        Set<UUID> updatePostRequestTagIds = updatePostRequest.getTagIds();

        if (!existingPostTagsIds.equals(updatePostRequestTagIds)) {
            List<Tag> newTags = tagService.findTagByIds(updatePostRequestTagIds);
            existingPost.setTags(new HashSet<>(newTags));
        }

        return postRepository.save(existingPost);
    }
    @Override
    public void deletePostById(UUID postId) {
        Post existingPost = findPostByPostId(postId);

        postRepository.delete(existingPost);
    }

    private Integer calculateReadingTime(String content) {
        if (content == null || content.isEmpty()) {
            return 0;
        }

        int wordCount = content.trim().split("\\s+").length;

        return (int) Math.ceil((double) wordCount / WORDS_PER_MINUTE);

    }
}
