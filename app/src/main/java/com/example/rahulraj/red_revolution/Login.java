package com.example.rahulraj.red_revolution;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.rahulraj.red_revolution.location.LocationCaptureTask;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends AppCompatActivity {
    Button signup, login;
    EditText email;
    Double d1, d2;
    String lat, lng, url = "https://data.gov.in/node/3287321/datastore/export/json",JSON;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        signup = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.button9);
        email= (EditText) findViewById(R.id.editText);

        getJSON();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startNextActivity();
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
    }

    private void getJSON() {
        class GetUrls extends AsyncTask<String, Void, String> {

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
            }

            @Override
            protected String doInBackground(String... params) {
                String uri = params[0];
                StringBuilder sb = new StringBuilder();
                BufferedReader bufferedReader = null;
                try {
                    URL url = new URL(uri);
                    HttpURLConnection con = (HttpURLConnection) url.openConnection();

                    bufferedReader = new BufferedReader(new InputStreamReader(con.getInputStream()));
                    String json;
                    while ((json = bufferedReader.readLine()) != null) {
                        sb.append(json + "\n");
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return sb.toString().trim();
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                JSON=s;
            }
        }
        GetUrls g = new GetUrls();
        g.execute(url);
    }

    private void startNextActivity() {
        new LocationCaptureTask(this) {
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
                    editor.putString("Email",email.getText().toString());
                    editor.commit();
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, R.string.LocationError, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
