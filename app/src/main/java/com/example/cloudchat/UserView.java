package com.example.cloudchat;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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

public class UserView extends AppCompatActivity {

    private ListView listView;
    private ArrayAdapter<String> adapter;
    private List<String> listUsers;
    private List<User> listTemp;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private User myUser;
    private User user;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_view);

        Init();
        getIntentMain();
        getUsersFromDB();
        setOnclickItem();
    }

    private void Init(){
        setTitle("Пользователи");
        listView = findViewById(R.id.list_users);
        listUsers = new ArrayList<>();
        listTemp = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listUsers);
        listView.setAdapter(adapter);
        myUser = new User();
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");
    }

    //=============================================================================================================================

    public void getUser(){
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                myUser = snapshot.child("Users").child("ID_" + UserSaved.parcerId(mAuth.getCurrentUser().getEmail())).getValue(User.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    //=============================================================================================================================

    private void getUsersFromDB(){
        ValueEventListener valueEventListener = new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(listUsers.size() > 0)listUsers.clear();
                if(listTemp.size() > 0)listTemp.clear();
                for(DataSnapshot ds : snapshot.getChildren()){
                    User users = ds.getValue(User.class);
                    assert users != null;
                    if(!users.getId().equals(UserSaved.parcerId("ID_" + mAuth.getCurrentUser().getEmail()))) {
                        listUsers.add(users.getUserName());
                        listTemp.add(users);
                    }
                }
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        };
        mDatabase.addValueEventListener(valueEventListener);
    }

    //=============================================================================================================================

    private void setOnclickItem(){
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                user = listTemp.get(position);

                Intent showPrivateMsg = new Intent(UserView.this, ShowPrivateMessages.class);
                showPrivateMsg.putExtra("id_user", user.getId());
                showPrivateMsg.putExtra("user_name", user.getUserName());
                //showPrivateMsg.putExtra("first_name", user.getFirstName());
                //showPrivateMsg.putExtra("second_name", user.getSecondName());
                //showPrivateMsg.putExtra("user_email", user.getUserEmail());
                //showPrivateMsg.putExtra("password", user.getPassword());

                showPrivateMsg.putExtra("id_my_user", myUser.getId());
                showPrivateMsg.putExtra("my_user_name", myUser.getUserName());
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
            myUser = new User(keyUser.getStringExtra("id_user"),
                    keyUser.getStringExtra("user_name")
            );

            /*,
                    keyUser.getStringExtra("first_name"),
                    keyUser.getStringExtra("second_name"),
                    keyUser.getStringExtra("user_email"),
                    keyUser.getStringExtra("password")*/
        }
    }
}
