package com.example.onlinechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity {

    EditText et_email,et_password;
    Button signin;
    ProgressBar progressBar;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth=FirebaseAuth.getInstance();

        et_email=findViewById(R.id.et_email);
        et_password=findViewById(R.id.et_password);
        signin =findViewById(R.id.btn_login);
        progressBar=findViewById(R.id.progressbar);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GetUser();
            }
        });
    }
    public void GetUser(){
        progressBar.setVisibility(View.VISIBLE);
        String email=et_email.getText().toString();
        String password=et_password.getText().toString();

        if(TextUtils.isEmpty(email)){
            Toast.makeText(LoginActivity.this,"Please fill email",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(LoginActivity.this,"Please fill password",Toast.LENGTH_SHORT).show();
            return;
        }
        mAuth.signInWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){

                            Toast.makeText(LoginActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            Intent intent=new Intent(LoginActivity.this,ProfileActivity.class);
                            startActivity(intent);
                        }else {
                            Toast.makeText(LoginActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
    }


}