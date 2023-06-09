package com.example.workday_nasamobileapp.DataAdaptor;

import java.io.Serializable;

public class ItemModel implements Serializable {
    private final String title, imageUrl, description, date;

    public ItemModel(String title, String imageUrl, String description, String date) {
        this.title = title;
        this.imageUrl = imageUrl;
        this.description = description;
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public String getDescription() {
        return description;
    }

    public String getDate() {
        return date;
    }

}
