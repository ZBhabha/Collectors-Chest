package com.vc.collectionchest;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.HashMap;

public class ItemDetails extends AppCompatActivity {
     //Variables declared
    private static final int REQUEST_CODE_IMAGE =101 ;
    private ImageView ivAdd;
    private EditText txtItem,txtDate,txtCategory,txtDescription;

    private TextView textViewProgress;
    private ProgressBar progressBar;
    private Button btnUpload;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();

     String itemName;
     String itemCategory;
     String itemDate;
     String itemDescription;

    Uri imageUri;
    boolean isImageAdded=false;

    DatabaseReference Dataref;
    StorageReference StorageRef;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item_details);

        ivAdd=findViewById(R.id.ivAdd);
        txtItem=findViewById(R.id.txtItem);
        txtDate = findViewById(R.id.txtDate);
        txtCategory = findViewById(R.id.txtCategory);
        textViewProgress=findViewById(R.id.textViewProgress);
        progressBar=findViewById(R.id.progressBar);
        btnUpload=findViewById(R.id.btnUpload);
        //Collection name is already set for the user when adding a new item
        txtCategory.setText(HelperClass.category);
        txtDescription= findViewById(R.id.txtDescription);
        textViewProgress.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        Dataref= FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child("CollectionName").child(HelperClass.key).child("ItemName");
        StorageRef= FirebaseStorage.getInstance().getReference().child(user_id).child("CollectionImage").child("ItemName");



        //Code adapted from:
        //Author: Shafaqat Ali - Technical Skillz
        // Link: https://youtu.be/2EhlB4jqb48
        //Imageview button to add item image
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });
        //Item object created
        ItemActivity i = new ItemActivity(itemName,itemDescription,itemDate,itemCategory);
        //Button to upload collection to Firebase
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnUpload.setEnabled(false);
                itemName = txtItem.getText().toString().trim();
                itemDescription = txtDescription.getText().toString().trim();
                itemDate = txtDate.getText().toString().trim();
                itemCategory = HelperClass.category;
                //Validation for user inputs
               if (itemDate.isEmpty()||itemDescription.isEmpty()||itemName.isEmpty()){
                   btnUpload.setEnabled(true);
                   Toast.makeText(ItemDetails.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
               }else if (isImageAdded!=false && itemName!=null)
                {
                    uploadImage(i);

                }else{
                    Toast.makeText(ItemDetails.this, "Please add an image", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    //Method to send collection data to firebase
    private void uploadImage(ItemActivity item) {
        textViewProgress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);



        final  String key=Dataref.push().getKey();
        StorageRef.child(key+".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                StorageRef.child(key +".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        HashMap<String,String> hashMap=new HashMap();

                        hashMap.put("ItemName",itemName);
                        hashMap.put("ItemDescription",itemDescription);
                        hashMap.put("itemDate",itemDate);
                        hashMap.put("ItemCategory",itemCategory);
                        hashMap.put("ImageUrl",uri.toString());

                        Dataref.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {

                            @Override
                            public void onSuccess(Void aVoid) {
                                startActivity(new Intent(getApplicationContext(), HomeActivity2.class));
                                Toast.makeText(ItemDetails.this, "Item Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                                //btnUpload.setClickable(true);
                            }
                        });
                    }
                });

            }// Progress bar to show how far the uploading process is in terms of %
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {

                double progress=(taskSnapshot.getBytesTransferred()*100)/taskSnapshot.getTotalByteCount();
                progressBar.setProgress((int) progress);
                textViewProgress.setText(progress +" %");
            }
        });



    }
    //Method for adding an image and displaying in imageview

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==REQUEST_CODE_IMAGE && data!=null)
        {
            imageUri=data.getData();
            isImageAdded=true;
            ivAdd.setImageURI(imageUri);
        }
    }
}
