package com.example.cloudchat;

import java.util.Date;

public class User {

    private String id;
    private String userName;
    private String firstName;
    private String userEmail;
    private String secondName;
    private String password;
    private Date dataReg;

    public User(String id, String userName, String firstName, String userEmail, String secondName, String password) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.userEmail = userEmail;
        this.secondName = secondName;
        this.password = password;
    }

    public User(String id, String userName, String firstName, String secondName, String userEmail, String password, Date dataReg) {
        this.id = id;
        this.userName = userName;
        this.firstName = firstName;
        this.userEmail = userEmail;
        this.secondName = secondName;
        this.password = password;
        this.dataReg = dataReg;
    }

    public User(String id, String userName) {
        this.id = id;
        this.userName = userName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getSecondName() {
        return secondName;
    }

    public void setSecondName(String secondName) {
        this.secondName = secondName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getDataReg() {
        return dataReg;
    }

    public void setDataReg(Date dataReg) {
        this.dataReg = dataReg;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public User() {
    }
}
