package com.example.rahulraj.red_revolution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class Login extends AppCompatActivity {
    Button signup, login;
    Double d1, d2;
    RequestQueue requestQueue;
    String lat, lng, url = "https://data.gov.in/node/356981/datastore/export/json",JSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        requestQueue = Volley.newRequestQueue(this);
        signup = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.button9);

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, Home.class);
                startActivity(intent);
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, SignUp.class);
                startActivity(i);
                if ((ContextCompat.checkSelfPermission(Login.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(Login.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(Login.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                } else {
                    startNextActivity();
                }

            }
        });

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                JSON=s;
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        requestQueue.add(stringRequest);
    }

    private void startNextActivity() {
        new com.example.rahulraj.red_revolution.location.LocationCaptureTask(this) {
            @Override
            public void afterLocationCapture(Location location) {
                if (location != null) {
                    d1 = location.getLatitude();
                    d2 = location.getLongitude();
                    lat = Double.toString(d1);
                    lng = Double.toString(d2);
                    SharedPreferences mPrefs = getSharedPreferences("IDvalue", 0);
                    SharedPreferences.Editor editor = mPrefs.edit();
                    editor.putString("Latitude", lat);
                    editor.putString("Longitude", lng);
                    editor.putString("JSON", JSON);
                    editor.commit();

                } else {
                    Toast.makeText(Login.this, R.string.LocationError, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
