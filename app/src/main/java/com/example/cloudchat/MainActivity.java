package com.example.cloudchat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;;

    private EditText etEmail;
    private EditText etPassword;

    private Intent goChat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Init();
    }

    private void Init(){
        mAuth = FirebaseAuth.getInstance();
        etEmail = findViewById(R.id.editTextTextPersonName);
        etPassword = findViewById(R.id.editTextTextPassword);
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            goChat = new Intent(MainActivity.this, CloudChat.class);
            startActivity(goChat);
            stopActivity();
        }else {
            Toast.makeText(MainActivity.this, "Выполните вход или зарегестрируйтесь", Toast.LENGTH_LONG).show();
        }
    }

    public void userAuth(View view) {
        signIn();
    }

    public void userRegister(View view) {
        Intent signUp = new Intent(MainActivity.this, SignUpUser.class);
        startActivity(signUp);
        stopActivity();
    }

    private void signIn(){

        if (!TextUtils.isEmpty(etEmail.getText().toString()) && !TextUtils.isEmpty(etPassword.getText().toString())){
            mAuth.signInWithEmailAndPassword(etEmail.getText().toString(), etPassword.getText().toString()).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @SuppressLint("SetTextI18n")
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        goChat = new Intent(MainActivity.this, CloudChat.class);
                        startActivity(goChat);
                        stopActivity();
                    } else {
                        Toast.makeText(getApplicationContext(), "Вы не можете войти под этими данными", Toast.LENGTH_SHORT).show();
                    }
                }
            });

        }
        else {
            Toast.makeText(this, "Введите данные корректно", Toast.LENGTH_SHORT).show();
        }
    }

    private void stopActivity(){
        this.finish();
    }
}