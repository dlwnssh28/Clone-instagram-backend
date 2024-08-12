package com.instagram.clone.post;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/posts/{postId}/images")
public class PostImageController {

    private final PostImageService postImageService;

    public PostImageController(PostImageService postImageService) {
        this.postImageService = postImageService;
    }

    @PostMapping
    public ResponseEntity<Void> addImagesToPost(
            @PathVariable Integer postId,
            @RequestBody List<PostImageDTO> postImageDTOList) {
        postImageService.savePostImages(postId, postImageDTOList);
        return ResponseEntity.ok().build();
    }

//    @GetMapping
//    public ResponseEntity<List<PostImageDTO>> getImagesByPost(@PathVariable Integer postId) {
//        List<PostImageDTO> postImageDTOList = postImageService.getImagesByPost(postId);
//        return ResponseEntity.ok(postImageDTOList);
//    }
}
