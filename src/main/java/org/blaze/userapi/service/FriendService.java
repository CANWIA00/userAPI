package org.blaze.userapi.service;

import jakarta.transaction.Transactional;
import org.blaze.userapi.dto.FriendDto;
import org.blaze.userapi.dto.converter.FriendDtoConverter;
import org.blaze.userapi.dto.message.FriendshipDtoMessage;
import org.blaze.userapi.exception.CustomException;
import org.blaze.userapi.model.F_status;
import org.blaze.userapi.model.Friend;
import org.blaze.userapi.model.Profile;
import org.blaze.userapi.repository.FriendRepository;
import org.springframework.amqp.core.AmqpTemplate;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class FriendService {

    private static final Logger log = LoggerFactory.getLogger(FriendService.class);
    private final FriendRepository friendRepository;
    private final ProfileService profileService;
    private final FriendDtoConverter friendDtoConverter;
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;
    private final AmqpTemplate amqpTemplate;

    public FriendService(FriendRepository friendRepository, ProfileService profileService, FriendDtoConverter friendDtoConverter, RabbitTemplate rabbitTemplate, DirectExchange directExchange, AmqpTemplate amqpTemplate) {
        this.friendRepository = friendRepository;
        this.profileService = profileService;
        this.friendDtoConverter = friendDtoConverter;
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
        this.amqpTemplate = amqpTemplate;
    }


    @Transactional
    public FriendDto sendFriendRequest(UUID id) {
        Profile sender = profileService.getMyProfile();
        Profile receiver = profileService.getProfileById(id);

        if (receiver == null || sender.equals(receiver)) {
            throw new CustomException("Invalid friend request");
        }

        if (friendRepository.findBySenderAndReceiver(sender, receiver) != null) {
            throw new CustomException("Friend request already sent");
        }

        Friend friend = new Friend(
                null,
                sender,
                receiver,
                F_status.PENDING,
                LocalDateTime.now()
        );

       Friend friendShip =  friendRepository.save(friend);
        FriendDto friendDto = friendDtoConverter.convertFrom(friend);

        log.info("*** Sender **** : " + sender.getId() + " / Receiver **** : " + receiver.getId());
        log.info("Attempting to send friend request message to RabbitMQ...");
        log.info("Saved friend ID: {}", friendShip.getId());

        FriendshipDtoMessage friendDtoMessage = new FriendshipDtoMessage();
        friendDtoMessage.setMessage("You have new friend request");
        friendDtoMessage.setFriendId(friendShip.getId());
        
        log.info("Created message with ID: {}", friendDtoMessage.getId());
        log.info("Set friendId in message: {}", friendDtoMessage.getFriendId());
        log.info("Full message content: {}", friendDtoMessage);

        try {
            rabbitTemplate.convertAndSend(directExchange.getName(), "friendship.request", friendDtoMessage);
            log.info("Successfully sent message to RabbitMQ");
        } catch (Exception e) {
            log.error("Failed to send friend request message to RabbitMQ: {}", e.getMessage(), e);
            throw e;
        }

        return friendDto;
    }


    protected Friend getFriend(UUID id) {
        if (id == null) {
            throw new CustomException("Friend ID must not be null");
        }
        return friendRepository.findById(id).orElseThrow(() -> new CustomException("Friendship not found with id : " + id));
    }

}
