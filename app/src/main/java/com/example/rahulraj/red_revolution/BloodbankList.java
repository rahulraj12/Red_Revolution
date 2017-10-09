package com.example.rahulraj.red_revolution;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class BloodbankList extends AppCompatActivity {
    Double latitude,longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloodbank_list);

        SharedPreferences mPrefs = getSharedPreferences("IDvalue", 0);
        latitude = Double.parseDouble(mPrefs.getString("Latitude",""));
        longitude = Double.parseDouble(mPrefs.getString("Longitude",""));
    }
}
