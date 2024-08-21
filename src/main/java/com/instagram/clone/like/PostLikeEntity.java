package com.instagram.clone.like;

import com.instagram.clone.post.PostEntity;
import com.instagram.clone.user.UserEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name="post_like")
@Getter
@Setter
@Entity
public class PostLikeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_like_id")
    private Integer postLikeId;

    @Column(name = "id", nullable = false)
    private String userId;  // userId를 String으로 유지

    @Column(name = "post_id", nullable = false)
    private Integer postId;  // postId를 Integer로 유지
}
