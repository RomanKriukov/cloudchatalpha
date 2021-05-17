package com.example.cloudchat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class ShowAllListChats extends AppCompatActivity {

    private ListView listView;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private User user;
    private Messages message;

    private List<Messages> listData;
    private List<Messages> listTemp;
    private MessageModelAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_all_list_chats);

        Init();
        getIntentMain();
        getMsgFromDB();
        setOnclickItem();
    }

    @SuppressLint("ResourceType")
    private void Init(){
        setTitle("Чаты");
        listView = findViewById(R.id.show_list_chats);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = new User();
        message = new Messages();
        listData = new ArrayList<>();
        listTemp = new ArrayList<>();
    }

    //=============================================================================================================================

    public void getMsgFromDB(){

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                if(listData.size() > 0) listData.clear();
                if(listTemp.size() > 0)listTemp.clear();

                for (DataSnapshot ds : snapshot.getChildren()){
                    Messages messages = ds.getValue(Messages.class);
                    String strLogin = ds.getKey();
                    messages.setAuthor(UserSaved.parcerLogin(strLogin));
                    messages.setIdAuthor(UserSaved.parcerIdMsg(strLogin));

                    assert messages != null;
                    listData.add(messages);
                    listTemp.add(messages);
                }
                adapter = new MessageModelAdapter(ShowAllListChats.this, listData);
                listView.setAdapter(adapter);
                listView.setStackFromBottom(true);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mDatabase.child("AllListChat").child("ID_" + UserSaved.parcerId(mAuth.getCurrentUser().getEmail())).addValueEventListener(valueEventListener);
    }

    //=============================================================================================================================

    private void setOnclickItem(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Messages msg;
                msg = listTemp.get(position);

                Intent showPrivateMsg = new Intent(ShowAllListChats.this, ShowPrivateMessages.class);
                showPrivateMsg.putExtra("id_user", msg.getIdAuthor());
                showPrivateMsg.putExtra("user_name", msg.getAuthor());
                //showPrivateMsg.putExtra("first_name", user.getFirstName());
                //showPrivateMsg.putExtra("second_name", user.getSecondName());
                //showPrivateMsg.putExtra("user_email", user.getUserEmail());
                //showPrivateMsg.putExtra("password", user.getPassword());

                showPrivateMsg.putExtra("id_my_user", user.getId());
                showPrivateMsg.putExtra("my_user_name", user.getUserName());
                //showPrivateMsg.putExtra("my_first_name", myUser.getFirstName());
                //showPrivateMsg.putExtra("my_second_name", myUser.getSecondName());
                //showPrivateMsg.putExtra("my_user_email", myUser.getUserEmail());
                //showPrivateMsg.putExtra("my_password", myUser.getPassword());

                startActivity(showPrivateMsg);
            }
        });
    }

    //=============================================================================================================================

    private void getIntentMain(){
        Intent keyUser = getIntent();
        if(keyUser != null) {
            user = new User(keyUser.getStringExtra("id_user"),
                    keyUser.getStringExtra("user_name")
            );
        }
    }
}
