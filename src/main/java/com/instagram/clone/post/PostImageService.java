package com.instagram.clone.post;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PostImageService {

    private final PostImageRepository postImageRepository;
    private final PostRepository postRepository;

    public PostImageService(PostImageRepository postImageRepository, PostRepository postRepository) {
        this.postImageRepository = postImageRepository;
        this.postRepository = postRepository;
    }

    @Transactional
    public void savePostImages(Integer postId, List<PostImageDTO> postImageDTOList) {
        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("게시물을 찾지 못했습니다"));

        for (PostImageDTO dto : postImageDTOList) {
            PostImageEntity postImageEntity = new PostImageEntity();
            postImageEntity.setUrl(dto.getUrl());
            postImageEntity.setAlt(dto.getAlt());
            postImageEntity.setPost(postEntity);

            postImageRepository.save(postImageEntity);
        }
    }

//    public List<PostImageDTO> getImagesByPost(Integer postId) {
//        PostEntity postEntity = postRepository.findById(postId)
//                .orElseThrow(() -> new RuntimeException("Post not found"));
//
//        List<PostImageEntity> postImageEntities = postImageRepository.findByPostId(postEntity.getPostId());
//        return postImageEntities.stream()
//                .map(this::convertToDTO)
//                .collect(Collectors.toList());
//    }

    private PostImageDTO convertToDTO(PostImageEntity entity) {
        return new PostImageDTO(entity); // 매개변수를 사용하는 생성자 호출
    }

}
