package org.blaze.userapi.service;

import jakarta.transaction.Transactional;
import org.blaze.userapi.dto.FriendDto;
import org.blaze.userapi.dto.ProfileDto;
import org.blaze.userapi.dto.converter.FriendDtoConverter;
import org.blaze.userapi.dto.message.FriendshipMessageDto;
import org.blaze.userapi.exception.CustomException;
import org.blaze.userapi.model.F_status;
import org.blaze.userapi.model.Friend;
import org.blaze.userapi.model.Profile;
import org.blaze.userapi.repository.FriendRepository;
import org.blaze.userapi.repository.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class FriendService {

    private static final Logger log = LoggerFactory.getLogger(FriendService.class);
    private final FriendRepository friendRepository;
    private final ProfileService profileService;
    private final FriendDtoConverter friendDtoConverter;
    private final RabbitTemplate rabbitTemplate;
    private final DirectExchange directExchange;
    private final UserRepository userRepository;

    public FriendService(FriendRepository friendRepository, ProfileService profileService, FriendDtoConverter friendDtoConverter, RabbitTemplate rabbitTemplate, DirectExchange directExchange, UserRepository userRepository) {
        this.friendRepository = friendRepository;
        this.profileService = profileService;
        this.friendDtoConverter = friendDtoConverter;
        this.rabbitTemplate = rabbitTemplate;
        this.directExchange = directExchange;
        this.userRepository = userRepository;
    }


    @Transactional
    public FriendDto sendFriendRequest(UUID id) {
        Profile sender = profileService.getMyProfile();
        Profile receiver = profileService.getProfileById(id);

        log.info("Sending friend request to " + id);
        if (receiver == null || sender.equals(receiver)) {
            log.info("Receiver null or sender and receiver are same person!");
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

    public List<FriendDto> getPendingFriendRequests() {
        Profile profile = profileService.getMyProfile();
        return friendDtoConverter.convertFrom(Objects.requireNonNull(friendRepository.findByReceiverAndStatus(profile, F_status.PENDING)));
    }

    public List<ProfileDto> getAllFriend() {
        Profile profile = profileService.getMyProfile();
        List<ProfileDto> allFriendsProfileList = new ArrayList<>();
        List<Friend> receiverAcceptedRequests = friendRepository.findByReceiverAndStatus(profile, F_status.ACCEPTED);
        List<Friend> senderAcceptedRequests = friendRepository.findBySenderAndStatus(profile, F_status.ACCEPTED);
        if(receiverAcceptedRequests != null) {
            List<ProfileDto> friendsFromReceiver = receiverAcceptedRequests.stream()
                    .map(friend -> profileService.findProfileById(friend.getSender().getId()))
                    .toList();
            allFriendsProfileList.addAll(friendsFromReceiver);
        }

        if (senderAcceptedRequests != null) {
            List<ProfileDto> friendsFromSender = senderAcceptedRequests.stream()
                    .map(friend -> profileService.findProfileById(friend.getReceiver().getId()))
                    .toList();
            allFriendsProfileList.addAll(friendsFromSender);
        }

        allFriendsProfileList.forEach(System.out::println);

        return allFriendsProfileList;


    }



    public String acceptFriendRequest(UUID id) {
        Friend friend = friendRepository.findById(id)
                .orElseThrow(() -> new CustomException("Friend request not found with id: " + id));

        friend.setStatus(F_status.ACCEPTED);
        friendRepository.save(friend);

        return "Friend request accepted successfully";
    }

    public String rejectFriendRequest(UUID id) {
        Friend friend = friendRepository.findById(id)
                .orElseThrow(() -> new CustomException("Friend request not found with id: " + id));
        friend.setStatus(F_status.REJECTED);
        friendRepository.save(friend);

        return "Friend request rejected successfully";
    }


    protected Friend getFriend(UUID id) {
        return friendRepository.findById(id).orElseThrow(() -> new CustomException("Friendship not found with id : " + id));
    }



}