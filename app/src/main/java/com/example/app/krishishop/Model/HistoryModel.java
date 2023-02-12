package com.example.app.krishishop.Model;

public class HistoryModel {

    String productName;
    String productQuantity;
    String purchaseDate;

    public HistoryModel() {
    }

    public HistoryModel(String productName, String productQuantity, String purchaseDate) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.purchaseDate = purchaseDate;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getProductQuantity() {
        return productQuantity;
    }

    public void setProductQuantity(String productQuantity) {
        this.productQuantity = productQuantity;
    }

    public String getPurchaseDate() {
        return purchaseDate;
    }

    public void setPurchaseDate(String purchaseDate) {
        this.purchaseDate = purchaseDate;
    }

}
