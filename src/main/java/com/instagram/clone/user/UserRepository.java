package com.instagram.clone.user;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface UserRepository extends JpaRepository<UserEntity, String> {
	
	Boolean existsByUserId(String userId);
	Boolean existsByEmail(String email);
	Boolean existsByPhone(String phone);
    UserEntity findByUserId(String userid);
    UserEntity findByEmail(String email);
    UserEntity findByPhone(String phone);
    Optional<UserEntity> findById(String id);
    
    @Query("SELECT u FROM UserEntity u ORDER BY u.createdAt DESC")
    List<UserEntity> findTopNUsers(Pageable pageable);

    @Query("SELECT u FROM UserEntity u WHERE u.userId LIKE %:query% OR u.name LIKE %:query%")
    List<UserEntity> searchByUserIdOrName(@Param("query") String query, Pageable pageable);

}
