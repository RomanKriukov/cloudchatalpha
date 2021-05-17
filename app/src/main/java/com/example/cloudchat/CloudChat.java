package com.example.cloudchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

public class CloudChat extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private User user;

    private Button gChat;
    private Button privateMsg;
    private Button allUs;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cloud_chat);
        Init();
        user = fileReader();
        getUser();
    }

    private void Init(){

        gChat = findViewById(R.id.general_chat_btn);
        privateMsg = findViewById(R.id.private_msg_btn);
        allUs = findViewById(R.id.all_users_btn);
        user = new User();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    //=============================================================================================================================

    public void getUser(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.child("Users").child("ID_" + UserSaved.parcerId(mAuth.getCurrentUser().getEmail())).getValue(User.class);
                assert user != null;
                UserSaved userSaved = new UserSaved(user);
                userSaved.convertToJson();
                fileWriter(userSaved.textJson);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //=============================================================================================================================

    public void generalChat(View view) {
        Intent gChat = new Intent(CloudChat.this, GeneralChat.class);
        startActivity(gChat);
    }

    //=============================================================================================================================

    public void privateMessages(View view) {
        Intent showListChat = new Intent(CloudChat.this, ShowAllListChats.class);

        showListChat.putExtra("id_user", user.getId());
        showListChat.putExtra("user_name", user.getUserName());
        //showListChat.putExtra("first_name", user.getFirstName());
        //showListChat.putExtra("second_name", user.getSecondName());
        //showListChat.putExtra("user_email", user.getUserEmail());
        //showListChat.putExtra("password", user.getPassword());

        startActivity(showListChat);
    }

    //=============================================================================================================================

    public void allUsers(View view) {
        Intent usersView = new Intent(CloudChat.this, UserView.class);

        usersView.putExtra("id_user", user.getId());
        usersView.putExtra("user_name", user.getUserName());
        //usersView.putExtra("first_name", user.getFirstName());
        //usersView.putExtra("second_name", user.getSecondName());
        //usersView.putExtra("user_email", user.getUserEmail());
        //usersView.putExtra("password", user.getPassword());

        startActivity(usersView);
    }

    private User fileReader(){
        String txt;
        byte[] buf = new byte[1024];
        FileInputStream r;
        try{
            r = openFileInput(UserSaved.FILE_NAME);
        } catch (FileNotFoundException ex){
            //n = false;
            return null;
        }

        try {
            r.read(buf);
            r.close();
        } catch (IOException ex) {
            //n = false;
            return null;
        }
        txt = new String(buf);
        UserSaved getUser = new UserSaved();
        //n = true;
        return getUser.fromJsonString(txt);
    }

    private void fileWriter(String str){
        FileOutputStream f;

        byte[] buf;
        buf = (str).getBytes();
        try {
            f = openFileOutput(UserSaved.FILE_NAME, Context.MODE_PRIVATE);
        } catch (FileNotFoundException ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            f.write(buf);
            f.flush();
            f.close();
        } catch (IOException ex) {
            Toast.makeText(getApplicationContext(), ex.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }
}