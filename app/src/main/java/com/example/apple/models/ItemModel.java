package com.example.apple.models;

public class ItemModel {
    private int id;
    private String title;
    private String description;
    private double price;
    private String dateCreated;
    private boolean isFavorite;

    public ItemModel(int id, String title, String description, double price, String dateCreated, boolean isFavorite) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.dateCreated = dateCreated;
        this.isFavorite = isFavorite;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getDateCreated() { return dateCreated; }
    public void setDateCreated(String dateCreated) { this.dateCreated = dateCreated; }

    public boolean isFavorite() { return isFavorite; }
    public void setFavorite(boolean favorite) { isFavorite = favorite; }
}