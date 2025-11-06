package com.example.abchihba;

public class Chat {
    public Chat(String tag, long time, String msg) {
        this.userTag = tag;
        this.time = time;
        this.message = msg;
    }
    private String userTag;
    private long time;
    private String message;

    public String getUserTag() {
        return userTag;
    }

    public String getMessage() {
        return message;
    }
    public long getTime() {
        return time;
    }
}
