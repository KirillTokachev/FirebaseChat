package com.example.firebasechat;

public class Message {

    private String textMessage;
    private String userName;
    private String imageUrl;

    //Поле определения чьё сообщение
    private boolean whoseIsMessage;

    // Поля для общения с одним пользователем
    private String sender;
    private String recipient;

    public Message(){

    }

    public Message(String textMessage, String userName, String imageUrl,
                   boolean whoseIsMessage, String sender, String recipient) {
        this.textMessage = textMessage;
        this.userName = userName;
        this.imageUrl = imageUrl;
        this.whoseIsMessage = whoseIsMessage;
        this.sender = sender;
        this.recipient = recipient;
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

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public boolean isWhoseIsMessage() {
        return whoseIsMessage;
    }

    public void setWhoseIsMessage(boolean whoseIsMessage) {
        this.whoseIsMessage = whoseIsMessage;
    }
}
