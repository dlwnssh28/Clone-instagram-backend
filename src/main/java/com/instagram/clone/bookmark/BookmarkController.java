package com.instagram.clone.bookmark;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/bookmark")
public class BookmarkController {

    @Autowired
    private BookmarkService bookmarkService;

    @PostMapping
    public ResponseEntity<String> savePost(@RequestBody BookmarkDTO dto) {
        boolean isLiked = bookmarkService.savePost(dto.getUserId(), dto.getPostId());
        if (isLiked) {
            return ResponseEntity.ok("Post saved successfully");
        } else {
            return ResponseEntity.badRequest().body("Post save failed");
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> checkSaveStatus(@RequestParam String userId, @RequestParam Integer postId) {
        boolean isLiked = bookmarkService.checkIfUserSavedPost(userId, postId);
        return ResponseEntity.ok(isLiked);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancleSavePost(@RequestBody BookmarkDTO dto) {
        boolean isCancled = bookmarkService.cancleSavePost(dto.getUserId(), dto.getPostId());
        if (isCancled) {
            return ResponseEntity.ok("Post unsaved successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to unsaved post");
        }
    }
}
