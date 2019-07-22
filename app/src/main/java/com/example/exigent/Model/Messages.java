package com.example.exigent.Model;

 public class Messages {

    private  String message, date;

    public Messages() {
    }

    public Messages(String message, String date) {

        this.message = message;
        this.date = date;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
