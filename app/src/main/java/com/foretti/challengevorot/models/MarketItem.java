package com.foretti.challengevorot.models;

public class MarketItem {
    public String name;
    public String description;
    public Integer price;

    public MarketItem() {
    }

    public MarketItem(String name, String description, Integer price) {
        this.name = name;
        this.description = description;
        this.price = price;
    }
}
