package com.example.abchihba;

public class Chat {
    public Chat(String tag, String msg) {
        this.userTag = tag;
        this.message = msg;
    }
    private String userTag;
    private String message;

    public String getUserTag() {
        return userTag;
    }

    public String getMessage() {
        return message;
    }
}
