package com.vc.collectionchest;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuView;
import androidx.recyclerview.widget.RecyclerView;

import com.vc.collectionchest.R;

class ViewHolder3 extends RecyclerView.ViewHolder {

        TextView name,description,date,category;
        View v;
        ImageView btnDelete;


        public ViewHolder3(@NonNull View itemView) {
            super(itemView);
            name=itemView.findViewById(R.id.textView_name);
            description = itemView.findViewById(R.id.textView_description);
            date = itemView.findViewById(R.id.textView_date);
            btnDelete = itemView.findViewById(R.id.btnDelete);


            v=itemView;
        }
    }

