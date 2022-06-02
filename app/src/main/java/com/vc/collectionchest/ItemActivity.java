package com.vc.collectionchest;

import com.google.firebase.auth.FirebaseUser;

import java.util.Date;

public class ItemActivity {
    //Variables declared
    String ItemName;
    String ItemDescription;
    String ItemDate;
    String ItemCategory;
    String ImageUrl;
//Constructors created
    public ItemActivity() {
    }

    public ItemActivity(String itemName, String itemDescription, String itemDate, String itemCategory) {
        ItemName = itemName;
        ItemDescription=itemDescription;
        ItemDate = itemDate;
        ItemCategory = itemCategory;
    }
//Getters and setters
    public String getItemName() {
        return ItemName;
    }

    public void setItemName(String itemName) {
        ItemName = itemName;
    }

    public String getItemDescription() {
        return ItemDescription;
    }

    public void setItemDescription(String itemDescription) {
        ItemDescription = itemDescription;
    }

    public String getItemDate() {
        return ItemDate;
    }

    public void setItemDate(String itemDate) {
        ItemDate = itemDate;
    }

    public String getItemCategory() {
        return ItemCategory;
    }

    public void setItemCategory(String itemCategory) {
        ItemCategory = itemCategory;
    }

    public String getImageUrl() {
        return ImageUrl;
    }

    public void setImageUrl(String imageUrl) {
        ImageUrl = imageUrl;
    }




}


