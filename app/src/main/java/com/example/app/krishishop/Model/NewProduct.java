package com.example.app.krishishop.Model;

public class NewProduct {

    String name;
    String description;
    String price;
    String type;
    String img_url;
    String rating;

    public NewProduct() {
    }

    public NewProduct(String name, String description, String price, String type, String img_url, String rating) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.type = type;
        this.img_url = img_url;
        this.rating = rating;
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

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getImg_url() {
        return img_url;
    }

    public void setImg_url(String img_url) {
        this.img_url = img_url;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

}
