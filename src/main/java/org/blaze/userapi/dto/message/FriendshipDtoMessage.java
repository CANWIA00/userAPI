package org.blaze.userapi.dto.message;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.io.Serializable;
import java.util.UUID;

public class FriendshipDtoMessage implements Serializable {
    @JsonProperty("id")
    private UUID id = UUID.randomUUID();
    
    @JsonProperty("friendId")
    private UUID friendId;
    
    @JsonProperty("message")
    private String message;

    public void setId(UUID id) {
        this.id = id;
    }

    public void setFriendId(UUID friendId) {
        if (friendId == null) {
            throw new IllegalArgumentException("Friend ID must not be null");
        }
        this.friendId = friendId;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public UUID getId() {
        return id;
    }

    public UUID getFriendId() {
        if (friendId == null) {
            throw new IllegalStateException("Friend ID must not be null");
        }
        return friendId;
    }

    public String getMessage() {
        return message;
    }

    public FriendshipDtoMessage(UUID id, UUID friendId, String message) {
        if (friendId == null) {
            throw new IllegalArgumentException("Friend ID must not be null");
        }
        this.id = id;
        this.friendId = friendId;
        this.message = message;
    }

    public FriendshipDtoMessage() {
        // Default constructor for deserialization
    }

    @Override
    public String toString() {
        return "FriendshipDtoMessage{" +
                "id=" + id +
                ", friendId=" + friendId +
                ", message='" + message + '\'' +
                '}';
    }
}
