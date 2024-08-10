package com.instagram.clone.post;

import lombok.Data;

import java.util.List;

@Data
public class PostRequestDTO {
    private String userId;
    private String content;
    private boolean commentFlag;
    private boolean showFlag;
    private List<PostImageDTO> images;
}
