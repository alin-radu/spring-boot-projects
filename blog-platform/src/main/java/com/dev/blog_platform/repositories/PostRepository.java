package com.dev.blog_platform.repositories;

import com.dev.blog_platform.domain.PostStatus;
import com.dev.blog_platform.domain.entities.Category;
import com.dev.blog_platform.domain.entities.Post;
import com.dev.blog_platform.domain.entities.Tag;
import com.dev.blog_platform.domain.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface PostRepository extends JpaRepository<Post, UUID> {
    List<Post> findAllByStatusAndCategoryAndTagsContaining(
            PostStatus status, Category category, Tag tag);

    List<Post> findAllByStatusAndCategory(
            PostStatus status, Category category);

    List<Post> findAllByStatusAndTagsContaining(
            PostStatus status, Tag tag);

    List<Post> findAllByStatus(PostStatus status);

    List<Post> findAllByAuthorAndStatus(User author, PostStatus status);
}
