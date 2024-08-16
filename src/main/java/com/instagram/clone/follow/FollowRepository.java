package com.instagram.clone.follow;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface FollowRepository extends JpaRepository<FollowEntity, Integer> {
	
	// 특정 사용자의 팔로워 수를 카운트
    int countByToUserId(String toUserId); 
    
    // 특정 사용자의 팔로잉 수를 카운트
    int countByFromUserId(String fromUserId); 
    
    // 특정 사용자의 팔로워 목록
    List<FollowEntity> findByToUserId(String toUserId);

    // 특정 사용자의 팔로잉 목록
    List<FollowEntity> findByFromUserId(String fromUserId);

    // 특정 사용자가 특정 사용자를 팔로우하고 있는지 확인
    boolean existsByFromUserIdAndToUserIdAndRequest(String fromUserId, String toUserId, boolean request);

    boolean existsByFromUserIdAndToUserId(String fromUserId, String toUserId);

    boolean existsByFromUserIdAndToUserIdAndRequestFalse(String fromUserId, String toUserId);

    Optional<FollowEntity> findByFromUserIdAndToUserId(String fromUserId, String toUserId);
}
