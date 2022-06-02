package com.vc.collectionchest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ViewActivity extends AppCompatActivity {
    //Variables declared
    TextView name,description,date;
    ImageView btnDelete;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    DatabaseReference ref,DataRef;
    StorageReference StorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        name=findViewById(R.id.textView_name);
        description = findViewById(R.id.textView_description);
        btnDelete=findViewById(R.id.btnDelete);
        date = findViewById(R.id.textView_date);
        String CollectionKey=getIntent().getStringExtra("CollectionKey");
        ref= FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child("CollectionName").child(HelperClass.key);
        DataRef=FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child("CollectionName").child(HelperClass.key).child("ItemName").child(CollectionKey);
        StorageRef= FirebaseStorage.getInstance().getReference().child(user_id).child("CollectionImage").child("ItemName").child(CollectionKey+".jpg");
        //Datasnapshot code adapted from:
        //Author: Frank van Puffelen
        //Link: https://stackoverflow.com/a/62097918
       DataRef.orderByKey().addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.exists())
                {
                    //Values to display item information
                    String itemName=dataSnapshot.child("ItemName").getValue().toString();
                    String  itemdescription = dataSnapshot.child("ItemDescription").getValue().toString();
                    String itemdate =dataSnapshot.child("itemDate").getValue().toString();

                    name.setText(itemName);
                    description.setText(itemdescription);
                    date.setText(itemdate);



                } else {

                   // Toast.makeText(ViewActivity.this, "No snapshot found", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
                //Button to delete item from collection
               btnDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        //Alert dialog code taken from :
                        //Author: Viral Patel
                        //Link:https://stackoverflow.com/a/36106306
                        AlertDialog.Builder alert = new AlertDialog.Builder(ViewActivity.this);
                        alert.setTitle("Delete entry");
                        alert.setMessage("Are you sure you want to delete?");
                        alert.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                        Intent i = new Intent(ViewActivity.this, HomeActivity2.class);
                        startActivity(i);

                        DataRef.removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                                StorageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void aVoid) {
                                        Toast.makeText(ViewActivity.this, "item deleted successfully", Toast.LENGTH_SHORT).show();

                                    }
                                });
                            }
                        });
                    }
                });
                        alert.setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // close dialog
                                dialog.cancel();
                            }
                        });
                        alert.show();
                    }


        });
}

}

