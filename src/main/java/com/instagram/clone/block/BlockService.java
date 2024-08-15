package com.instagram.clone.block;

import com.instagram.clone.user.UserEntity;
import com.instagram.clone.user.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BlockService {

    private final BlockRepository blockRepository;
    private final UserRepository userRepository;

    public BlockService(BlockRepository blockRepository, UserRepository userRepository) {
        this.blockRepository = blockRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void blockUser(String fromUserId, String toUserId) {
        UserEntity fromUser = userRepository.findByUserId(fromUserId);
        UserEntity toUser = userRepository.findByUserId(toUserId);

        if (fromUser == null || toUser == null) {
            throw new RuntimeException("User not found");
        }

        if (blockRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId)) {
            throw new RuntimeException("User is already blocked");
        }

        BlockEntity blockEntity = new BlockEntity();
        blockEntity.setFromUser(fromUser);
        blockEntity.setToUser(toUser);

        blockRepository.save(blockEntity);
    }

    @Transactional
    public void unblockUser(String fromUserId, String toUserId) {
        BlockEntity blockEntity = blockRepository.findByFromUserIdAndToUserId(fromUserId, toUserId)
            .orElseThrow(() -> new RuntimeException("Block relationship not found"));

        blockRepository.delete(blockEntity);
    }

    @Transactional(readOnly = true)
    public boolean isBlocked(String fromUserId, String toUserId) {
        return blockRepository.existsByFromUserIdAndToUserId(fromUserId, toUserId);
    }
}
