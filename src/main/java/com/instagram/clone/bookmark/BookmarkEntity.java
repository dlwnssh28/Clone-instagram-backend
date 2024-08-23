package com.instagram.clone.bookmark;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Table(name="bookmark")
@Getter
@Setter
@Entity
public class BookmarkEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bookmark_id")
    private Integer bookmarkId;

    @Column(name = "id", nullable = false)
    private String userId;  // userId를 String으로 유지

    @Column(name = "post_id", nullable = false)
    private Integer postId;  // postId를 Integer로 유지
}
