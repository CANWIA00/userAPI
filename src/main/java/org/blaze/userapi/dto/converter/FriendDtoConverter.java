package org.blaze.userapi.dto.converter;

import org.blaze.userapi.dto.FriendDto;
import org.blaze.userapi.model.Friend;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class FriendDtoConverter {

    public FriendDto convertFrom(Friend friend) {
        return new FriendDto(
                friend.getId(),
                friend.getSender(),
                friend.getReceiver(),
                friend.getStatus(),
                friend.getCreatedAt()
        );
    }

    public List<FriendDto> convertFrom(List<Friend> friends) {
        return friends.stream().map(this::convertFrom).collect(Collectors.toList());
    }
}
