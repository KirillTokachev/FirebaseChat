package com.example.firebasechat;

public class Message {

    String textMessage;
    String userName;
    String imageUrl;

    public Message(){

    }

    public Message(String textMessage, String userName, String imageUrl) {
        this.textMessage = textMessage;
        this.userName = userName;
        this.imageUrl = imageUrl;
    }

    public String getTextMessage() {
        return textMessage;
    }

    public void setTextMessage(String textMessage) {
        this.textMessage = textMessage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
