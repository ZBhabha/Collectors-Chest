package com.vc.collectionchest;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AboutActivity extends AppCompatActivity {

    private TextView tvAboutHeading;
    private TextView tvAboutInfo;
    private TextView tvAboutConfetti;
    private TextView tvAboutPasswordProtect;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        tvAboutHeading = (TextView) findViewById(R.id.tvAboutHeading);
        tvAboutInfo= (TextView) findViewById(R.id.tvAboutInfo);
        tvAboutConfetti = (TextView) findViewById(R.id.tvAboutConfetti);
        tvAboutPasswordProtect = (TextView) findViewById(R.id.tvAboutPasswordProtect);


    }
}