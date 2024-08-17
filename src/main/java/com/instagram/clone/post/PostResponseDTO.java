package com.instagram.clone.post;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostResponseDTO {
    private Integer postId;
    private String userId;
    private String content;
    private LocalDateTime createdAt;
    private boolean commentFlag;
    private boolean showFlag;
    private List<PostImageDTO> images;

    public PostResponseDTO(PostEntity post) {
        this.postId = post.getPostId();
        this.userId = post.getUserId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.commentFlag = post.isCommentFlag();
        this.showFlag = post.isShowFlag();
    }

    public PostResponseDTO(PostEntity post, List<PostImageEntity> images) {
        this.postId = post.getPostId();
        this.userId = post.getUserId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.commentFlag = post.isCommentFlag();
        this.showFlag = post.isShowFlag();
        this.images = images.stream()
                .map(PostImageDTO::new)  // 메서드 참조 사용
                .collect(Collectors.toList());
    }

}
