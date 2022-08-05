package com.mezdah.mystore.model;

import java.io.Serializable;

public class Product implements Serializable {
    String name,description,image,category;
    String productID;
    int price;

    public Product() {
    }

    public Product(String name, String description, String image, String category, String productID, int price) {
        this.name = name;
        this.description = description;
        this.image = image;
        this.category = category;
        this.productID = productID;
        this.price = price;
    }

    public String getProductID() {
        return productID;
    }

    public void setProductID(String productID) {
        this.productID = productID;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }
}
