package com.instagram.clone.user;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.instagram.clone.auth.CustomUserDetails;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;


    @GetMapping("/profile")
    public UserDTO getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails) {
        return userService.getUserByUserId(customUserDetails.getUsername());
    }

    @GetMapping("/check-email")
    public ResponseEntity<Boolean> checkEmailExists(@RequestParam String email) {
        return ResponseEntity.ok(userService.checkEmailExists(email));
    }

    @GetMapping("/check-phone")
    public ResponseEntity<Boolean> checkPhoneExists(@RequestParam String phone) {
        return ResponseEntity.ok(userService.checkPhoneExists(phone));
    }

    @GetMapping("/check-userid")
    public ResponseEntity<Boolean> checkUserIdExists(@RequestParam String userId) {
        return ResponseEntity.ok(userService.checkUserIdExists(userId));
    }
    
    @GetMapping("/suggestions")
    public ResponseEntity<List<UserDTO>> getUserSuggestions(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                            @RequestParam(defaultValue = "5") int limit) {
        List<UserDTO> suggestedUsers = userService.getUserSuggestions(customUserDetails.getUsername(), limit);
        return ResponseEntity.ok(suggestedUsers);
    }

    // 특정 userId에 해당하는 사용자 정보 반환
    @GetMapping("/profile/{userId}")
    public UserDTO getUserInfoById(@PathVariable String userId) {
        return userService.getUserByUserId(userId); // userId를 기반으로 사용자 정보 가져오기
    }

}