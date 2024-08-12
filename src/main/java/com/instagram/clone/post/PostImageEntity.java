package com.instagram.clone.post;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "post_image")
@Getter
@Setter
public class PostImageEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "post_image_id")
    private Integer postImageId;

    @Column(name = "url", nullable = false, columnDefinition = "TEXT")
    private String url;

    @Column(name = "alt", nullable = false, columnDefinition = "TEXT")
    private String alt;

    @ManyToOne
    @JoinColumn(name = "post_id", nullable = false)
    private PostEntity post; // This should be enough to establish the relationship

}
