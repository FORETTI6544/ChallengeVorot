package com.example.abchihba.ui;

public class Reviews {
    private String tag;
    private String game;
    private String review;
    private String rating;
    private String preview;
    private String date;


    public Reviews() {
    }
    public Reviews(String tag, String game, String review, String rating, String preview, String date) {
        this.tag = tag;
        this.game = game;
        this.review = review;
        this.rating = rating;
        this.preview = preview;
        this.date = date;
    }

    public String getTag() {
        return tag;
    }

    public String getGame() {
        return game;
    }

    public String getReview() {
        return review;
    }

    public String getRating() {
        return rating;
    }

    public String getPreview() {
        return preview;
    }
    public String getDate() {
        return date;
    }
}
