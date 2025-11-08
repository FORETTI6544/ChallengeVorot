package com.example.abchihba.ui;

public class Users {
    private String name;
    private String avatar;
    private String genre;
    private String game;
    private String preview;
    private String status;
    private String tag;
    private String to;
    private Boolean readiness;
    private long time;

    public Users(){
    }
    public Users(String name,
                 String avatar,
                 String genre,
                 String game,
                 String preview,
                 String status,
                 String tag,
                 String to,
                 Boolean readiness,
                 long time){
        this.name = name;
        this.avatar = avatar;
        this.genre = genre;
        this.game = game;
        this.preview = preview;
        this.status = status;
        this.tag = tag;
        this.to = to;
        this.readiness = readiness;
        this.time = time;
    }

    public String getName() {
        return name;
    }
    public String getAvatar() {
        return avatar;
    }
    public String getGame() {
        return game;
    }
    public String getPreview() {
        return preview;
    }
    public String getStatus(){
        return status;
    }
    public String getTag() {
        return tag;
    }
    public String getTo() {
        return to;
    }
    public String getGenre() {
        return genre;
    }
    public Boolean getReadiness() {
        return readiness;
    }
    public long getTime() {
        return time;
    }
}
