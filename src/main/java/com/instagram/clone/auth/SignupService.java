package com.instagram.clone.auth;

import java.util.UUID;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.instagram.clone.user.UserEntity;
import com.instagram.clone.user.UserRepository;

@Service
public class SignupService {

    private final UserRepository userRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    
    public SignupService(UserRepository userRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userRepository = userRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }
    
    public void signup(SignupDTO signupDTO) {
        String userId = signupDTO.getUserId();
        String email = signupDTO.getEmail();
        String phone = signupDTO.getPhone();
        String name = signupDTO.getName();
        String password = signupDTO.getPassword();
        
        // Check if either email or phone is provided
        if ((email == null || email.isEmpty()) && (phone == null || phone.isEmpty())) {
            throw new IllegalArgumentException("Either email or phone must be provided");
        }
        
        // Check for email existence if email is provided
        if (email != null && !email.isEmpty() && userRepository.existsByEmail(email)) {
            System.out.println("Email already exists: " + email);
            return;
        }

        // Check for phone existence if phone is provided
        if (phone != null && !phone.isEmpty() && userRepository.existsByPhone(phone)) {
            System.out.println("Phone already exists: " + phone);
            return;
        }

        // Check for userId existence
        if (userRepository.existsByUserId(userId)) {
            System.out.println("UserID already exists: " + userId);
            return;
        }

        // Create a new user entity
        UserEntity data = new UserEntity();
        data.setId(UUID.randomUUID().toString().replace("-", ""));
        data.setUserId(userId);
        data.setEmail(email);
        data.setPhone(phone);
        data.setName(name);
        data.setRole("ROLE_USER");
        data.setPassword(bCryptPasswordEncoder.encode(password));
        
        System.out.println("UserEntity to be saved: " + data.getName());
        userRepository.save(data);
    }
}
