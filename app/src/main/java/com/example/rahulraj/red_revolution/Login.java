package com.example.rahulraj.red_revolution;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class Login extends AppCompatActivity {
    Button signup;
    Double d1,d2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        signup= (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Login.this,SignUp.class);
                startActivity(i);
                if ((ContextCompat.checkSelfPermission(Login.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(Login.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)){
                    ActivityCompat.requestPermissions(Login.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                } else{
                    startNextActivity();
                }

            }
        });

    }
    private void startNextActivity(){
        new com.example.rahulraj.red_revolution.location.LocationCaptureTask(this){
            @Override
            public void afterLocationCapture(Location location) {
                if(location != null){
                    d1 = location.getLatitude();
                    d2 = location.getLongitude();

                } else{
                    Toast.makeText(Login.this,R.string.LocationError,Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
