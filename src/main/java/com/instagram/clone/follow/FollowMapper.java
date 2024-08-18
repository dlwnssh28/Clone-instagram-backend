package com.instagram.clone.follow;

import com.instagram.clone.user.UserDTO;
import com.instagram.clone.user.UserEntity;

public class FollowMapper {

    public static FollowDTO toDto(FollowEntity followEntity) {
        FollowDTO followDTO = new FollowDTO();
        followDTO.setFollowId(followEntity.getFollowId());

        // fromUser와 toUser의 userId와 profileImg를 설정
        followDTO.setFromUser(new UserDTO(
            followEntity.getFromUser().getUserId(),
            followEntity.getFromUser().getProfileImg()
        ));
        followDTO.setToUser(new UserDTO(
            followEntity.getToUser().getUserId(),
            followEntity.getToUser().getProfileImg()
        ));

        followDTO.setRequest(followEntity.isRequest());
        followDTO.setCreatedAt(followEntity.getCreatedAt());

        return followDTO;
    }
}
