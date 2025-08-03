package org.blaze.userapi.controller;

import io.jsonwebtoken.io.IOException;
import org.blaze.userapi.dto.message.ChatProfileInfoDto;
import org.blaze.userapi.dto.message.MessageDto;
import org.blaze.userapi.dto.request.SignalMessageRequest;
import org.blaze.userapi.service.ChatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;


import java.security.Principal;
import java.util.Collections;
import java.util.List;
import java.util.Map;


@Controller
@RequestMapping("/api/v1/chat")
public class ChatController {

    private static final Logger log = LoggerFactory.getLogger(ChatController.class);
    private final ChatService chatService;

    public ChatController(ChatService chatService) {
        this.chatService = chatService;
    }

    @GetMapping("/info/{receiverMail}")
    public ResponseEntity<ChatProfileInfoDto> getCurrentUserProfile(Principal principal, @PathVariable("receiverMail") String mail) {
        return ResponseEntity.ok(chatService.handleInfo(principal,mail));
    }

    @MessageMapping("/chat.sendMessage")
    public void sendMessage(@Payload MessageDto chatMessageRequest, Principal principal) {
        chatService.handleSendMessage(chatMessageRequest,principal);
    }

    @GetMapping("/rooms/{roomId}")
    public ResponseEntity<List<MessageDto>> getAllChatMessages(@PathVariable String roomId, Principal principal) {
        if (!roomId.contains(principal.getName())) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        List<MessageDto> messages = chatService.getAllChatMessages(roomId);
        return ResponseEntity.ok(messages);
    }

    @MessageMapping("/chat/addUser")
    public void addUser(@Payload MessageDto messageDto, StompHeaderAccessor stompHeaderAccessor) {
       chatService.handleAddUser(messageDto,stompHeaderAccessor);
    }

    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> uploadFile(
            @RequestParam("file") MultipartFile file,
            @RequestParam("senderMail") String senderMail,
            @RequestParam("receiverMail") String receiverMail
    ) {
        try {
            Map<String, String> response = chatService.uploadFile(file, senderMail, receiverMail);
            return ResponseEntity.ok(response);
        } catch (IOException e) {
            log.error("❌ File upload failed: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Collections.singletonMap("error", "File upload failed"));
        } catch (java.io.IOException e) {
            throw new RuntimeException(e);
        }
    }

    @MessageMapping("/call")
    public void callUser(@Payload SignalMessageRequest messageDto, Principal principal) {
        log.info("🔒 Principal name: {}", principal.getName());
        log.info("🎯 Message to: {}", messageDto.getTo());
        chatService.callUser(messageDto,principal);
    }


}
