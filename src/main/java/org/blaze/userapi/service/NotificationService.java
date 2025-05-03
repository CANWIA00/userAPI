package org.blaze.userapi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blaze.userapi.dto.message.FriendshipMessageDto;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class NotificationService {

    private static final Logger log = LogManager.getLogger(NotificationService.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final FriendService friendService;
    private final UserService userService;
    private final ProfileService profileService;

    public NotificationService(SimpMessagingTemplate messagingTemplate, FriendService friendService, UserService userService, ProfileService profileService) {
        this.messagingTemplate = messagingTemplate;
        this.friendService = friendService;
        this.userService = userService;
        this.profileService = profileService;
    }

    @RabbitListener(queues = "sendFriendRequestQueue")
    public void handleFriendRequest(FriendshipMessageDto message) {
        log.info("Listener received friend message: {}", message);

        log.info("Notification: {}", message.getMessage());
        log.info("Mails: {} / {}", message.getReceiverEmail(), message.getSenderEmail());

        try {
            String receiverEmail = message.getReceiverEmail();
            if (receiverEmail == null || receiverEmail.isBlank()) {
                log.error("Receiver email is null or blank, cannot send WebSocket message.");
                return;
            }

            messagingTemplate.convertAndSendToUser(
                    receiverEmail,
                    "/topic/notifications",
                    message.getMessage()
            );
        } catch (Exception e) {
            log.error("Error while sending WebSocket notification: ", e);
            throw e;
        }
    }


}