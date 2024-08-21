package com.instagram.clone.like;

import com.instagram.clone.post.PostEntity;
import com.instagram.clone.user.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostLikeRepository extends JpaRepository<PostLikeEntity, Integer> {
    boolean existsByUserIdAndPostId(String userId, Integer postId);
}

