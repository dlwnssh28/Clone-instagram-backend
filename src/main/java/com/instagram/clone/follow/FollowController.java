package com.instagram.clone.follow;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/follow")
public class FollowController {

    private final FollowService followService;

    public FollowController(FollowService followService) {
        this.followService = followService;
    }

    @PostMapping("/{toUserId}")
    public ResponseEntity<FollowDTO> followUser(@PathVariable String toUserId, 
                                                @AuthenticationPrincipal UserDetails currentUser) {
        // 현재 인증된 사용자의 ID를 가져옴
        String fromUserId = currentUser.getUsername();

        FollowDTO followDTO = followService.followUser(fromUserId, toUserId);
        return ResponseEntity.ok(followDTO);
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Integer> getFollowerCount(@PathVariable String userId) {
        int followerCount = followService.getFollowerCount(userId);
        return ResponseEntity.ok(followerCount);
    }

    @GetMapping("/{userId}/following/count")
    public ResponseEntity<Integer> getFollowingCount(@PathVariable String userId) {
        int followingCount = followService.getFollowingCount(userId);
        return ResponseEntity.ok(followingCount);
    }

    @GetMapping("/{userId}/followers")
    public ResponseEntity<List<FollowDTO>> getFollowers(@PathVariable String userId, @AuthenticationPrincipal UserDetails currentUser) {
        List<FollowDTO> followers = followService.getFollowers(userId, currentUser.getUsername());
        return ResponseEntity.ok(followers);
    }

    @GetMapping("/{userId}/following")
    public ResponseEntity<List<FollowDTO>> getFollowing(@PathVariable String userId, @AuthenticationPrincipal UserDetails currentUser) {
        List<FollowDTO> following = followService.getFollowing(userId, currentUser.getUsername());
        return ResponseEntity.ok(following);
    }

    @PostMapping("/accept/{followId}")
    public ResponseEntity<FollowDTO> acceptFollowRequest(@PathVariable Integer followId, 
                                                         @AuthenticationPrincipal UserDetails currentUser) {
        String userId = currentUser.getUsername();
        FollowDTO followDTO = followService.acceptFollowRequest(followId, userId);
        return ResponseEntity.ok(followDTO);
    }
    
    @PostMapping("/reject/{followId}")
    public ResponseEntity<Void> rejectFollowRequest(@PathVariable Integer followId, 
                                                    @AuthenticationPrincipal UserDetails currentUser) {
        String userId = currentUser.getUsername();
        followService.rejectFollowRequest(followId, userId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/unfollow/{toUserId}")
    public ResponseEntity<Void> unfollowUser(@PathVariable String toUserId, 
                                             @AuthenticationPrincipal UserDetails currentUser) {
        String fromUserId = currentUser.getUsername();
        followService.unfollowUser(fromUserId, toUserId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/block/{toUserId}")
    public ResponseEntity<Void> blockUser(@PathVariable String toUserId, 
                                          @AuthenticationPrincipal UserDetails currentUser) {
        String fromUserId = currentUser.getUsername();
        followService.blockUser(fromUserId, toUserId);
        return ResponseEntity.noContent().build();
    }
    
    @PostMapping("/unblock/{toUserId}")
    public ResponseEntity<Void> unblockUser(@PathVariable String toUserId, @AuthenticationPrincipal UserDetails currentUser) {
        String fromUserId = currentUser.getUsername();
        followService.unblockUser(fromUserId, toUserId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/status/{toUserId}")
    public ResponseEntity<FollowStatusDTO> getFollowStatus(@PathVariable String toUserId, 
                                                           @AuthenticationPrincipal UserDetails currentUser) {
        String fromUserId = currentUser.getUsername();
        FollowStatusDTO followStatus = followService.getFollowStatus(fromUserId, toUserId);
        return ResponseEntity.ok(followStatus);
    }

}
