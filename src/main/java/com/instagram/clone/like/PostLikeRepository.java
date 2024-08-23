package com.instagram.clone.like;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Integer> {
    boolean existsByUserIdAndPostId(String userId, Integer postId);
    PostLikeEntity findByUserIdAndPostId(String userId, Integer postId);
}

