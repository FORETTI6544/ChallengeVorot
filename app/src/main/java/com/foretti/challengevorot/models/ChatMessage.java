package com.foretti.challengevorot.models;

import java.time.Instant;

public class ChatMessage {
    public Long id;
    public String room_name;
    public String userId;
    public String type;
    public String content;
    public String attachment_base64;
    public Instant created_at;
}
