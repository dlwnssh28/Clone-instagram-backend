package com.instagram.clone.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.instagram.clone.user.UserEntity;
import com.instagram.clone.user.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String login) throws UsernameNotFoundException {
        UserEntity user = userRepository.findByUserId(login);
        
        if (user == null) {
            user = userRepository.findByEmail(login);
        }

        if (user == null) {
            user = userRepository.findByPhone(login);
        }

        if (user == null) {
            throw new UsernameNotFoundException("User not found with userId, email or phone: " + login);
        }

        return new CustomUserDetails(user);
    }
}
