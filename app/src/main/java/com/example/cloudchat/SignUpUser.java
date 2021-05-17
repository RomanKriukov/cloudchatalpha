package com.example.cloudchat;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class SignUpUser extends AppCompatActivity {

    private EditText userName;
    private EditText firstName;
    private EditText secondName;
    private EditText userEmail;
    private EditText password;
    private EditText cPassword;
    private Date date;
    private User user;

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        Init();
    }

    private void Init(){
        userName = findViewById(R.id.et_user_name);
        firstName = findViewById(R.id.et_user_first_name);
        secondName = findViewById(R.id.et_user_second_name);
        userEmail = findViewById(R.id.user_email);
        password = findViewById(R.id.user_password);
        cPassword = findViewById(R.id.confirm_password);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent signIn = new Intent(SignUpUser.this, MainActivity.class);
        startActivity(signIn);
        stopActivity();
    }

    public void signUpUser(View view) {

        if (!TextUtils.isEmpty(userName.getText().toString()) &&
                !TextUtils.isEmpty(firstName.getText().toString()) &&
                !TextUtils.isEmpty(secondName.getText().toString()) &&
                !TextUtils.isEmpty(userEmail.getText().toString()) &&
                !TextUtils.isEmpty(password.getText().toString()) &&
                password.getText().toString().equals(cPassword.getText().toString())) {
            mAuth.createUserWithEmailAndPassword(userEmail.getText().toString(), password.getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        date = new Date();
                        user = new User("ID_" + UserSaved.parcerId(userEmail.getText().toString()), userName.getText().toString(), firstName.getText().toString(), secondName.getText().toString(), userEmail.getText().toString(), SHA256.cryptPass(password.getText().toString()), date);

                        mDatabase.child("Users").child(user.getId()).setValue(user);
                        UserSaved userSaved = new UserSaved(user);
                        userSaved.convertToJson();
                        fileWriter(userSaved.textJson);

                        Intent goChat = new Intent(SignUpUser.this, CloudChat.class);
                        startActivity(goChat);
                        stopActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), "User not SignUP", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
        else {
            Toast.makeText(this, "Введите данные корректно", Toast.LENGTH_SHORT).show();
        }
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

    private void stopActivity(){
        this.finish();
    }
}
