package com.foretti.challengevorot.models;

public class Review {
    public String userName;
    public String userAvatar;
    public String gameName;
    public String gamePreview;
    public int rating;
    public String text;

    public Review() {
    }

    public Review(String userName, String userAvatar, String gameName, String gamePreview, int rating, String text) {
        this.userName = userName;
        this.userAvatar = userAvatar;
        this.gameName = gameName;
        this.gamePreview = gamePreview;
        this.rating = rating;
        this.text = text;
    }
}
