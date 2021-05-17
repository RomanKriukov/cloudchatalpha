package com.example.cloudchat;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
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

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GeneralChat extends AppCompatActivity {

    private EditText etSendMsg;
    private ListView listView;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private User user;
    private Messages message;

    private List<Messages> listData;
    private MessageModelAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gchat);
        Init();
        getUser();
        getMsgFromDB();
    }

    private void Init(){
        setTitle("Общий чат");
        etSendMsg = findViewById(R.id.et_input_msg);
        listView = findViewById(R.id.list_item);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = new User();
        message = new Messages();
        listData = new ArrayList<Messages>();
    }

    //=============================================================================================================================

    public void getUser(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                user = snapshot.child("Users").child("ID_" + UserSaved.parcerId(mAuth.getCurrentUser().getEmail())).getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //=============================================================================================================================

    public void getMsgFromDB(){

        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listData.size() > 0){
                    listData.clear();
                }
                for (DataSnapshot ds : snapshot.getChildren()){
                    Messages messages = ds.getValue(Messages.class);
                    assert messages != null;
                    listData.add(messages);
                }
                adapter = new MessageModelAdapter(GeneralChat.this, listData);
                listView.setAdapter(adapter);
                listView.setStackFromBottom(true);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
            }
        };
        mDatabase.child("GeneralChat").addValueEventListener(valueEventListener);
    }

    //=============================================================================================================================

    public void sendButton(View view) {
        if(!TextUtils.isEmpty(etSendMsg.getText().toString())) {
            message.setIdAuthor(user.getId());
            message.setAuthor(user.getUserName());
            message.setMessage(etSendMsg.getText().toString());
            message.setDate(new Date());
            etSendMsg.setText("");
            mDatabase.child("GeneralChat").push().setValue(message);
        }
        else {
            Toast.makeText(GeneralChat.this, "Введите сообщение", Toast.LENGTH_SHORT).show();
        }
    }


}
