package com.vc.collectionchest;


import androidx.annotation.NonNull;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class CollectionActivity {
    //Variables declared
    String CollectionID;
    String CollectionName;
    String  CollectionGoal;
    String ImageUrl;






    //Constructor created
    public CollectionActivity(String collectionID, String collectionName, String  collectionGoal) {
        CollectionID = collectionID;
        CollectionName = collectionName;
        CollectionGoal = collectionGoal;
    }
    //Empty constructor
    public CollectionActivity() {
    }



    //Getters and setters
    public String getCollectionID() {
        return CollectionID;
    }

    public void setCollectionID(String collectionID) {
        CollectionID = collectionID;
    }


    public String getCollectionName() {
        return CollectionName;
    }

    public void setCollectionName(String collectionName) {
        CollectionName = collectionName;
    }

    public String getCollectionGoal() {
        return CollectionGoal;
    }

    public void setCollectionGoal(String collectionGoal) {
        CollectionGoal = collectionGoal;
    }


    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }

}
