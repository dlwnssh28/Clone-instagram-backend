package com.instagram.clone.bookmark;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookmarkService {

    @Autowired
    private BookmarkRepository BookmarkRepository;

    public boolean savePost(String userId, Integer postId) {
        try {
            if (BookmarkRepository.existsByUserIdAndPostId(userId, postId)) {
                return false;  // 이미 좋아요를 누른 경우
            }

            BookmarkEntity postSave = new BookmarkEntity();
            postSave.setUserId(userId);
            postSave.setPostId(postId);

            BookmarkRepository.save(postSave);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public boolean checkIfUserSavedPost(String userId, Integer postId) {
        return BookmarkRepository.existsByUserIdAndPostId(userId, postId);
    }

    public boolean cancleSavePost(String userId, Integer postId) {
        try {
            // 1. userId와 postId로 BookmarkEntity 찾기
            BookmarkEntity postSave = BookmarkRepository.findByUserIdAndPostId(userId, postId);
            if (postSave != null) {
                // 2. 찾은 엔티티를 삭제
                BookmarkRepository.delete(postSave);
                return true;
            }
            return false; // 좋아요가 존재하지 않는 경우
        } catch (Exception e) {
            return false;
        }
    }
}
