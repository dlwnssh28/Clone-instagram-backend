package com.instagram.clone.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PostLikeService {

    @Autowired
    private PostLikeRepository postLikeRepository;

    public boolean likePost(String userId, Integer postId) {
        try {
            if (postLikeRepository.existsByUserIdAndPostId(userId, postId)) {
                return false;  // 이미 좋아요를 누른 경우
            }

            PostLikeEntity postLike = new PostLikeEntity();
            postLike.setUserId(userId);
            postLike.setPostId(postId);

            postLikeRepository.save(postLike);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkIfUserLikedPost(String userId, Integer postId) {
        return postLikeRepository.existsByUserIdAndPostId(userId, postId);
    }
}
