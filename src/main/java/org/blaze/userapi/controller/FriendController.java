package org.blaze.userapi.controller;

import org.blaze.userapi.dto.FriendDto;
import org.blaze.userapi.dto.ProfileDto;
import org.blaze.userapi.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/friend")
public class FriendController {

    private final FriendService friendService;

    public FriendController(FriendService friendService) {
        this.friendService = friendService;
    }


    @PostMapping("/{id}")
    public ResponseEntity<FriendDto> sendFriendRequest(@PathVariable UUID id) {
        return ResponseEntity.ok(friendService.sendFriendRequest(id));
    }

    @PatchMapping("/accept/{id}")
    public ResponseEntity<String> acceptFriendRequest(@PathVariable UUID id) {
        return ResponseEntity.ok(friendService.acceptFriendRequest(id));
    }

    @PatchMapping("/reject/{id}")
    public ResponseEntity<String> rejectFriendRequest(@PathVariable UUID id) {
        return ResponseEntity.ok(friendService.rejectFriendRequest(id));
    }

    @GetMapping("/all")
    public ResponseEntity<List<ProfileDto>> getAllFriend() {
        return ResponseEntity.ok(friendService.getAllFriend());
    }

    @GetMapping("/request")
    public ResponseEntity<List<FriendDto>> getPendingFriend() {
        return ResponseEntity.ok(friendService.getPendingFriendRequests());
    }

}
