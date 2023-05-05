package com.example.onlinechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    EditText et_email,et_password,et_name,et_cpassword;
    Button signup;
    TextView login;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        mAuth=FirebaseAuth.getInstance();
        db=FirebaseFirestore.getInstance();

        et_email=findViewById(R.id.et_email);
        et_password=findViewById(R.id.et_password);
        et_name=findViewById(R.id.et_name);
        et_cpassword=findViewById(R.id.et_confirm_password);
        signup =findViewById(R.id.btn_register);
        login=findViewById(R.id.tv_login);
        progressBar=findViewById(R.id.progressbarR);


        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddNewUser();
            }
        });
    }
    public void AddNewUser(){
        progressBar.setVisibility(View.VISIBLE);
        String name=et_name.getText().toString();
        String email=et_email.getText().toString();
        String password=et_password.getText().toString();
        String cPassword=et_cpassword.getText().toString();


        if(TextUtils.isEmpty(name)){
            Toast.makeText(RegisterActivity.this,"Please fill name",Toast.LENGTH_SHORT).show();
            return;
        }
        if(TextUtils.isEmpty(email)){
            Toast.makeText(RegisterActivity.this,"Please fill email",Toast.LENGTH_SHORT).show();
            return;
        }
        if (TextUtils.isEmpty(password)){
            Toast.makeText(RegisterActivity.this,"Please fill password",Toast.LENGTH_SHORT).show();
            return;
        }

        if (TextUtils.isEmpty(cPassword)){
            Toast.makeText(RegisterActivity.this,"Please fill confirm password",Toast.LENGTH_SHORT).show();
            return;
        }

        mAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            FirebaseUser firebaseUser = mAuth.getCurrentUser();
                            String userID = firebaseUser.getUid();

                            Map<String, Object> user = new HashMap<>();
                            user.put("id", userID);
                            user.put("name",name);
                            user.put("email",email);
                            user.put("password",password);
                            user.put("cPassword",cPassword);

                            db.collection("users").document(userID)
                                    .set(user)
                                    .addOnSuccessListener(documentReference -> {
                                        Log.e("n", "Data added successfully to database: ");
                                    })
                                    .addOnFailureListener(e -> Log.e("n", "Failed to add database", e));

                            Toast.makeText(RegisterActivity.this,"Welcome",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                            Intent intent=new Intent(RegisterActivity.this, LoginActivity.class);
                            startActivity(intent);

                        }else {
                            Toast.makeText(RegisterActivity.this,"Sign up Failed",Toast.LENGTH_SHORT).show();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });

    }

}