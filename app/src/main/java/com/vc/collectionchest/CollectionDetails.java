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

public class CollectionDetails extends AppCompatActivity {
    //Variables declared

    private static final int REQUEST_CODE_IMAGE =101 ;
    private ImageView ivAdd;
    private EditText txtCollection;
    private EditText txtGoal;
    private TextView textViewProgress;
    private ProgressBar progressBar;
    private Button btnUpload;
    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    String collectionGoal;
    String collectionName;
    String collectionID;
    String randomInt;

    Uri imageUri;
    boolean isImageAdded=false;

    DatabaseReference Dataref;
    StorageReference StorageRef;






    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        ivAdd=findViewById(R.id.ivAdd);
        txtCollection=findViewById(R.id.txtItem);
        txtGoal=findViewById(R.id.txtGoal);
        textViewProgress=findViewById(R.id.textViewProgress);
        progressBar=findViewById(R.id.progressBar);
        btnUpload=findViewById(R.id.btnUpload);


        textViewProgress.setVisibility(View.GONE);
        progressBar.setVisibility(View.GONE);
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
       Dataref= FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child("CollectionName");
        StorageRef= FirebaseStorage.getInstance().getReference().child(user_id).child("CollectionImage");

        //Code adapted from:
        //Author: Shafaqat Ali - Technical Skillz
        // Link: https://youtu.be/2EhlB4jqb48
        //Imageview button to add collection image
        ivAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(intent,REQUEST_CODE_IMAGE);
            }
        });

        //Button to upload collection to Firebase
        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                 btnUpload.setEnabled(false);
                 collectionName = txtCollection.getText().toString().trim();
                 //method to generate random custom ID for collection
                 generateRandom();
                 collectionID = randomInt;
                 collectionGoal = txtGoal.getText().toString();
              //Collection object created
                CollectionActivity c = new CollectionActivity(collectionID,collectionName,collectionGoal);
                //Validation for user inputs
                if (collectionGoal.isEmpty()||collectionName.isEmpty()) {
                    btnUpload.setEnabled(true);
                    Toast.makeText(CollectionDetails.this, "Please fill in all fields", Toast.LENGTH_SHORT).show();
                }else if (isImageAdded!=false && collectionName!=null)
                {
                    uploadCollection(c);
                }
            }
        });
    }

    //Method to send collection data to firebase
    private void uploadCollection(CollectionActivity collection) {
        textViewProgress.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);



        final  String key=Dataref.push().getKey();
        StorageRef.child(key+".jpg").putFile(imageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                StorageRef.child(key +".jpg").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Data sent to firebase in hashmap format
                        HashMap<String,String> hashMap=new HashMap();
                        hashMap.put("CollectionID",collectionID);
                        hashMap.put("CollectionName",collectionName);
                        hashMap.put("CollectionGoal",collectionGoal);
                        hashMap.put("ImageUrl",uri.toString());





                        Dataref.child(key).setValue(hashMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {
                              ;
                                Intent intent=new Intent(CollectionDetails.this, HomeActivity.class);

                                startActivity(intent);
                                Toast.makeText(CollectionDetails.this, "Data Successfully Uploaded!", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });

            }  // Progress bar to show how far the uploading process is in terms of %
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
    //Method to generate random ID for custom Collection ID
    public String generateRandom() {
        int min = 1000;
        int max = 9000;
        int random = (int)Math.floor(Math.random()*(max-min+1)+min);
        randomInt = Integer.toString(random);


        return randomInt ;
    }
}
