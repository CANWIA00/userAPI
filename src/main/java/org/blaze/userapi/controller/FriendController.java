package org.blaze.userapi.controller;

import org.blaze.userapi.dto.FriendDto;
import org.blaze.userapi.service.FriendService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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


}
