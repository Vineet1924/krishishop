package com.example.app.krishishop.Model;

public class Reseller {
    private String username;
    private String city;
    private String email;
    private String password;
    private String mobileno;
    private String storeid;
    private String usertype;

    public Reseller(String username, String city, String email, String password, String mobileno, String storeid, String usertype){

        this.username = username;
        this.city = city;
        this.email = email;
        this.password = password;
        this.mobileno = mobileno;
        this.storeid = storeid;
        this.usertype = usertype;
    }

    public Reseller() {

    }

    public String getUsername(){
        return username;
    }
    public void setUsername(String username){
        this.username=username;
    }
    public String getCity(){
        return city;
    }
    public void setCity(String city){
        this.city=city;
    }
    public String getEmail(){
        return email;
    }
    public void setEmail(String email){
        this.email=email;
    }
    public String getPassword(){
        return password;
    }
    public void setPassword(String password){
        this.password=password;
    }
    public String getMobileno(){
        return mobileno;
    }
    public void setMobileno(String mobileno){
        this.mobileno=mobileno;
    }
    public String getStoreid(){
        return storeid;
    }
    public void setStoreid(String storeid){
        this.storeid=storeid;
    }
    public String getUsertype() {
        return usertype;
    }
    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
