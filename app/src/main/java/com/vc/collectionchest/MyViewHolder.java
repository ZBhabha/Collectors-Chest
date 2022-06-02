package com.vc.collectionchest;

import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.vc.collectionchest.R;

class MyViewHolder extends RecyclerView.ViewHolder {
    ImageView imageView;
    TextView textView,goalsView,txtGoal;
    View v;

    public MyViewHolder(@NonNull View itemView) {
        super(itemView);
        imageView=itemView.findViewById(R.id.image_single_view);

        textView=itemView.findViewById(R.id.textView_single_view);
        goalsView = itemView.findViewById(R.id.goals_single_view);

        txtGoal=itemView.findViewById(R.id.txt_goal);

        v=itemView;
    }
}

