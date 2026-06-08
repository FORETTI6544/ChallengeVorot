package com.foretti.challengevorot.models;

public class Users {
    private String name;
    private String avatar;
    private String genre;
    private String game;
    private String gameStatus;
    private String id;
    private String askTo;
    private Boolean readiness;

    public Users(){
    }
    public Users(String name,
                 String avatar,
                 String genre,
                 String game,
                 String gameStatus,
                 String id,
                 String askTo,
                 Boolean readiness){
        this.name = name;
        this.avatar = avatar;
        this.genre = genre;
        this.game = game;
        this.gameStatus = gameStatus;
        this.id = id;
        this.askTo = askTo;
        this.readiness = readiness;
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
    public String getGameStatus(){
        return gameStatus;
    }
    public String getId() {
        return id;
    }
    public String getAskTo() {
        return askTo;
    }
    public String getGenre() {
        return genre;
    }
    public Boolean getReadiness() {
        return readiness;
    }
}
