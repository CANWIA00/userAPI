package org.blaze.userapi.dto.request;

import org.blaze.userapi.model.MessageType;

public class ChatMessageRequest {

    private MessageType type;
    private String content;
    private String token;
    private String receiverId;
    private String timestamp;

    public ChatMessageRequest() {
    }

    public ChatMessageRequest(MessageType type, String content, String token, String receiverId, String timestamp) {
        this.type = type;
        this.content = content;
        this.token = token;
        this.receiverId = receiverId;
        this.timestamp = timestamp;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
