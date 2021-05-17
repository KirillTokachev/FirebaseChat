package com.example.firebasechat;

public class User {
    private String userName;
    private String email;
    private String id;

    public User() {
    }

    public User(String userName, String email, String id) {
        this.userName = userName;
        this.email = email;
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
