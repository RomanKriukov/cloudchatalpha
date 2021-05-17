package com.example.cloudchat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShowPrivateMessages extends AppCompatActivity {

    private EditText etSendMsg;
    private ListView listView;

    private DatabaseReference mDatabase;
    private User user;
    private User contact;
    private Messages message;

    private List<Messages> listData;
    private MessageModelAdapter adapter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_messages);

        Init();
        getIntentMain();
        getMsgFromDB();
    }

    private  void Init(){
        etSendMsg = findViewById(R.id.input_msg);
        listView = findViewById(R.id.list_item_messages);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        user = new User();
        message = new Messages();
        listData = new ArrayList<Messages>();
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
                adapter = new MessageModelAdapter(ShowPrivateMessages.this, listData);
                listView.setAdapter(adapter);
                listView.setStackFromBottom(true);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.child("PrivateChat").child(user.getId()).child(contact.getId()).addValueEventListener(valueEventListener);
    }

    //=============================================================================================================================

    private void getIntentMain(){
        Intent keyUser = getIntent();
        if(keyUser != null) {
            contact = new User(keyUser.getStringExtra("id_user"),
                    keyUser.getStringExtra("user_name")
            );

            user = new User(keyUser.getStringExtra("id_my_user"),
                    keyUser.getStringExtra("my_user_name")
            );
            setTitle(contact.getUserName());

        }
        else {
            setTitle("Null");
        }
    }

    //=============================================================================================================================

    public void sendMessage(View view) {
        getMsgFromDB();
        if(!TextUtils.isEmpty(etSendMsg.getText().toString())) {
            message.setIdAuthor(user.getId());
            message.setAuthor(user.getUserName());
            message.setMessage(etSendMsg.getText().toString());
            message.setDate(new Date());
            etSendMsg.setText("");

            String key = mDatabase.push().getKey();
            mDatabase.child("PrivateChat").child(user.getId()).child(contact.getId()).child(key).setValue(message);
            mDatabase.child("PrivateChat").child(contact.getId()).child(user.getId()).child(key).setValue(message);
            mDatabase.child("AllListChat").child(user.getId()).child(contact.getUserName() + "&" + contact.getId()).setValue(message);
            mDatabase.child("AllListChat").child(contact.getId()).child(user.getUserName() + "&" + user.getId()).setValue(message);
        }
        else {
            Toast.makeText(ShowPrivateMessages.this, "Введите сообщение", Toast.LENGTH_SHORT).show();
        }
    }
}
