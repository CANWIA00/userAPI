package org.blaze.userapi.service;

import org.blaze.userapi.dto.ProfileDto;
import org.blaze.userapi.dto.converter.MessageDtoConverter;
import org.blaze.userapi.dto.message.MessageDto;
import org.blaze.userapi.dto.message.ChatProfileInfoDto;
import org.blaze.userapi.model.Message;
import org.blaze.userapi.model.MessageType;
import org.blaze.userapi.model.Profile;
import org.blaze.userapi.repository.MessageRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.Principal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class ChatService {

    private final SimpMessagingTemplate messagingTemplate;
    private final ProfileService profileService;
    private static final Logger log = LoggerFactory.getLogger(ChatService.class);
    private final MessageRepository messageRepository;
    private final MessageDtoConverter messageDtoConverter;
    private static final String UPLOAD_DIR = "D:/JAVA PROJECTS/userAPI/uploads";



    public ChatService(SimpMessagingTemplate messagingTemplate, ProfileService profileService, MessageRepository messageRepository, MessageDtoConverter messageDtoConverter) {
        this.messagingTemplate = messagingTemplate;
        this.profileService = profileService;
        this.messageRepository = messageRepository;
        this.messageDtoConverter = messageDtoConverter;
    }

    public void handleSendMessage(MessageDto chatMessageRequest, Principal principal) {
        String sender = principal.getName();
        String receiver = chatMessageRequest.getReceiverMail();
        String chatRoomId = Stream.of(sender, receiver).sorted().collect(Collectors.joining("_"));
        try{
            messagingTemplate.convertAndSend("/topic/chat/" + chatRoomId, chatMessageRequest);
        } catch (Exception e) {
            log.error("Handle Send Message Error: {}",e.getMessage());
        }
        Message message = new Message(
                null,
                chatRoomId,
                sender,
                receiver,
                chatMessageRequest.getContent(),
                MessageType.CHAT,
                LocalDateTime.now()
        );
        messageRepository.save(message);
    }

    public List<MessageDto> getAllChatMessages(String roomId) {
        List<Message> messageList = messageRepository.findAllByChatRoomId(roomId);
        return messageDtoConverter.convertFrom(messageList);
    }


    public void handleAddUser(MessageDto messageDto, StompHeaderAccessor stompHeaderAccessor) {
        Objects.requireNonNull(stompHeaderAccessor.getSessionAttributes()).put("userId", messageDto.getSenderId());
    }

    public ChatProfileInfoDto handleInfo(Principal principal, String mail) {
        ProfileDto profileDtoSender = profileService.findMeByPrincipal(principal);
        Profile profileDtoReceiver = profileService.getProfileByUserMail(mail);
        return new ChatProfileInfoDto(
                profileDtoSender.getFullName(),
                profileDtoSender.getUserMail(),
                profileDtoSender.getUserId(),
                Objects.requireNonNull(profileDtoReceiver.getFullName()),
                Objects.requireNonNull(profileDtoReceiver.getUser()).getEmail(),
                Objects.requireNonNull(profileDtoReceiver.getId())
        );
    }

    public Map<String, String> uploadFile(MultipartFile file, String senderMail, String receiverMail) throws IOException {
        log.info("Uploading file {} to {}", file.getOriginalFilename(), senderMail);
        log.info("Uploading file {} to {}", file.getOriginalFilename(), receiverMail);
        File uploadDir = new File(UPLOAD_DIR);
        if (!uploadDir.exists()) {
            uploadDir.mkdirs();
        }

        String extension = getExtension(file.getOriginalFilename());
        String fileName = UUID.randomUUID() + "." + extension;
        Path filePath = Paths.get(UPLOAD_DIR, fileName);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        String fileUrl = "http://localhost:8080/uploads/" + fileName;
        String chatRoomId = Stream.of(senderMail, receiverMail)
                .sorted()
                .reduce((a, b) -> a + "_" + b)
                .orElse("unknown_room");
        Message message = new Message(
                null,
                chatRoomId,
                senderMail,
                receiverMail,
                fileUrl,
                MessageType.FILE,
                LocalDateTime.now()
        );

        messageRepository.save(message);

        return Collections.singletonMap("fileUrl", fileUrl);
    }


    private String getExtension(String originalName) {
        if (originalName == null || !originalName.contains(".")) {
            return "bin";
        }
        return originalName.substring(originalName.lastIndexOf('.') + 1);
    }
}
