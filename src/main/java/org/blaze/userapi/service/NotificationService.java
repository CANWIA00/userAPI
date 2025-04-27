package org.blaze.userapi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blaze.userapi.dto.message.FriendshipDtoMessage;
import org.blaze.userapi.model.Friend;
import org.blaze.userapi.repository.FriendRepository;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;


import java.util.HashMap;
import java.util.Map;


@Service
public class NotificationService {

    private static final Logger log = LogManager.getLogger(NotificationService.class);
    private final SimpMessagingTemplate messagingTemplate;
    private final UserService userService;
    private final ProfileService profileService;
    private final FriendRepository friendRepository;
    private final FriendService friendService;

    public NotificationService(SimpMessagingTemplate messagingTemplate, UserService userService, ProfileService profileService, FriendRepository friendRepository, FriendService friendService) {
        this.messagingTemplate = messagingTemplate;
        this.userService = userService;
        this.profileService = profileService;
        this.friendRepository = friendRepository;
        this.friendService = friendService;
    }

    @RabbitListener(queues = "sendFriendRequestQueue")
    public void handleFriendRequest(FriendshipDtoMessage message) {
        log.info("Listener received friend message: " + message.toString());
        log.info("Message ID: {}", message.getId());
        log.info("Friend ID from message: {}", message.getFriendId());
        log.info("Message content: {}", message.getMessage());

        try {
            Friend friend = friendService.getFriend(message.getFriendId());
            log.info("Listener received friend: " + friend.toString());
            String receiverMail = friend.getReceiver().getUser().getEmail();
            log.info("Listener received friend mail: " + receiverMail);

            Map<String, String> notification = new HashMap<>();
            notification.put("title", "New Friend Request");
            notification.put("body", message.getMessage());

            messagingTemplate.convertAndSendToUser(
                    receiverMail,
                    "/topic/notifications",
                    notification
            );
        } catch (Exception e) {
            log.error("Error processing friend request: {}", e.getMessage(), e);
            throw e;
        }
    }
}
