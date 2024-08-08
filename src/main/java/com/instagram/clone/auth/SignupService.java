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
		if (email == null && phone == null) {
			throw new IllegalArgumentException("Either email or phone must be provided");
		}
		Boolean isPhoneExist = phone != null && userRepository.existsByPhone(phone);
		Boolean isEmailExist = email != null && userRepository.existsByEmail(email);
		Boolean isUserIdExist = userRepository.existsByUserId(userId);

		if (isEmailExist || isPhoneExist) {
            if(isEmailExist) {
    			System.out.println("Email already exists: " + email);
                return;
            } else if(isPhoneExist) {
    			System.out.println("Phone already exists: " + phone);
                return;
            }
        } else if (isUserIdExist) {
        	System.out.println("UserID already exists: " + userId);
        	return;
        }

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
