package org.blaze.userapi.service;

import jakarta.transaction.Transactional;
import org.blaze.userapi.dto.FriendDto;
import org.blaze.userapi.dto.converter.FriendDtoConverter;
import org.blaze.userapi.exception.CustomException;
import org.blaze.userapi.model.F_status;
import org.blaze.userapi.model.Friend;
import org.blaze.userapi.model.Profile;
import org.blaze.userapi.repository.FriendRepository;
import org.blaze.userapi.util.DataMigration;
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



    public FriendDto sendFriendRequest(UUID id) {

        Profile sender = profileService.getMyProfile();
        Profile receiver = profileService.getProfileById(id);

        if( receiver == null || sender.equals(receiver)) {
            throw new CustomException("You have a problem with your sending friend request");
        }

        if(friendRepository.findBySenderAndReceiver(sender, receiver) != null) {
            throw new CustomException("You have already sent a friend request");
        }

        Friend friend = new Friend(
                null,
                sender,
                receiver,
                F_status.PENDING,
                LocalDateTime.now()
        );

        log.info("Created Friend Object", friend);
        friendRepository.save(friend);

        rabbitTemplate.convertAndSend(directExchange.getName(), "firstRoute", friend);

        return friendDtoConverter.convertFrom(friend);
    }
}
