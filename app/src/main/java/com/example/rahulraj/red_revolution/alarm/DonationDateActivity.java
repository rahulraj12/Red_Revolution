package com.example.rahulraj.red_revolution.alarm;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import com.example.rahulraj.red_revolution.R;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DonationDateActivity extends AppCompatActivity {
    DatePicker picker;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donation_date);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        SharedPreferences sp = getSharedPreferences("IDvalue",0);
        String name = sp.getString("name","");
        if("".equals(name)){
            findViewById(R.id.name).setVisibility(View.VISIBLE);
        }
        picker = ((DatePicker)findViewById(R.id.datePicker));

        picker.setMaxDate(new Date().getTime());


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String nm = ((EditText)findViewById(R.id.name)).getText().toString();
                if("".equals(nm)){
                    Toast.makeText(DonationDateActivity.this,getResources().getString(R.string.invalid_name),Toast.LENGTH_LONG).show();
                    return;
                }


                SimpleDateFormat dateformat = new SimpleDateFormat("d MMM''yy");

                String dt = dateformat.format(new Date(picker.getYear(), picker.getMonth(), picker.getDayOfMonth()));

                Intent intent = new Intent();
                intent.setClass(DonationDateActivity.this,AddCertificateActivity.class);
                intent.putExtra("date",dt);
                intent.putExtra("name",nm);
                startActivity(intent);
            }
        });
    }

}
