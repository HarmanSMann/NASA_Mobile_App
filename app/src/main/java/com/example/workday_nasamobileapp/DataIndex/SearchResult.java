package com.example.workday_nasamobileapp.DataIndex;

public class SearchResult {
    private String title;
    private String imageUrl;

    public SearchResult(String title, String imageUrl) {
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

