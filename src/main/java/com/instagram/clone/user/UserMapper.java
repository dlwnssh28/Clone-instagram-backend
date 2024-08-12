package com.instagram.clone.user;

import java.util.UUID;

public class UserMapper {

    public static UserDTO toDto(UserEntity user) {
        return new UserDTO(
            user.getId(),
            user.getUserId(),
            user.getName(),
            user.getPassword(),
            user.getEmail(),
            user.getPhone(),
            user.getGender(),
            user.getIntroduce(),
            user.getProfileImg(),
            user.getSocialId(),
            user.isPrivateFlag(),
            user.getCreatedAt()
        );
    }

    public static UserEntity toEntity(UserDTO userDTO) {
        UserEntity user = new UserEntity();
        user.setId(userDTO.getId() != null ? userDTO.getId() : UUID.randomUUID().toString().replace("-", ""));
        user.setUserId(userDTO.getUserId());
        user.setName(userDTO.getName());
        user.setPassword(userDTO.getPassword());
        user.setEmail(userDTO.getEmail());
        user.setPhone(userDTO.getPhone() != null ? userDTO.getPhone() : "000-0000-0000");
        user.setGender(userDTO.getGender());
        user.setIntroduce(userDTO.getIntroduce());
        user.setProfileImg(userDTO.getProfileImg());
        user.setSocialId(userDTO.getSocialId());
        user.setCreatedAt(userDTO.getCreateAt());
        user.setPrivateFlag(userDTO.isPrivateFlag());
        return user;
    }
}
