package com.vc.collectionchest;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    //Variables declared
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    Button btnLogin,btnRegister,btnGoogle;
    EditText txtEmail,txtPassword;
    TextView btnSign;
    String email,password;
    GoogleSignInOptions gso;
    GoogleSignInClient gsc;

     //Login UI  adpated from :
     //Technical Skillz
    //Link:https://youtu.be/BLfqZlUI_MM

    @Override
    public void onStart() {
        super.onStart();
        FirebaseApp.initializeApp(this);
        //Open dashboard if user is logged in
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null ) {
        Intent myIntent = new Intent(MainActivity.this, DashBoard.class);
        startActivity(myIntent);
        currentUser.reload();
    }
}

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        btnLogin = findViewById(R.id.btnLogin);

        btnGoogle = findViewById(R.id.btnGoogle);
        txtEmail = findViewById(R.id.txtEmail);
        txtPassword = findViewById(R.id.txtPassword);
        btnSign = findViewById(R.id.textViewSignUp);



        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();
        gsc = GoogleSignIn.getClient(this,gso);
          btnGoogle.setVisibility(View.GONE);
        btnSign.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, Register.class));
            }
                });


        //Code to prevent dark mode on users phone
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
         //Button to Login into app
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email = txtEmail.getText().toString().trim();
                password = txtPassword.getText().toString().trim();
                //Validation for login
                if( email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(MainActivity.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }else {
                    mAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                                        Intent i = new Intent(MainActivity.this,DashBoard.class);
                                        startActivity(i);
                                    }
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

        //Button to sign in with Google
        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SignIn();
            }


        });


    }
//Method for google sign in
    private void SignIn() {

            Intent intent = gsc.getSignInIntent();
            startActivityForResult(intent,100);
        }

        @Override
        protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
            super.onActivityResult(resultCode,requestCode,data);
            if (requestCode==100){
                Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
                try {
                    task.getResult(ApiException.class);
                    Dashboard();
                }catch (Exception e){
                    Toast.makeText(this,"Error",Toast.LENGTH_SHORT).show();
                }

            }
        }
        //Code to open home page
        private void Dashboard() {
        finish();
        Intent intent = new Intent(getApplicationContext(),DashBoard.class);
        startActivity(intent);
        }
    }
