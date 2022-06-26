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
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

import nl.dionsegijn.konfetti.core.Angle;
import nl.dionsegijn.konfetti.core.Party;
import nl.dionsegijn.konfetti.core.PartyFactory;
import nl.dionsegijn.konfetti.core.Position;
import nl.dionsegijn.konfetti.core.Spread;
import nl.dionsegijn.konfetti.core.emitter.Emitter;
import nl.dionsegijn.konfetti.core.emitter.EmitterConfig;
import nl.dionsegijn.konfetti.core.models.Shape;
import nl.dionsegijn.konfetti.core.models.Size;
import nl.dionsegijn.konfetti.xml.KonfettiView;

public class HomeActivity2 extends AppCompatActivity {
    //Variables declared
    private TextView textViewProgress;
    private ProgressBar progressBar;
    EditText inputSearch;
    RecyclerView recyclerView;
    FloatingActionButton floatingbtn;
    FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    FirebaseRecyclerOptions<ItemActivity> options;
    FirebaseRecyclerAdapter<ItemActivity, ViewHolder2> adapter;
    int count;
     Boolean congrats =false;

     Button btnGraph;



    KonfettiView konfettiView;
    DatabaseReference Dataref;
    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child("CollectionName").child(HelperClass.key);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home2);
        mAuth= FirebaseAuth.getInstance();
        Dataref = FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child("CollectionName").child(HelperClass.key).child("ItemName");
        inputSearch = findViewById(R.id.inputSearch);
        recyclerView = findViewById(R.id.recylerView);
        floatingbtn = findViewById(R.id.floatingbtn);
        textViewProgress=findViewById(R.id.textViewProgress);
        progressBar=findViewById(R.id.progressBar);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));
        recyclerView.setHasFixedSize(true);
        konfettiView = findViewById(R.id.konfettiView);
        btnGraph = findViewById(R.id.btnGraph);


        //Floating button to add a new item
        floatingbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), ItemDetails.class));
            }
        });

        //Populate recyclerview with collection items
        LoadData("");


        Dataref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                 count = (int) dataSnapshot.getChildrenCount();
              HelperClass.itemCount = count;
                int number = Integer.parseInt(HelperClass.goal);
                if(count == number){
                    congrats = true;
                }
                double display = (double)count / (double) number* 100;
                textViewProgress.setText(HelperClass.category+": " + count +"/"+HelperClass.goal);

                if(congrats==true&&HelperClass.check==true){
                    yay();
                }
                progressBar.setProgress((int)display);
                HelperClass.count = display;
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });




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
        btnGraph.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent myIntent = new Intent(HomeActivity2.this, GraphActivity.class);

                startActivity(myIntent);

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

    //Method to display confetti when user reaches their goal
    //Confetti code adapted from :
    //Github- Konfetti library
    //Author: Daniel Martinus
    //https://github.com/DanielMartinus/Konfetti

    public void yay(){
        EmitterConfig emitterConfig = new Emitter(10L, TimeUnit.SECONDS).perSecond(100);
        Party party =
                new PartyFactory(emitterConfig)
                        .angle(Angle.BOTTOM)
                        .spread(Spread.ROUND)
                        //  .shapes(Arrays.asList(Shape.Square.INSTANCE, Shape.Circle.INSTANCE, drawableShape))
                        .colors(Arrays.asList(0xfce18a, 0xff726d, 0xf4306d, 0xb48def))
                        .setSpeedBetween(0f, 15f)
                        .position(new Position.Relative(0.0, 0.0).between(new Position.Relative(1.0, 0.0)))
                        .build();
        konfettiView.start(party);
        konfettiView.setOnClickListener(view ->
                konfettiView.start(party)
        );
    }
}
