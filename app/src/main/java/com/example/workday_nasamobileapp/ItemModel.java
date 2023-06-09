package com.example.workday_nasamobileapp;

public class ItemModel {
    private String title, imageUrl;

    public ItemModel(String title, String imageUrl) {
        this.title = title;
        this.imageUrl = imageUrl;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

}
