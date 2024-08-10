package com.instagram.clone.post;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PostImageRepository extends JpaRepository<PostImageEntity, Integer> {
//    List<PostImageEntity> findByPostId(Integer postId);
}
