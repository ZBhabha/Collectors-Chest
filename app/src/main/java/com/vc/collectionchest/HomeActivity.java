package com.vc.collectionchest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class HomeActivity extends AppCompatActivity {
    //Variables declared
    EditText inputSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingbtn;
    FirebaseAuth mAuth;
    ImageView btnDeletes;

    private FirebaseUser currentUser;
    FirebaseRecyclerOptions<CollectionActivity> options;
    FirebaseRecyclerAdapter<CollectionActivity, MyViewHolder> adapter;
    DatabaseReference Dataref;

    StorageReference StorageRef;
    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        mAuth = FirebaseAuth.getInstance();

        Dataref = FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child("CollectionName");
        inputSearch = findViewById(R.id.inputSearch);
        recyclerView = findViewById(R.id.recylerView);
        floatingbtn = findViewById(R.id.floatingbtn);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        StorageRef = FirebaseStorage.getInstance().getReference().child(user_id).child("CollectionImage");

        recyclerView.setHasFixedSize(true);


        //Floating button to add a new collection
        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), CollectionDetails.class));
            }
        });
        //Populate recyclerview with collections
        LoadData("");
////////

        //Code adapted from:
        //Author: Shafaqat Ali - Technical Skillz
        // Link: https://youtu.be/_nIoEAC3kLg

        //Allows user to search through collections
        inputSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString() != null) {
                    LoadData(s.toString());
                } else {
                    LoadData("");
                }

            }
        });

    }

    //Method to load data
    private void LoadData(String data) {
        Query query = Dataref.orderByChild("CollectionName").startAt(data).endAt(data + "\uf8ff");

        options = new FirebaseRecyclerOptions.Builder<CollectionActivity>().setQuery(query, CollectionActivity.class).build();
        adapter = new FirebaseRecyclerAdapter<CollectionActivity, MyViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position, @NonNull CollectionActivity model) {

                //Collections recyclerview is populated with the required data from firebase
                holder.textView.setText(model.getCollectionName());
                holder.goalsView.setText(String.valueOf(model.getCollectionGoal()));
                Picasso.get().load(model.getImageUrl()).into(holder.imageView);



                holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(HomeActivity.this, HomeActivity2.class);
                        HelperClass.goal = model.getCollectionGoal();
                        String key = model.getCollectionID();
                        //Collection key is stored in helper variable for item classes to access
                        HelperClass.key = key;
                        HelperClass.category = model.getCollectionName();
                        startActivity(intent);
                    }
                });

            }


            @NonNull
            @Override
            public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.single_view, parent, false);
                return new MyViewHolder(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }

    //Method to ensure user is taken to dashboard whn back button is pressed
    //Not to add collection page
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(HomeActivity.this, DashBoard.class);
        startActivity(i);

        finish();

    }

}
