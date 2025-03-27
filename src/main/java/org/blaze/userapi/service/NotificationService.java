package org.blaze.userapi.service;

import org.blaze.userapi.model.Friend;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService( SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "sendFriendRequest")
    public void handleFriendRequest(Friend friend) {

        String message = "You have a new friend request from" + friend.getSender();

        messagingTemplate.convertAndSend("/topic/notifications/" + friend.getSender(), message );

        

    }


}
