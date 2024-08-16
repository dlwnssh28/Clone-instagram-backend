package com.instagram.clone.block;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BlockDTO {
    private Integer blockId;
    private String fromUserId;
    private String toUserId;
}
