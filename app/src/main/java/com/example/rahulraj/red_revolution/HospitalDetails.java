package com.example.rahulraj.red_revolution;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.MenuItem;
import android.widget.TextView;

public class HospitalDetails extends AppCompatActivity {
    TextView textView,textView2,textView3,textView4,textView5;
    String h_name,addr,city,state;
    CheckInternet c1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        c1 = new CheckInternet(this);
        boolean check = c1.checkInternet();
        if (!check) {
            Intent intent = new Intent(HospitalDetails.this, NoInternet.class);
            startActivity(intent);
        }

        setContentView(R.layout.hospital_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        textView = (TextView) findViewById(R.id.textView10);
        textView2 = (TextView) findViewById(R.id.textView12);
        textView3 = (TextView) findViewById(R.id.textView15);
        textView4 = (TextView) findViewById(R.id.textView17);
        textView5 = (TextView) findViewById(R.id.textView19);

        Bundle bundle=getIntent().getExtras();
        h_name = bundle.getString("Hospital");
        addr = bundle.getString("Address");
        city = bundle.getString("City");
        state = bundle.getString("State");

        String heading = "DETAILS OF HOSPITAL";

        SpannableString content = new SpannableString(heading);
        content.setSpan(new UnderlineSpan(),0,heading.length(),0);
        textView.setText(content);

        textView2.setText(h_name);
        textView3.setText(addr);
        textView4.setText(city);
        textView5.setText(state);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }
}
