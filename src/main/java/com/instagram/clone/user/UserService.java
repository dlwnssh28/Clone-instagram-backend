package com.instagram.clone.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.instagram.clone.follow.FollowRepository;

import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final FollowRepository followRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, FollowRepository followRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.followRepository = followRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public boolean checkPassword(String rawPassword, String encodedPassword) {
        return passwordEncoder.matches(rawPassword, encodedPassword);
    }

    public UserDTO getUserByUserId(String userId) {
        UserEntity user = userRepository.findByUserId(userId);
        if (user != null) {
            return UserMapper.toDto(user);
        } else {
            throw new RuntimeException("User not found");
        }
    }

    public UserDTO getUserById(String id) {
        return userRepository.findById(id)
                .map(UserMapper::toDto)
                .orElseThrow(() -> new RuntimeException("User not found"));
    }

    public UserDTO createUser(UserDTO userDTO) {
        UserEntity user = UserMapper.toEntity(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        UserEntity savedUser = userRepository.save(user);
        return UserMapper.toDto(savedUser);
    }
    
    public boolean checkEmailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean checkPhoneExists(String phone) {
        return userRepository.existsByPhone(phone);
    }

    public boolean checkUserIdExists(String userId) {
        return userRepository.existsByUserId(userId);
    }

    public UserEntity findUserByUserId(String userId) {
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }
        return user;
    }
    
    public String getUserUuidByUserId(String userId) {
        return findUserByUserId(userId).getId(); // UUID 반환
    }

    public List<UserDTO> getUserSuggestions(String currentUserId, int limit) {
        Pageable pageable = PageRequest.of(0, limit);
        UserEntity fromUser = userRepository.findByUserId(currentUserId);

        List<UserEntity> users = userRepository.findTopNUsers(pageable);

        return users.stream()
                .filter(user -> !user.getId().equals(fromUser.getId())) // 로그인 중인 자신을 제외
                .filter(user -> !followRepository.existsByFromUserIdAndToUserId(fromUser.getId(), user.getId())) // 이미 팔로우 중인 사용자 제외
                .map(UserMapper::toDto) // 필터링된 사용자만 DTO로 변환
                .collect(Collectors.toList());
    }

    public List<UserDTO> searchUsers(String query, Pageable pageable) {
        List<UserEntity> users = userRepository.searchByUserIdOrName(query, pageable);
        return users.stream().map(UserMapper::toDto).toList();
    }
}

