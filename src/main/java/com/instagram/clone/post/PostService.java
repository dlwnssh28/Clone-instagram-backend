package com.instagram.clone.post;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class PostService {

    private final PostRepository postRepository;
    private final PostImageRepository postImageRepository;
    private static final String UPLOAD_DIR = System.getProperty("user.dir") + "/uploads";

    public PostService(PostRepository postRepository, PostImageRepository postImageRepository) {
        this.postRepository = postRepository;
        this.postImageRepository = postImageRepository;
    }

    @Transactional
    public PostResponseDTO createPost(PostRequestDTO postRequestDTO, List<MultipartFile> images, List<String> alts) {
        // Create and save the post entity
        PostEntity postEntity = new PostEntity();
        postEntity.setUserId(postRequestDTO.getUserId());
        postEntity.setContent(postRequestDTO.getContent());
        postEntity.setCreatedAt(LocalDateTime.now());
        postEntity.setCommentFlag(postRequestDTO.isCommentFlag());
        postEntity.setShowFlag(postRequestDTO.isShowFlag());
        postRepository.save(postEntity);
        System.out.println("Saved PostEntity ID: " + postEntity.getPostId());

        // Save each image with the associated post entity
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            String alt = alts.get(i);

            PostImageEntity postImageEntity = new PostImageEntity();
            postImageEntity.setUrl(saveImage(image)); // Save image and get URL
            postImageEntity.setAlt(alt);
            postImageEntity.setPost(postEntity); // Associate PostEntity with PostImageEntity

            // Log details of the PostImageEntity before saving
            System.out.println("Saving PostImageEntity with Post ID: " + postEntity.getPostId() + ", Alt: " + alt);
            postImageRepository.save(postImageEntity);
        }

        return new PostResponseDTO(postEntity);
    }

    private final String uploadDir = System.getProperty("user.dir") + "/src/main/resources/static/uploads";

    private String saveImage(MultipartFile image) {
        try {
            // 업로드 폴더가 존재하지 않으면 생성
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // 파일 저장
            String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            image.transferTo(filePath);

            // 파일의 상대 URL 반환 (예: /uploads/파일이름)
            return "/uploads/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image", e);
        }
    }

    private String generateFileName(String originalFilename) {
        String extension = getFileExtension(originalFilename);
        return UUID.randomUUID().toString() + "." + extension;
    }

    private String getFileExtension(String filename) {
        return filename.substring(filename.lastIndexOf(".") + 1);
    }

    private void createDirectoryIfNotExists(Path dirPath) throws IOException {
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }
    }

    public PostResponseDTO getPostById(Integer postId) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾을 수 없습니다."));
        return new PostResponseDTO(postEntity);
    }

    public List<PostResponseDTO> getPostsByUserId(String userId) {
        List<PostEntity> posts = postRepository.findByUserId(userId);
        return posts.stream()
                .map(post -> {
                    List<PostImageEntity> postImages = post.getPostImages(); // postId에 해당하는 모든 이미지 가져오기
                    int totalImageCount = postImages.size(); // 전체 이미지 개수 계산

                    // 썸네일 용도로 첫 번째 이미지만 선택 (0번째 이미지)
                    List<PostImageEntity> thumbnailImage = List.of(postImages.get(0));

                    return new PostResponseDTO(post, thumbnailImage, totalImageCount);
                })
                .collect(Collectors.toList());
    }

}
