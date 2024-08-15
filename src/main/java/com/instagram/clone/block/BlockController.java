package com.instagram.clone.block;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/block")
public class BlockController {

    private final BlockService blockService;

    public BlockController(BlockService blockService) {
        this.blockService = blockService;
    }

    @PostMapping("/{toUserId}")
    public ResponseEntity<BlockDTO> blockUser(@PathVariable String toUserId, 
                                              @AuthenticationPrincipal UserDetails currentUser) {
        String fromUserId = currentUser.getUsername();
        BlockDTO blockDTO = blockService.blockUser(fromUserId, toUserId);
        return ResponseEntity.ok(blockDTO);
    }

    @DeleteMapping("/{toUserId}")
    public ResponseEntity<Void> unblockUser(@PathVariable String toUserId, 
                                            @AuthenticationPrincipal UserDetails currentUser) {
        String fromUserId = currentUser.getUsername();
        blockService.unblockUser(fromUserId, toUserId);
        return ResponseEntity.noContent().build();
    }
}
