package org.blaze.userapi.service;

import jakarta.transaction.Transactional;
import org.blaze.userapi.dto.FriendDto;
import org.blaze.userapi.dto.converter.FriendDtoConverter;
import org.blaze.userapi.dto.message.FriendshipMessageDto;
import org.blaze.userapi.exception.CustomException;
import org.blaze.userapi.model.F_status;
import org.blaze.userapi.model.Friend;
import org.blaze.userapi.model.Profile;
import org.blaze.userapi.repository.FriendRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Service
public class FriendService {

    private static final Logger log = LoggerFactory.getLogger(FriendService.class);
    private final FriendRepository friendRepository;
    private final ProfileService profileService;
    private final FriendDtoConverter friendDtoConverter;
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;

    public FriendService(FriendRepository friendRepository, ProfileService profileService, FriendDtoConverter friendDtoConverter, RabbitTemplate rabbitTemplate, DirectExchange directExchange) {
        this.friendRepository = friendRepository;
        this.profileService = profileService;
        this.friendDtoConverter = friendDtoConverter;
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
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

        FriendshipMessageDto messageDto = new FriendshipMessageDto();
        messageDto.setFriendId(friendShip.getId());
        messageDto.setReceiverEmail(Objects.requireNonNull(Objects.requireNonNull(friendShip.getReceiver()).getUser()).getEmail());
        messageDto.setSenderEmail(Objects.requireNonNull(Objects.requireNonNull(friendShip.getSender()).getUser()).getEmail());
        messageDto.setMessage("You received a friend request from : " + friendShip.getSender().getUser().getEmail() );


        try {
            rabbitTemplate.convertAndSend(directExchange.getName(), "friendship.request", messageDto);
            log.info("Successfully sent message to RabbitMQ");
        } catch (Exception e) {
            log.error("Failed to send friend request message to RabbitMQ: {}", e.getMessage(), e);
            throw e;
        }

        return friendDto;
    }


    protected Friend getFriend(UUID id) {
        return friendRepository.findById(id).orElseThrow(() -> new CustomException("Friendship not found with id : " + id));
    }

}