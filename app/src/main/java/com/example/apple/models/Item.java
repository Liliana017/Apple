package com.example.apple.models;

public class Item {
    private final int id;
    private final int userId;
    private String title;
    private String description;
    private double price;
    private boolean isFavorite;
    private final String dateCreated;

    public Item(int id, int userId, String title, String description, double price, boolean isFavorite, String dateCreated) {
        this.id = id;
        this.userId = userId;
        this.title = title;
        this.description = description;
        this.price = price;
        this.isFavorite = isFavorite;
        this.dateCreated = dateCreated;
    }

    public int getId() {
        return id;
    }


    public int getUserId() {
        return userId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public double getPrice() {
        return price;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }
}