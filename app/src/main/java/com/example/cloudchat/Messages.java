package com.example.cloudchat;

import java.util.Date;

public class Messages {

    private String author;
    private String idAuthor;
    private String message;
    private Date date;

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getIdAuthor() {
        return idAuthor;
    }

    public void setIdAuthor(String idAuthor) {
        this.idAuthor = idAuthor;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Messages() {
    }

    public Messages(String author, String idAuthor, String message, Date date) {
        this.author = author;
        this.idAuthor = idAuthor;
        this.message = message;
        this.date = date;
    }
}
