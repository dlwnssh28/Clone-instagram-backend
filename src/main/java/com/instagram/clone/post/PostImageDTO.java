package com.instagram.clone.post;

import lombok.Data;

@Data
public class PostImageDTO {
    private Integer postImageId;
    private String url;
    private String alt;

    public PostImageDTO() {
        // 기본 생성자
    }

    public PostImageDTO(PostImageEntity postImage) {
        this.postImageId = postImage.getPostImageId();
        this.url = postImage.getUrl();
        this.alt = postImage.getAlt();
    }
}
