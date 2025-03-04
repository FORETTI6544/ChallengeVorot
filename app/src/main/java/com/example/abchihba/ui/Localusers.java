package com.example.abchihba.ui;

public class Localusers {
    private String tag;
    private String to;
    public Localusers(String tag, String to){
        this.tag = tag;
        this.to = to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getTo() {
        return to;
    }

    public String getTag() {
        return tag;
    }
}
