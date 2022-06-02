package com.vc.collectionchest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.squareup.picasso.Picasso;

public class HomeActivity2 extends AppCompatActivity {
    //Vaiables declared
    EditText inputSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingbtn;
    FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    FirebaseRecyclerOptions<ItemActivity> options;
    FirebaseRecyclerAdapter<ItemActivity, ViewHolder2> adapter;
    DatabaseReference Dataref;
    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        mAuth= FirebaseAuth.getInstance();
        Dataref = FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child("CollectionName").child(HelperClass.key).child("ItemName");
        inputSearch = findViewById(R.id.inputSearch);
        recyclerView = findViewById(R.id.recylerView);
        floatingbtn = findViewById(R.id.floatingbtn);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        //Floating button to add a new item
        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ItemDetails.class));
            }
        });
        //Populate recyclerview with collection items
        LoadData("");


        //Code adapted from:
        //Author: Shafaqat Ali - Technical Skillz
        // Link: https://youtu.be/_nIoEAC3kLg

        //Allows user to search through collection items
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

    private void LoadData(String data) {
        Query query=Dataref.orderByChild("ItemName").startAt(data).endAt(data+"\uf8ff");

        options=new FirebaseRecyclerOptions.Builder<ItemActivity>().setQuery(query,ItemActivity.class).build();
        adapter=new FirebaseRecyclerAdapter<ItemActivity, ViewHolder2>(options) {
            @Override
            protected void onBindViewHolder(@NonNull ViewHolder2 holder, @SuppressLint("RecyclerView") final int position, @NonNull ItemActivity model) {

                holder.textView.setText(model.getItemName());
               holder.textView_category.setText(HelperClass.category);
                Picasso.get().load(model.getImageUrl()).into(holder.imageView);
               holder.v.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent=new Intent(HomeActivity2.this, ViewActivity.class);
                        intent.putExtra("CollectionKey",getRef(position).getKey());
                        startActivity(intent);
                    }
                });

            }

            @NonNull
            @Override
            public ViewHolder2 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

                View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.activity_view,parent,false);
                return new ViewHolder2(v);
            }
        };
        adapter.startListening();
        recyclerView.setAdapter(adapter);

    }
    //Method to ensure user is taken to collection page when back button is pressed
    //Not to add item page again
    @Override
    public void onBackPressed()
    {
        super.onBackPressed();
        startActivity(new Intent(HomeActivity2.this, HomeActivity.class));
        finish();

    }
}
