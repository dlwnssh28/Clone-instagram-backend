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
    private boolean privateFlag;
    private LocalDateTime CreateAt;
}
