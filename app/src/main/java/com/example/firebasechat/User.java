package com.example.firebasechat;

public class User {
    private String userName;
    private String email;
    private String id;
    private int avatarMockUpResource;

    public User() {
    }

    public User(String userName, String email, String id, int avatarMockUpResource) {
        this.userName = userName;
        this.email = email;
        this.id = id;
        this.avatarMockUpResource = avatarMockUpResource;
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

    public int getAvatarMockUpResource() {
        return avatarMockUpResource;
    }

    public void setAvatarMockUpResource(int avatarMockUpResource) {
        this.avatarMockUpResource = avatarMockUpResource;
    }
}
