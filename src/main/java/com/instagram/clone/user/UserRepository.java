package com.instagram.clone.user;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<UserEntity, String> {
	
	Boolean existsByUserId(String userId);
	Boolean existsByEmail(String email);
	Boolean existsByPhone(String phone);
    UserEntity findByUserId(String userid);
    UserEntity findByEmail(String email);
    UserEntity findByPhone(String phone);
    
    @Query("SELECT u FROM UserEntity u ORDER BY u.createdAt DESC")
    List<UserEntity> findTopNUsers(Pageable pageable);

}
