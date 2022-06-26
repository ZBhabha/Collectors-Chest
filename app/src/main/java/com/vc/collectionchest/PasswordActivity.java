package com.vc.collectionchest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;

public class PasswordActivity extends AppCompatActivity {
    Button btnEnter;
    EditText edt;
    String user_id;
    String pass;
    DatabaseReference Dataref;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_password);
        btnEnter=findViewById(R.id.btnEnter);
        edt = findViewById(R.id.pass);
        user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Dataref = FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child("Protect");


        Dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {

                    pass =dataSnapshot.child("Protect").getValue().toString();

                   HelperClass.pass = pass;

                } else {

                     Toast.makeText(PasswordActivity.this, "Please reinstall app", Toast.LENGTH_SHORT).show();
                }

            }
///////////////

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


                btnEnter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String input = edt.getText().toString();

                        if (!input.equals("")) {
                            if (input.equals(HelperClass.pass)) {
                                startActivity(new Intent(getApplicationContext(), DashBoard.class));
                                finish();

                            } else {
                                Toast.makeText(PasswordActivity.this, "Wrong Password", Toast.LENGTH_SHORT).show();

                            }
                        } else {
                            Toast.makeText(PasswordActivity.this, "Enter Password", Toast.LENGTH_SHORT).show();

                        }
                    }
                });

    }
    private  String getCurrentPassword(){
        Calendar calendar = Calendar.getInstance();
        int curHours24 =calendar.get(Calendar.HOUR_OF_DAY);
        int curHours12 =calendar.get(Calendar.HOUR);
        int curMinutes = calendar.get(Calendar.MINUTE);
        return curHours12 + ""+curMinutes;
    }
}