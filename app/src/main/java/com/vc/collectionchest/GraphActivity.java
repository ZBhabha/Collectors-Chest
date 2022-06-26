package com.vc.collectionchest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {
    DatabaseReference Dataref;
    ArrayList barArraylist,collectionList;
    ArrayList<String> labels = new ArrayList<String>();
    String user_id = FirebaseAuth.getInstance().getCurrentUser().getUid();
    int count;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        BarChart barChart = findViewById(R.id.barchart);
        Dataref = FirebaseDatabase.getInstance().getReference().child("users").child(user_id).child("CollectionName").child(HelperClass.key);
       // getData();
        barArraylist = new ArrayList();

        barArraylist.add(new BarEntry(1f,(float) HelperClass.count));

        BarDataSet barDataSet = new BarDataSet(barArraylist,HelperClass.category);
       // BarDataSet barDataSett = new BarDataSet(collectionList,"Collectors Chest");
        BarData barData = new BarData( barDataSet);
//////
        XAxis xAxis = barChart.getXAxis();

        //////
        barChart.setData(barData);
        //color bar data set
        barDataSet.setColors(ColorTemplate.JOYFUL_COLORS);
        //text color
        barDataSet.setValueTextColor(Color.BLACK);
        //setting text size
        barDataSet.setValueTextSize(25f);
        barData.setBarWidth(0.4f);
        barChart.getAxisRight().setAxisMinValue(0f);
        barChart.getAxisRight().setAxisMaxValue(100f);
        barChart.getAxisLeft().setAxisMinValue(0f);

        barChart.getAxisLeft().setAxisMaxValue(100f);

        barChart.getDescription().setEnabled(false);
        barChart.animateY(2000);




    }

    private void getData() {
        collectionList = new ArrayList<>();
        collectionList.add(("hh"));

        barArraylist = new ArrayList();
        barArraylist.add(new BarEntry(6f, (float)HelperClass.count));

    }


}


/* private void retrieveData() {
        Dataref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if (dataSnapshot.hasChildren()){
                    for (DataSnapshot myDataSnapshot:dataSnapshot.getChildren()){
                        DataPoint dataPoint
                    }

                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }*/

