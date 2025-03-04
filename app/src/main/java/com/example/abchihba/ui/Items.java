package com.example.abchihba.ui;

public class Items {
    private String name;
    private String description;
    private String price;

    // Конструктор
    public Items() {
    }
    public Items(String name, String description, String price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }


    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public String getPrice() {
        return price;
    }

}