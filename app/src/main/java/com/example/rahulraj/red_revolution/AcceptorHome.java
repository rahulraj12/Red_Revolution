package com.example.rahulraj.red_revolution;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class AcceptorHome extends AppCompatActivity {
    Button donorList,bloodbank;
    String url = "http://www.rahulraj47.esy.es/donor.php";
    String DonorJSON;
    RequestQueue requestQueue;
    CheckInternet c1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c1 = new CheckInternet(this);
        boolean check = c1.checkInternet();
        if (!check) {
            Intent intent = new Intent(AcceptorHome.this, NoInternet.class);
            startActivity(intent);
        }
        setContentView(R.layout.acceptor_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        requestQueue = Volley.newRequestQueue(this);
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                DonorJSON = s;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(stringRequest);
        donorList = (Button) findViewById(R.id.button3);
        bloodbank= (Button) findViewById(R.id.button5);
        donorList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=c1.checkInternet();
                if (!check) {
                    Intent intent = new Intent(AcceptorHome.this, NoInternet.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(AcceptorHome.this, DonorList.class);
                    intent.putExtra("DonorJSON", DonorJSON);
                    startActivity(intent);
                }
            }
        });
        bloodbank.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(AcceptorHome.this,BloodbankList.class);
                startActivity(intent);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onResume() {
        super.onResume();
        boolean check=c1.checkInternet();
        if (!check) {
            Intent intent = new Intent(AcceptorHome.this, NoInternet.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}
