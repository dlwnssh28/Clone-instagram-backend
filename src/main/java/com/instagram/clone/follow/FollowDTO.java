package com.instagram.clone.follow;

import com.instagram.clone.user.UserDTO;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class FollowDTO {

    private Integer followId;
    private UserDTO fromUser;
    private UserDTO toUser;
    private boolean request;  // 요청 상태 (false: 요청됨, true: 수락됨 또는 공개 계정에서 자동 수락됨)
							  // false: 팔로우 요청이 전송된 상태 (비공개 계정의 경우).
							  // true: 팔로우가 수락된 상태 또는 공개 계정에서 자동으로 팔로우가 수락된 상태.
    private LocalDateTime createdAt;
}
