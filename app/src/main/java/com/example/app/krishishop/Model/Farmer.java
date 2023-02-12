package com.example.app.krishishop.Model;

public class Farmer {
    private String username;
    private String email;
    private String password;
    private String mobileno;
    private String usertype;




    public Farmer(String username, String email, String password, String mobileno,String usertype) {
        this.username = username;
        this.email = email;
        this.password = password;
        this.mobileno = mobileno;
        this.usertype = usertype;
    }
    public Farmer(){

    }
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getMobileno() {
        return mobileno;
    }

    public void setMobileno(String mobileno) {
        this.mobileno = mobileno;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
