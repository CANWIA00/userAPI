package org.blaze.userapi.dto.message;


import org.blaze.userapi.model.MessageType;

public class MessageDto {


    public String getReceiverMail() {
        return receiverMail;
    }

    public void setReceiverMail(String receiverMail) {
        this.receiverMail = receiverMail;
    }


    private MessageType type;
    private String content;
    private String senderId;
    private String senderMail;
    private String receiverMail;
    private String timestamp;

    public MessageDto() {
    }

    public MessageDto(MessageType type, String content, String senderId, String receiverMail, String timestamp, String senderName) {
        this.type = type;
        this.content = content;
        this.senderId = senderId;
        this.senderMail = senderName;
        this.receiverMail = receiverMail;
        this.timestamp = timestamp;
    }

    public String getSenderMail() {
        return senderMail;
    }

    public void setSenderMail(String senderMail) {
        this.senderMail = senderMail;
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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }



    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
