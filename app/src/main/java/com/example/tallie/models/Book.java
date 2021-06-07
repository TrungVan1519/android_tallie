package com.example.tallie.models;

import java.io.Serializable;

public class Book implements Serializable {
    private int picture;
    private String name, description, authorName;
    private double price;
    private int qty;

    public Book(int picture, String name, String description, String authorName, double price, int qty) {
        this.picture = picture;
        this.name = name;
        this.description = description;
        this.authorName = authorName;
        this.price = price;
        this.qty = qty;
    }

    public int getPicture() {
        return picture;
    }

    public void setPicture(int picture) {
        this.picture = picture;
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

    public String getAuthorName() {
        return authorName;
    }

    public void setAuthorName(String authorName) {
        this.authorName = authorName;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQty() {
        return qty;
    }

    public void setQty(int qty) {
        this.qty = qty;
    }
}
