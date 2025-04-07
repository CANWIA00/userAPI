package org.blaze.userapi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blaze.userapi.dto.FriendDto;
import org.blaze.userapi.model.Friend;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.amqp.core.Message;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.core.JsonProcessingException;
import java.util.Map;

@Service
public class NotificationService {

    private static final Logger log = LogManager.getLogger(NotificationService.class);
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "sendFriendRequestQueue")
    public void handleFriendRequest(FriendDto friend) {
        log.info("Received friend request for ID: {}", friend.getReceiver().getId());

        String notification =  friend.getSender().getId() + " want sent you a friend request";
        String receiverId = String.valueOf(friend.getReceiver().getId());
        messagingTemplate.convertAndSendToUser(receiverId, "/notification", notification);

    }
}
