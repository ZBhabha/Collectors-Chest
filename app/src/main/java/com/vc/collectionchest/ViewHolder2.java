package com.vc.collectionchest;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vc.collectionchest.R;

class ViewHolder2 extends RecyclerView.ViewHolder {
    ImageView imageView,imageView_arrow;
    TextView textView,textView_category;
    View v;


    public ViewHolder2(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.image_single_view_Activity);
        imageView_arrow=itemView.findViewById(R.id.imageView_arrow);
        textView=itemView.findViewById(R.id.textView_single_view_activity);
        textView_category = itemView.findViewById(R.id.textView_category);

        v=itemView;
    }
}