package com.example.app.krishishop.Model;

public class Register {
    private String username;
    private String email;
    private String createPassword;
    private String usertype;

    public Register(String username, String email, String createPassword, String usertype) {
        this.username = username;
        this.email = email;
        this.createPassword = createPassword;
        this.usertype = usertype;
    }

    public Register() {

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

    public String getCreatePassword() {
        return createPassword;
    }

    public void setCreatePassword(String createPassword) {
        this.createPassword = createPassword;
    }

    public String getUsertype() {
        return usertype;
    }

    public void setUsertype(String usertype) {
        this.usertype = usertype;
    }
}
