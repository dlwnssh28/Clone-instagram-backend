package com.instagram.clone.user;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<UserEntity, String> {
	
	Boolean existsByUserId(String userId);
	Boolean existsByEmail(String email);
	Boolean existsByPhone(String phone);
    UserEntity findByUserId(String userid);
    UserEntity findByEmail(String email);
    UserEntity findByPhone(String phone);
}
