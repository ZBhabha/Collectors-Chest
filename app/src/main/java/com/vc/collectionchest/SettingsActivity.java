package com.vc.collectionchest;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SettingsActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private Switch switch1;
    private Button btnSave,btnAbout,btnLog;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String SWITCH1 = "switch1";

    private boolean switchOffOn;

    //---------------------------------------------------code attribution-----------------------------------------
    //Shared preferences code adapted from :
    //author:coding in flow
    //URL:https://www.youtube.com/watch?v=fJEFZ6EOM9o

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        btnLog=findViewById(R.id.btnLog);
        btnAbout = findViewById(R.id.btnAbout);
        mAuth = FirebaseAuth.getInstance();
        switch1 = (Switch) findViewById(R.id.switch1);
        btnSave= (Button) findViewById(R.id.btnSave);


     btnAbout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        Intent myIntent = new Intent(SettingsActivity.this, AboutActivity.class);
        startActivity(myIntent);
    }
      });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }

        });

        loadData();
        updateViews();
        //Button to logout
        btnLog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseUser user = mAuth.getCurrentUser();

                if (user != null) {

                    String userEmail = user.getEmail();

                    Toast.makeText(SettingsActivity.this, "Goodbye "+userEmail, Toast.LENGTH_SHORT).show();
                }
                mAuth.getInstance().signOut();
                Intent myIntent = new Intent(SettingsActivity.this, MainActivity.class);
                startActivity(myIntent);
            }
        });
    }

    public void saveData(){
        SharedPreferences sharedPreferences= getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SWITCH1, switch1.isChecked());
        if(switch1.isChecked()==true) {
            editor.apply();
           HelperClass.on = switch1.isChecked();
            Toast.makeText(this, "PASSWORD PROTECT IS ON", Toast.LENGTH_SHORT).show();
        } else if(switch1.isChecked()==false) {
            editor.apply();
          HelperClass.on = switch1.isChecked();
            Toast.makeText(this, "PASSWORD PROTECT IS OFF", Toast.LENGTH_SHORT).show();
        }

    }

    public void loadData(){
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        switchOffOn = sharedPreferences.getBoolean(SWITCH1, false);



    }

    public void updateViews(){

        switch1.setChecked(switchOffOn);

    }
//Method to ensure user is taken to dashboard whn back button is pressed
    //Not to add collection page
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(SettingsActivity.this, DashBoard.class);
        startActivity(i);

        finish();

    }
}