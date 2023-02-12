package com.example.app.krishishop.Model;

import java.io.Serializable;

public class DeliveryModel implements Serializable {

    String status;
    String paymentId;
    String paymentTime;
    String paymentDate;
    Double totalPaid;
    String documentId;
    String confirmOrder;
    String name;
    String address;
    String pinCode;
    String phoneNumber;
    String products;

    public DeliveryModel() {
    }

    public DeliveryModel(String status, String paymentId, String paymentTime, String paymentDate,
                         Double totalPaid, String documentId, String confirmOrder,String name,
                         String address, String pinCode,String phoneNumber,String products) {
        this.status = status;
        this.paymentId = paymentId;
        this.paymentTime = paymentTime;
        this.paymentDate = paymentDate;
        this.totalPaid = totalPaid;
        this.documentId = documentId;
        this.confirmOrder = confirmOrder;
        this.name = name;
        this.address = address;
        this.pinCode = pinCode;
        this.phoneNumber = phoneNumber;
        this.products = products;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getPaymentId() {
        return paymentId;
    }

    public void setPaymentId(String paymentId) {
        this.paymentId = paymentId;
    }

    public String getPaymentTime() {
        return paymentTime;
    }

    public void setPaymentTime(String paymentTime) {
        this.paymentTime = paymentTime;
    }

    public String getPaymentDate() {
        return paymentDate;
    }

    public void setPaymentDate(String paymentDate) {
        this.paymentDate = paymentDate;
    }

    public Double getTotalPaid() {
        return totalPaid;
    }

    public void setTotalPaid(Double totalPaid) {
        this.totalPaid = totalPaid;
    }

    public String getDocumentId() {
        return documentId;
    }

    public void setDocumentId(String documentId) {
        this.documentId = documentId;
    }

    public String getConfirmOrder() {
        return confirmOrder;
    }

    public void setConfirmOrder(String confirmOrder) {
        this.confirmOrder = confirmOrder;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPinCode() {
        return pinCode;
    }

    public void setPinCode(String pinCode) {
        this.pinCode = pinCode;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getProducts() {
        return products;
    }

    public void setProducts(String products) {
        this.products = products;
    }
}
