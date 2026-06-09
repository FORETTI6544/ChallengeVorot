package com.foretti.challengevorot.models;

public class User {
    public String name;
    public String avatar;
    public String genre;
    public String game;
    public String gameStatus;
    public String id;
    public String askTo;
    public Boolean readiness;

    public User(){
    }
    public User(String name,
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
}
