package org.blaze.userapi.service;

import org.blaze.userapi.model.Friend;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "sendFriendRequest")
    public void handleFriendRequest(Friend friend) {
        String message = String.format("You have a new friend request from %s", 
            friend.getSender().getUser().getUsername());
        
        // Send to the receiver's notification channel
        messagingTemplate.convertAndSend(
            "/topic/notifications/" + friend.getReceiver().getUser().getId(), 
            message
        );
    }
}
