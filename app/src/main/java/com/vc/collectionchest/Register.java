package com.vc.collectionchest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {
    //Variables declared
    FirebaseAuth mAuth;
    Button btnRegister;
    EditText txtEmail, txtPassword, txtName;
    private FirebaseUser currentUser;
    String email, password, name;
    TextView btnAcc;

    //Register UI  adpated from :
    //Technical Skillz
    //Link:https://youtu.be/BLfqZlUI_MM

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mAuth = FirebaseAuth.getInstance();
        btnAcc = findViewById(R.id.tvAlreadyHaveAccount);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        txtName = findViewById(R.id.txtName);
        btnRegister = findViewById(R.id.btnRegister);


        // Button to go to login page
        btnAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Register.this, MainActivity.class);
                startActivity(i);
            }
        });


        //Button to register new user
        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = txtEmail.getText().toString().trim();
                password = txtPassword.getText().toString().trim();
                name = txtName.getText().toString().trim();

                if ((email.isEmpty() || password.isEmpty())) {

                    Toast.makeText(Register.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                } else if (!validatePassword()==false && !validateEmail()==false){

                    mAuth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        User user = new User(name);
                                        FirebaseDatabase.getInstance().getReference("users")
                                                .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                                .setValue(user);


                                        if (task.isSuccessful()) {
                                            Toast.makeText(Register.this, "User created", Toast.LENGTH_SHORT).show();

                                            mAuth.getInstance().signOut();

                                            Intent i = new Intent(Register.this, MainActivity.class);
                                            startActivity(i);
                                        }


                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Register.this,e.getMessage(),Toast.LENGTH_SHORT).show();
                            //Toast.makeText(Register.this, "User with this email already exists", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }
    //Validation for password length
    public Boolean validatePassword() {
        password = txtPassword.getText().toString().trim();
        if (password.length() < 6) {
            Toast.makeText(Register.this, "Password has to be 6 characters or more", Toast.LENGTH_SHORT).show();
            return false;
        }else
            return true;

    }
    //Validation for user email
    public Boolean validateEmail(){
        email = txtEmail.getText().toString().trim();
        if(!email.contains("@")){
            Toast.makeText(Register.this, "Enter a valid email", Toast.LENGTH_SHORT).show();
            return false;
        }else{
            return true;
        }
    }
}