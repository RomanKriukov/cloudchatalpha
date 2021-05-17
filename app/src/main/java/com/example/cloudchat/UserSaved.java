package com.example.cloudchat;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Date;

public class UserSaved{

    public static String FILE_NAME = "userInit";
    private User user;
    public String textJson = "";

    public UserSaved(User user) {
        this.user = user;
    }

    public UserSaved() {
    }


    public void convertToJson(){

        textJson = "{" +
                "\"id\":" + "\""+ user.getId() + "\"," +
                "\"UserName\":" + "\""+ user.getUserName() + "\"," +
                "\"FirstName\":" + "\""+ user.getFirstName() + "\"," +
                "\"SecondName\":" + "\""+ user.getSecondName() + "\"," +
                "\"UserEmail\":" + "\""+ user.getUserEmail() + "\"," +
                "\"Password\":" + "\""+ user.getPassword() + "\"" +
                //"\"DataReg\":" + "\""+ user.getDataReg() + "\"" +
                "}";
    }

    public User fromJsonString(String json){
        String id = "";
        String userName = "";
        String firstName = "";
        String secondName = "";
        String userEmail = "";
        String password = "";
        try {
            JSONObject jo = new JSONObject(json);
            id = jo.getString("id");
            userName = jo.getString("UserName");
            firstName = jo.getString("FirstName");
            secondName = jo.getString("SecondName");
            userEmail = jo.getString("UserEmail");
            password = jo.getString("Password");
            //dataReg = jo.getString("DataReg");

        } catch (JSONException ex) {
            ex.printStackTrace();
        }
        return new User(id, userName, firstName, secondName, userEmail, password);
    }

    public static String parcerId(String user){
        String result = "";
        int startNum = user.indexOf('.');
        int endNum = user.length();
        result = user.substring(startNum, endNum);
        result = user.replace(result, "");

        return result;
    }

    public static String parcerLogin(String user){
        String result = "";
        int startNum = user.indexOf('&');
        int endNum = user.length();
        result = user.substring(startNum, endNum);
        result = user.replace(result, "");

        return result;
    }

    public static String parcerIdMsg(String user){
        String result = "";
        int startNum = user.indexOf('&') + 1;
        int endNum = user.length();
        result = user.substring(startNum, endNum);
        //result = user.replace(result, "");

        return result;
    }
}
