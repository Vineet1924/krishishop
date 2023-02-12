package com.example.app.krishishop.Model;

public class SellHistoryModel {

    String productImage;
    String productName;
    String productPrice;
    String sellDate;

    public SellHistoryModel() {
    }

    public SellHistoryModel(String productImage, String productName, String productPrice, String sellDate) {
        this.productImage = productImage;
        this.productName = productName;
        this.productPrice = productPrice;
        this.sellDate = sellDate;
    }

    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }

    public String getSellDate() {
        return sellDate;
    }

    public void setSellDate(String sellDate) {
        this.sellDate = sellDate;
    }
}
