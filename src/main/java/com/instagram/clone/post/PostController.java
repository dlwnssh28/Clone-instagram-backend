package com.instagram.clone.post;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @PostMapping
    public ResponseEntity<PostResponseDTO> createPost(
            @RequestParam("post") String postJson,
            @RequestParam("images") List<MultipartFile> images,
            @RequestParam("alts") List<String> alts) {

        // 'application/octet-stream' is not supported 오류
        // JSON 데이터를 DTO로 변환
        ObjectMapper objectMapper = new ObjectMapper();
        PostRequestDTO postRequestDTO;
        try {
            postRequestDTO = objectMapper.readValue(postJson, PostRequestDTO.class);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("유효하지 않은 데이터 타입", e);
        }

        System.out.println("게시물 등록 시도");
        PostResponseDTO postResponseDTO = postService.createPost(postRequestDTO, images, alts);
        System.out.println("등록할 게시물 정보: " + postResponseDTO);
        return ResponseEntity.ok(postResponseDTO);
    }


    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDTO> getPostById(@PathVariable Integer id) {
        PostResponseDTO postResponseDTO = postService.getPostById(id);
        return ResponseEntity.ok(postResponseDTO);
    }
}
