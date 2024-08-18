package com.instagram.clone.user;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;


import lombok.RequiredArgsConstructor;

import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
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

}

