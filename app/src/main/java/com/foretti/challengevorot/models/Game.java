package com.foretti.challengevorot.models;

public class Game {
    public String game_name;
    public String appid;
    public String preview_image;

    public Game() {
    }

    public Game(String gameName, String appid) {
        this.game_name = gameName;
        this.appid = appid;
    }

    public Game(String gameName, String appid, String previewImage) {
        this.game_name = gameName;
        this.appid = appid;
        this.preview_image = previewImage;
    }
}
