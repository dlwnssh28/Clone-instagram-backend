package com.instagram.clone.auth;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class SignupDTO {
    private String email;
    private String phone;
    private String userId;
    private String name;
    private String password;
}
