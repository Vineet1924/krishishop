package com.example.app.krishishop.Model;

public class AllHistory {

    String productName;
    String productQuantity;
    String purchaseDate;
    String userID;

    public AllHistory() {
    }

    public AllHistory(String productName, String productQuantity, String purchaseDate, String userID) {
        this.productName = productName;
        this.productQuantity = productQuantity;
        this.purchaseDate = purchaseDate;
        this.userID = userID;
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

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }
}
