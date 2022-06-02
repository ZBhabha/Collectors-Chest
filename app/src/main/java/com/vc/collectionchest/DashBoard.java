package com.vc.collectionchest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;

//XML code for dashboard adapted from
//Author : Angga Risky
//https://youtu.be/LOcD1evBcSA
public class DashBoard extends AppCompatActivity {
   //Variables declared
    private FirebaseAuth mAuth;
    ImageView ivCollections,ivProfile;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dash_board);
        ivCollections = findViewById(R.id.ivCollections);
        ivProfile = findViewById(R.id.ivProfile);
        mAuth = FirebaseAuth.getInstance();

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
      }
    }
