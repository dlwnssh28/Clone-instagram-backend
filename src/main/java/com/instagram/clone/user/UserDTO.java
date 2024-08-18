package com.instagram.clone.user;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

import lombok.AllArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private String id;
    private String userId;
    private String name;
    private String password;
    private String email;
    private String phone;
    private String gender;
    private String introduce;
    private String profileImg;
    private String socialId;
    private boolean privateFlag; // false: 공개 계정, true: 비공개 계정
    private LocalDateTime createdAt;
    private boolean isFollowing; 

    public UserDTO(String id, String userId, String name, String password, String email, String phone, String gender, String introduce, String profileImg, String socialId, boolean privateFlag, LocalDateTime createdAt) {
        this.id = id;
        this.userId = userId;
        this.name = name;
        this.password = password;
        this.email = email;
        this.phone = phone;
        this.gender = gender;
        this.introduce = introduce;
        this.profileImg = profileImg;
        this.socialId = socialId;
        this.privateFlag = privateFlag;
        this.createdAt = createdAt;
    }

    // 프로필 이미지와 userId만 설정하는 경우를 위한 생성자
    public UserDTO(String userId, String profileImg) {
        this.userId = userId;
        this.profileImg = profileImg;
    }
}
