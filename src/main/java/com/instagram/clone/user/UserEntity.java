package com.instagram.clone.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "user")
@Getter
@Setter
public class UserEntity {

    @Id
    @Column(length = 32, nullable = false)
    private String id;

    @Column(name = "user_id", length = 100, nullable = false)
    private String userId;

    @Column(length = 100, nullable = false)
    private String name;

    @Column(length = 100, nullable = false)
    private String password;

    @Column(length = 100, nullable = true)
    private String email;

    @Column(length = 50, nullable = true)
    private String phone;

    @Column(length = 10, nullable = true)
    private String gender;

    @Column(length = 200, nullable = true)
    private String introduce;

    @Column(name = "profile_img", columnDefinition = "TEXT", length = 500)
    private String profileImg;

    @Column(name = "social_id", length = 200)
    private String socialId;

    @Column(name = "private_flag", nullable = false, columnDefinition = "TINYINT DEFAULT 0")
    private boolean privateFlag;

    @Column(name = "role", length = 30)
    private String role;

    @Column(name = "created_at", nullable = true)
    private LocalDateTime createdAt;
}
