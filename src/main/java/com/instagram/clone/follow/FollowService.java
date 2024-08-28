package com.instagram.clone.follow;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.instagram.clone.block.BlockService;
import com.instagram.clone.user.UserEntity;
import com.instagram.clone.user.UserRepository;

@Service
public class FollowService {

    private final FollowRepository followRepository;
    private final UserRepository userRepository;
    private final BlockService blockService;

    public FollowService(FollowRepository followRepository, UserRepository userRepository,  BlockService blockService) {
        this.followRepository = followRepository;
        this.userRepository = userRepository;
        this.blockService = blockService;
    }

    @Transactional
    public FollowDTO followUser(String fromUserId, String toUserId) {
        UserEntity fromUser = userRepository.findByUserId(fromUserId);
        if (fromUser == null) {
            throw new RuntimeException("User not found");
        }

        UserEntity toUser = userRepository.findByUserId(toUserId);
        if (toUser == null) {
            throw new RuntimeException("User not found");
        }
        
        // 이미 팔로우 관계가 있는지 확인
        if (followRepository.existsByFromUserIdAndToUserId(fromUser.getId(), toUser.getId())) {
            throw new RuntimeException("Already following this user");
        }
        
        // 차단 여부 확인
        if (blockService.isBlocked(toUserId, fromUserId)) {
            throw new RuntimeException("You are blocked by this user");
        }


        boolean request = !toUser.isPrivateFlag(); // 비공개 계정일 경우 요청 상태로 설정

        FollowEntity followEntity = new FollowEntity();
        followEntity.setFromUser(fromUser);
        followEntity.setToUser(toUser);
        followEntity.setRequest(request);

        followRepository.save(followEntity);

        return FollowMapper.toDto(followEntity);
    }

    @Transactional(readOnly = true)
    public int getFollowerCount(String userId) {
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return followRepository.countByToUserId(user.getId());
    }

    @Transactional(readOnly = true)
    public int getFollowingCount(String userId) {
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        return followRepository.countByFromUserId(user.getId());
    }

    @Transactional(readOnly = true)
    public List<FollowDTO> getFollowers(String userId, String currentUserId) {
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (user.isPrivateFlag() && !isMutualFollow(userId, currentUserId)) {
            throw new AccessDeniedException("Cannot access followers of a private account");
        }

        List<FollowEntity> followers = followRepository.findByToUserId(user.getId());
        return followers.stream().map(FollowMapper::toDto).collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<FollowDTO> getFollowing(String userId, String currentUserId) {
        UserEntity user = userRepository.findByUserId(userId);
        if (user == null) {
            throw new RuntimeException("User not found");
        }

        if (user.isPrivateFlag() && !isMutualFollow(userId, currentUserId)) {
            throw new AccessDeniedException("Cannot access following of a private account");
        }

        List<FollowEntity> following = followRepository.findByFromUserId(user.getId());
        return following.stream().map(FollowMapper::toDto).collect(Collectors.toList());
    }
    
    @Transactional
    public FollowDTO acceptFollowRequest(Integer followId, String currentUserId) {
        // 해당 팔로우 요청을 찾습니다.
        FollowEntity followEntity = followRepository.findById(followId)
            .orElseThrow(() -> new RuntimeException("Follow request not found"));

        // 현재 사용자가 요청된 사용자와 일치하는지 확인합니다.
        if (!followEntity.getToUser().getUserId().equals(currentUserId)) {
            throw new RuntimeException("Unauthorized action");
        }

        // 요청을 수락 상태로 변경합니다.
        followEntity.setRequest(true);
        followRepository.save(followEntity);

        // 맞팔로우 상태를 설정하기 위해 followUser 메서드를 호출합니다.
        FollowDTO mutualFollowDTO = followUser(currentUserId, followEntity.getFromUser().getUserId());

        return FollowMapper.toDto(followEntity); // 요청을 수락한 상태로 변환된 엔티티를 반환합니다.
    }
    
    // 팔로우 요청 거부 기능
    @Transactional
    public void rejectFollowRequest(Integer followId, String currentUserId) {
        FollowEntity followEntity = followRepository.findById(followId)
            .orElseThrow(() -> new RuntimeException("Follow request not found"));

        if (!followEntity.getToUser().getUserId().equals(currentUserId)) {
            throw new RuntimeException("Unauthorized action");
        }

        followRepository.delete(followEntity);
    }
    
    @Transactional
    public void unfollowUser(String fromUserId, String toUserId) {
        UserEntity fromUser = userRepository.findByUserId(fromUserId);
        if (fromUser == null) {
            throw new RuntimeException("User not found with userId: " + fromUserId);
        }

        UserEntity toUser = userRepository.findByUserId(toUserId);
        if (toUser == null) {
            throw new RuntimeException("User not found with userId: " + toUserId);
        }

        // 각 id를 사용해 FollowEntity를 조회
        FollowEntity followEntity = followRepository.findByFromUserIdAndToUserId(fromUser.getId(), toUser.getId())
                .orElseThrow(() -> new RuntimeException("Follow relationship not found"));

        followRepository.delete(followEntity);
    }


    // 팔로우 상태 확인 기능
    @Transactional(readOnly = true)
    public FollowStatusDTO getFollowStatus(String fromUserId, String toUserId) {;
        boolean isFollowing = followRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId);
        boolean hasRequested = followRepository.existsByFromUserIdAndToUserIdAndRequestFalse(fromUserId, toUserId);

        FollowStatusDTO followStatus = new FollowStatusDTO();
        followStatus.setFollowing(isFollowing);
        followStatus.setRequested(hasRequested);

        return followStatus;
    }
    
    // 맞팔로우 상태 확인
    @Transactional(readOnly = true)
    public boolean isMutualFollow(String userId1, String userId2) {
        boolean user1FollowsUser2 = followRepository.existsByFromUserIdAndToUserId(userId1, userId2);
        boolean user2FollowsUser1 = followRepository.existsByFromUserIdAndToUserId(userId2, userId1);

        return user1FollowsUser2 && user2FollowsUser1;
    }

}
