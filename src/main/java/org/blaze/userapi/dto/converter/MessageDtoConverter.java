package org.blaze.userapi.dto.converter;

import org.blaze.userapi.dto.message.MessageDto;
import org.blaze.userapi.model.Message;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class MessageDtoConverter {

    public MessageDto convertFrom(Message message) {
        MessageDto messageDto = new MessageDto();
        messageDto.setType(message.getType());
        messageDto.setSenderMail(message.getSenderMail());
        messageDto.setReceiverMail(message.getReceiverMail());
        messageDto.setContent(message.getContent());
        messageDto.setTimestamp(message.getCreatedAt().toString());

        return messageDto;
    }

    public List<MessageDto> convertFrom( List<Message> messages ) {
        return messages.stream().map(this::convertFrom).collect(Collectors.toList());
    }
}
