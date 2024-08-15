package com.instagram.clone.block;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface BlockRepository extends JpaRepository<BlockEntity, Integer> {
    Optional<BlockEntity> findByFromUserIdAndToUserId(String fromUserId, String toUserId);
    
    boolean existsByFromUserIdAndToUserId(String fromUserId, String toUserId);
}
