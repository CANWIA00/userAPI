package org.blaze.userapi.dto.message;

import java.util.UUID;

public class FriendshipMessageDto {
    private UUID friendId;
    private String message;
    private String receiverEmail;
    private String senderEmail;

    public FriendshipMessageDto() {
    }

    public FriendshipMessageDto(UUID friendId, String message, String receiverEmail, String senderEmail) {
        this.friendId = friendId;
        this.message = message;
        this.receiverEmail = receiverEmail;
        this.senderEmail = senderEmail;
    }

    public UUID getFriendId() {
        return friendId;
    }

    public void setFriendId(UUID friendId) {
        this.friendId = friendId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }

    public String getSenderEmail() {
        return senderEmail;
    }

    public void setSenderEmail(String senderEmail) {
        this.senderEmail = senderEmail;
    }
}
