package org.blaze.userapi.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.blaze.userapi.model.Friend;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    private static final Logger log = LogManager.getLogger(NotificationService.class);
    private final SimpMessagingTemplate messagingTemplate;

    public NotificationService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    @RabbitListener(queues = "firstQueue")
    public void handleFriendRequest(@Payload Friend friend) {

        log.info("Received friend request from: " + friend.getSender().getUser().getUsername());


    }
}
