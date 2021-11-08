package com.example.magiccards.cards.data.entities;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Card {
//    @SerializedName("id")
    private Integer id;
    private String title;
    private String description;
    private Integer stars;
    private String addedOn;
    private Boolean rare;
    private String image;
    private Integer postedBy;
    private Double latitude;
    private Double longitude;

    public Card(){}

    public Card(Integer id, String title, String description, Integer stars, String addedOn, Boolean rare, String image, Integer postedBy, Double latitude, Double longitude) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.stars = stars;
        this.addedOn = addedOn;
        this.rare = rare;
        this.image = image;
        this.postedBy = postedBy;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getAddedOn() {
        return addedOn;
    }

    public void setAddedOn(String addedOn) {
        this.addedOn = addedOn;
    }

    public Integer getStars() {
        return stars;
    }

    public void setStars(Integer stars) {
        this.stars = stars;
    }

    public Boolean getRare() {
        return rare;
    }

    public void setRare(Boolean rare) {
        this.rare = rare;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public Integer getPostedBy() {
        return postedBy;
    }

    public void setPostedBy(Integer postedBy) {
        this.postedBy = postedBy;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
}
