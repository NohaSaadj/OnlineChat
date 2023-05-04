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

import java.util.ArrayList;
import java.util.List;

public class ProfileActivity extends AppCompatActivity {
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    TextView Name,Email,Password;
    EditText updateEmail;
    EditText updatePassword;
    ImageButton Edit;

    ImageView add;

    User user;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Name=findViewById(R.id.usernam_text);
        Email=findViewById(R.id.email_text);
        Password=findViewById(R.id.password_text);
        Edit=findViewById(R.id.profileEditBtn);

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

        Edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String currentUserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                updateUser(currentUserId);
            }
        });
        GetAllUsers();

    }
        public  void GetAllUsers(){
            db.collection("users").get()
                    .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot documentSnapshots) {
                            if (documentSnapshots.isEmpty()) {
                                Log.d("n", "onSuccess: LIST EMPTY");
                                return;
                            } else {
                                for (DocumentSnapshot documentSnapshot : documentSnapshots) {
                                    if (documentSnapshot.exists()) {
                                        Name.setText(documentSnapshot.getString("name").toString());
                                        Email.setText(documentSnapshot.getString("email").toString());
                                        Password.setText(documentSnapshot.getString("password").toString());

                                    }
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.e("LogDATA", "get failed  ");

                        }
                    });
        }
    public void updateUser(final String userId) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Edit Profile");
        final View customLayout = getLayoutInflater().inflate(R.layout.dialog, null);
        builder.setView(customLayout);

        // Get references to the EditText fields in the dialog
        final EditText updateName = customLayout.findViewById(R.id.update_username);
        final EditText updateEmail = customLayout.findViewById(R.id.update_email);
        final EditText updatePassword = customLayout.findViewById(R.id.update_password);

        // Retrieve the user's current data from the database
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

        // Set up the dialog's "Update" button
        builder.setPositiveButton("Update", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                // Update the user's data in the database
                db.collection("users").document(userId)
                        .update("name", updateName.getText().toString(),
                                "email", updateEmail.getText().toString(),
                                "password", updatePassword.getText().toString())
                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
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

        // Show the dialog
        AlertDialog dialog = builder.create();
        dialog.show();
    }



}
