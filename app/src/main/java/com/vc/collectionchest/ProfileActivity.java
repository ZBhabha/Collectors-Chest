package com.vc.collectionchest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    TextInputEditText txtEmail,txtName;
    TextView txtDisplay;
    DatabaseReference Dataref;
    Button btnLog,btnUpdate;
    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    //Profile UI adapted from :
    //Author: Taimoor Sikander
    //Link: https://youtu.be/9QOg8R8ol1w

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        txtEmail = findViewById(R.id.txtEmail);
        txtDisplay = findViewById(R.id.txtDisplay);
        btnUpdate =findViewById(R.id.btnUpdate);
        btnLog = findViewById(R.id.btnLog);
        Dataref = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);
        txtName = findViewById(R.id.txtName);
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        //Validation to populate email and username fields
        if (user != null) {

            String userEmail = user.getEmail();
            txtEmail.setText(userEmail);

        }
        Dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("name").getValue().toString();
                txtName.setText(userName);
                txtDisplay.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
      //Button to logout
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {

                    String userEmail = user.getEmail();
                    Toast.makeText(ProfileActivity.this, "Goodbye "+userEmail, Toast.LENGTH_SHORT).show();
                }
                mAuth.getInstance().signOut();
                Intent myIntent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
        //Button to update username
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = mAuth.getCurrentUser();
                if (user != null) {
                    String updatedName = txtName.getText().toString().trim();
                    Dataref.child("name").setValue(updatedName);
                    Toast.makeText(ProfileActivity.this, "Username updated successfully", Toast.LENGTH_SHORT).show();

                }



            }
        });

    }
}