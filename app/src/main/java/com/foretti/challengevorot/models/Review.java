package com.foretti.challengevorot.models;

public class Review {
    public Integer id;
    public String userId;
    public String gameName;
    public String gamePreview;
    public Integer rating;
    public String text;

    public Review() {
    }

    public Review(Integer id, String userId, String gameName, String gamePreview, Integer rating, String text) {
        this.id = id;
        this.userId = userId;
        this.gameName = gameName;
        this.gamePreview = gamePreview;
        this.rating = rating;
        this.text = text;
    }
}
