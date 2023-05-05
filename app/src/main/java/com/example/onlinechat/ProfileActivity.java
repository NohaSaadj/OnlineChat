package com.example.onlinechat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.onlinechat.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.storage.FirebaseStorage;

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    FirebaseStorage Storage = FirebaseStorage.getInstance();
    String userID = mAuth.getCurrentUser().getUid();
    TextView Name,Email,Password;
    ImageButton ChooseImg;
    ImageButton Edit;
    ImageView add;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Name=findViewById(R.id.usernam_text);
        Email=findViewById(R.id.email_text);
        Password=findViewById(R.id.password_text);
        Edit=findViewById(R.id.profileEditBtn);
        ChooseImg=findViewById(R.id.btnChooseImage);

        FirebaseMessaging.getInstance().subscribeToTopic("")//name topic for example
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Log.e("n","done");

                        }else {
                            Log.e("n","faild");
                        }
                    }
                });

        ChooseImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");

            }
        });

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                updateUser(currentUserId);
            }
        });
        GetUserById(userID);

    }
    public void GetUserById(String userID) {
        db.collection("users").document(userID)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            Name.setText(documentSnapshot.getString("name"));
                            Email.setText(documentSnapshot.getString("email"));
                            Password.setText(documentSnapshot.getString("password"));
                        } else {
                            Log.d("n", "No such document");
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.d("n", "get failed with ", e);
                    }
                });
        }
    public void updateUser(final String userId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Profile");
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog, null);
        builder.setView(customLayout);


        final EditText updateName = customLayout.findViewById(R.id.update_username);
        final EditText updateEmail = customLayout.findViewById(R.id.update_email);
        final EditText updatePassword = customLayout.findViewById(R.id.update_password);


        db.collection("users").document(userId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        if (documentSnapshot.exists()) {
                            // Set the current data in the EditText fields
                            updateName.setText(documentSnapshot.getString("name"));
                            updateEmail.setText(documentSnapshot.getString("email"));
                            updatePassword.setText(documentSnapshot.getString("password"));
                        }
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("n", "Error retrieving user data", e);
                    }
                });


        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                db.collection("users").document(userId)
                        .update("name", updateName.getText().toString(),
                                "email", updateEmail.getText().toString(),
                                "password", updatePassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                Toast.makeText(ProfileActivity.this,"successfully updated",Toast.LENGTH_SHORT).show();
                                Log.d("n", "DocumentSnapshot successfully updated!");
                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Log.e("n", "Error updating document", e);
                            }
                        });
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }



}
