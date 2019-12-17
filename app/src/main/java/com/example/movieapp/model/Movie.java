package com.example.movieapp.model;

import com.google.gson.annotations.SerializedName;

public class Movie {

    @SerializedName("id")
    private String id;

    @SerializedName("title")
    private String name;

    @SerializedName("overview")
    private String description;

    @SerializedName("poster_path")
    private String picture = "https://boygeniusreport.files.wordpress.com/2016/12/spider-man-homecoming.jpg?quality=98&strip=all";

    public Movie(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public Movie(String id, String name, String description, String picture) {
        this.id = id;
        this.name = name;
        this.description = description;
        this.picture = picture;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }
}
