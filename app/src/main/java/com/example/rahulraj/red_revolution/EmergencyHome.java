package com.example.rahulraj.red_revolution;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

public class EmergencyHome extends AppCompatActivity {
    Button bloodBank,hospital;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.emergency_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        bloodBank= (Button) findViewById(R.id.bloodbank);
        hospital= (Button) findViewById(R.id.hospital);

        hospital.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmergencyHome.this,Hospital.class);
                startActivity(intent);
            }
        });
        bloodBank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(EmergencyHome.this,BloodbankList.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
