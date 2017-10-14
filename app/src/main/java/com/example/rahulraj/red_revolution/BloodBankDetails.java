package com.example.rahulraj.red_revolution;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;

public class BloodBankDetails extends AppCompatActivity {
    TextView textView,textView2,textView3,textView4,textView5,textView6,textView7;
    String h_name,addr,city,state,pincode,contact;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.blood_bank_details);

        textView = (TextView) findViewById(R.id.textView10);
        textView2 = (TextView) findViewById(R.id.textView12);
        textView3 = (TextView) findViewById(R.id.textView15);
        textView4 = (TextView) findViewById(R.id.textView17);
        textView5 = (TextView) findViewById(R.id.textView19);
        textView6 = (TextView) findViewById(R.id.textView21);
        textView7 = (TextView) findViewById(R.id.textView24);

        Bundle bundle=getIntent().getExtras();
        h_name = bundle.getString("Hospital");
        addr = bundle.getString("Address");
        city = bundle.getString("City");
        state = bundle.getString("State");
        pincode = bundle.getString("Pincode");
        contact = bundle.getString("Contact");

        String heading = "DETAILS OF BLOOD BANK";

        SpannableString content = new SpannableString(heading);
        content.setSpan(new UnderlineSpan(),0,heading.length(),0);
        textView.setText(content);

        textView2.setText(h_name);
        textView3.setText(addr);
        textView4.setText(city);
        textView5.setText(state);
        textView6.setText(pincode);

        SpannableString content1 = new SpannableString(contact);
        content1.setSpan(new UnderlineSpan(),0,contact.length(),0);
        textView7.setText(content1);
        textView7.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent phoneIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts(
                        "tel", contact, null));
                startActivity(phoneIntent);
            }
        });
    }
}
