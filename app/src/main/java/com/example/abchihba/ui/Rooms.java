package com.example.abchihba.ui;

import java.util.ArrayList;
import java.util.List;

public class Rooms {
    String name;
    String password;
    Boolean status;
    List<String> users;

    public Rooms(){
        this.name = "name";
        this.password = "password";
        this.status = false;
        this.users = new ArrayList<>();
    }
    public Rooms(String name, String password, Boolean status, List<String> users) {
        this.name = name;
        this.password = password;
        this.status = status;
        this.users = users;
    }

    public String getName() {
        return name;
    }

    public String getPassword() {
        return password;
    }

    public List<String> getUsers() {
        return users;
    }
}
