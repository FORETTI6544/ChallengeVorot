package com.foretti.challengevorot.models;

public class ChatMessage {
    public String userId;
    public String userName;
    public String userAvatar;
    public String message;
    public long timestamp;

    public ChatMessage() {
    }

    public ChatMessage(String userId, String userName, String userAvatar, String message, long timestamp) {
        this.userId = userId;
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.message = message;
        this.timestamp = timestamp;
    }
}
