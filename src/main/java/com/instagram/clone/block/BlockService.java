package com.instagram.clone.block;

import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.instagram.clone.follow.FollowEntity;
import com.instagram.clone.follow.FollowRepository;
import com.instagram.clone.user.UserEntity;
import com.instagram.clone.user.UserRepository;
import com.instagram.clone.user.UserService;

@Service
public class BlockService {

    private final BlockRepository blockRepository;
    private final FollowRepository followRepository;
    private final UserService userService;

    public BlockService(BlockRepository blockRepository, UserRepository userRepository, UserService userService, FollowRepository followRepository) {
        this.blockRepository = blockRepository;
        this.followRepository = followRepository;
        this.userService = userService;
    }

    @Transactional
    public BlockDTO blockUser(String fromUserId, String toUserId) {
        UserEntity fromUser = userService.findUserByUserId(fromUserId);
        UserEntity toUser = userService.findUserByUserId(toUserId);
        
        // 이미 차단한 사용자인지 확인
        if (blockRepository.existsByFromUserIdAndToUserId(fromUser.getId(), toUser.getId())) {
            throw new RuntimeException("User already blocked");
        }

        // 기존 팔로우 관계 제거 (fromUser가 toUser를 팔로우하는 경우)
        Optional<FollowEntity> followFromTo = followRepository.findByFromUserIdAndToUserId(fromUser.getId(), toUser.getId());
        followFromTo.ifPresent(followRepository::delete);

        // 기존 팔로워 관계 제거 (toUser가 fromUser를 팔로우하는 경우)
        Optional<FollowEntity> followToFrom = followRepository.findByFromUserIdAndToUserId(toUser.getId(), fromUser.getId());
        followToFrom.ifPresent(followRepository::delete);

        // 차단 엔티티 생성 및 저장
        BlockEntity blockEntity = new BlockEntity();
        blockEntity.setFromUser(fromUser);
        blockEntity.setToUser(toUser);

        blockRepository.save(blockEntity);

        return BlockMapper.toDto(blockEntity);
    }
    
    @Transactional
    public void unblockUser(String fromUserId, String toUserId) {
        UserEntity fromUser = userService.findUserByUserId(fromUserId);
        UserEntity toUser = userService.findUserByUserId(toUserId);

        // 차단 해제할 관계를 찾습니다.
        BlockEntity blockEntity = blockRepository.findByFromUserIdAndToUserId(fromUser.getId(), toUser.getId())
            .orElseThrow(() -> new RuntimeException("Block relationship not found"));

        // 차단 관계를 삭제합니다.
        blockRepository.delete(blockEntity);
    }

    @Transactional(readOnly = true)
    public boolean isBlocked(String fromUserId, String toUserId) {
        return blockRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId);
    }
}
