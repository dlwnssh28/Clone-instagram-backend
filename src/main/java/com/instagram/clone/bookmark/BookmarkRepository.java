package com.instagram.clone.bookmark;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookmarkRepository extends JpaRepository<BookmarkEntity, Integer> {
    boolean existsByUserIdAndPostId(String userId, Integer postId);
    BookmarkEntity findByUserIdAndPostId(String userId, Integer postId);
}

