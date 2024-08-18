package com.instagram.clone.block;

import com.instagram.clone.user.UserEntity;

public class BlockMapper {

    public static BlockDTO toDto(BlockEntity blockEntity) {
        BlockDTO blockDTO = new BlockDTO();
        blockDTO.setBlockId(blockEntity.getBlockId());
        blockDTO.setFromUserId(blockEntity.getFromUser().getUserId());
        blockDTO.setToUserId(blockEntity.getToUser().getUserId());
        return blockDTO;
    }

    public static BlockEntity toEntity(BlockDTO blockDTO, UserEntity fromUser, UserEntity toUser) {
        BlockEntity blockEntity = new BlockEntity();
        blockEntity.setBlockId(blockDTO.getBlockId());
        blockEntity.setFromUser(fromUser);
        blockEntity.setToUser(toUser);
        return blockEntity;
    }
}
