package com.instagram.clone.post;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostResponseDTO {
    private Integer postId;
    private String userId;
    private String content;
    private LocalDateTime createdAt;
    private boolean commentFlag;
    private boolean showFlag;

    public PostResponseDTO(PostEntity post) {
        this.postId = post.getPostId();
        this.userId = post.getUserId();
        this.content = post.getContent();
        this.createdAt = post.getCreatedAt();
        this.commentFlag = post.isCommentFlag();
        this.showFlag = post.isShowFlag();
    }
}
