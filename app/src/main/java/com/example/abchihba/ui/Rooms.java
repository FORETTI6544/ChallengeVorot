package com.example.abchihba.ui;

import java.util.List;

public class Rooms {
    String name;
    String password;
    List<String> users;

    public Rooms(String name, String password, List<String> users) {
        this.name = name;
        this.password = password;
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
