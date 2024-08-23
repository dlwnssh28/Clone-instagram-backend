package com.instagram.clone.like;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/postlike")
public class PostLikeController {

    @Autowired
    private PostLikeService postLikeService;

    @PostMapping
    public ResponseEntity<String> likePost(@RequestBody PostLikeDTO dto) {
        boolean isLiked = postLikeService.likePost(dto.getUserId(), dto.getPostId());
        if (isLiked) {
            return ResponseEntity.ok("Post liked successfully");
        } else {
            return ResponseEntity.badRequest().body("Post like failed");
        }
    }

    @GetMapping("/status")
    public ResponseEntity<Boolean> checkLikeStatus(@RequestParam String userId, @RequestParam Integer postId) {
        boolean isLiked = postLikeService.checkIfUserLikedPost(userId, postId);
        return ResponseEntity.ok(isLiked);
    }

    @PostMapping("/cancel")
    public ResponseEntity<String> cancleLikePost(@RequestBody PostLikeDTO dto) {
        boolean isCancled = postLikeService.cancleLikePost(dto.getUserId(), dto.getPostId());
        if (isCancled) {
            return ResponseEntity.ok("Post unliked successfully");
        } else {
            return ResponseEntity.badRequest().body("Failed to unlike post");
        }
    }
}
