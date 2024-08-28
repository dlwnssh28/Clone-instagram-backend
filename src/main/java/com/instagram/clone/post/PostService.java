package com.instagram.clone.post;

import com.instagram.clone.bookmark.BookmarkEntity;
import com.instagram.clone.bookmark.BookmarkRepository;
import org.springframework.beans.factory.annotation.Autowired;
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
import java.util.Optional;
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
        // 게시물 엔티티 생성 및 저장
        PostEntity postEntity = new PostEntity();
        postEntity.setUserId(postRequestDTO.getUserId());
        postEntity.setContent(postRequestDTO.getContent());
        postEntity.setCreatedAt(LocalDateTime.now());
        postEntity.setCommentFlag(postRequestDTO.isCommentFlag());
        postEntity.setShowFlag(postRequestDTO.isShowFlag());
        postRepository.save(postEntity);

        // 각 이미지와 연결된 PostImageEntity 생성 및 저장
        for (int i = 0; i < images.size(); i++) {
            MultipartFile image = images.get(i);
            String alt = alts.get(i);

            PostImageEntity postImageEntity = new PostImageEntity();
            postImageEntity.setUrl(saveImage(image)); // 이미지 파일 저장 후 URL 반환
            postImageEntity.setAlt(alt);
            postImageEntity.setPost(postEntity);

            postImageRepository.save(postImageEntity);
        }

        return new PostResponseDTO(postEntity);
    }

    // 파일 저장 디렉토리 경로 설정
    private final String uploadDir = System.getProperty("user.dir") + "/uploads";

    private String saveImage(MultipartFile image) {
        try {

            // 업로드 폴더가 존재하지 않으면 생성
            Path uploadPath = Paths.get(uploadDir);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath); // 디렉토리 생성
            }

            // 파일 이름 생성
            String fileName = UUID.randomUUID().toString() + "_" + image.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);

            // 파일을 지정된 경로에 저장 (임시 파일을 사용하지 않음)
            Files.write(filePath, image.getBytes()); // 파일을 직접 디스크에 기록

            // 파일의 상대 URL 반환 (예: /uploads/파일이름)
            String fileUrl = "/uploads/" + fileName;
            return fileUrl;
        } catch (IOException e) {
            throw new RuntimeException("이미지 저장 실패", e);
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

    public PostResponseDTO getPostById(Integer postId) {
        Optional<PostEntity> postEntityOptional = postRepository.findById(postId);
        if (postEntityOptional.isPresent()) {
            return new PostResponseDTO(postEntityOptional.get());
        } else {
            return null;
        }
    }

    @Autowired
    private BookmarkRepository bookmarkRepository;

    public List<PostResponseDTO> getSavedPosts(String id) {
        List<BookmarkEntity> bookmarks = bookmarkRepository.findByUserId(id);
        List<Integer> postIds = bookmarks.stream().map(BookmarkEntity::getPostId).collect(Collectors.toList());

        List<PostEntity> posts = postRepository.findByPostIdIn(postIds);

        return posts.stream().map(post -> {
            List<PostImageEntity> postImages = post.getPostImages();
            return new PostResponseDTO(post, postImages, postImages.size());
        }).collect(Collectors.toList());
    }

}
