package com.vc.collectionchest;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.cardview.widget.CardView;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

//XML code for dashboard adapted from
//Author : Angga Risky
//https://youtu.be/LOcD1evBcSA
public class DashBoard extends AppCompatActivity {
   //Variables declared
    private FirebaseAuth mAuth;
    ImageView ivCollections,ivProfile,ivSettings;
    DatabaseReference Dataref;
   TextView name;
      CardView card;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ivCollections = findViewById(R.id.ivCollections);
        ivProfile = findViewById(R.id.ivProfile);
        ivSettings=findViewById(R.id.ivSettings);
        mAuth = FirebaseAuth.getInstance();
        card = findViewById(R.id.card);
        name = findViewById(R.id.txtName);
        String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Dataref = FirebaseDatabase.getInstance().getReference().child("users").child(user_id);

        //Code to prevent dark mode on users phone
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);

        Dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String userName = dataSnapshot.child("name").getValue().toString();
                name.setText(userName);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        //Imageview button to open collections page
        ivCollections.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    Intent myIntent = new Intent(DashBoard.this, HomeActivity.class);

                    startActivity(myIntent);


            }
        });

        //Imageview button to open profile page
        ivProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(DashBoard.this, ProfileActivity.class);

                startActivity(myIntent);

            }
        });

        //Imageview button to open profile page
        ivSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(DashBoard.this, SettingsActivity.class);

                startActivity(myIntent);

            }
        });

        card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(DashBoard.this, HomeActivity.class);

                startActivity(myIntent);


            }
        });
      }
    }
