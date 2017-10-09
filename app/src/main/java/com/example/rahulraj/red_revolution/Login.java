package com.example.rahulraj.red_revolution;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Login extends AppCompatActivity {
    Button signup,login;
    Double d1,d2;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    EditText phone , password;
    String Phone;
    ProgressBar progressBar;
    TextView forgot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(Login.this, SignUp.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        setContentView(R.layout.login);
        phone= (EditText) findViewById(R.id.editText);
        password= (EditText) findViewById(R.id.editText2);
        forgot= (TextView) findViewById(R.id.forgot);
        login= (Button) findViewById(R.id.button9);
        signup= (Button) findViewById(R.id.signup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(Login.this,SignUp.class);
                startActivity(i);
            }
        });

        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, SignUp.class);
                startActivity(intent);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Phone=phone.getText().toString().trim();
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                final String Password=password.getText().toString().trim();
               if (!TextUtils.isEmpty(Phone) && !TextUtils.isEmpty(Password)){
                   progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(Phone,Password)
                            .addOnCompleteListener(Login.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressBar.setVisibility(View.GONE);
                                    if (!task.isSuccessful()) {
                                        if (Password.length() < 6) {
                                            password.setError("Password too short, enter minimum 6 characters.");
                                        } else {
                                            Toast.makeText(Login.this, "Authentication failed, please check your email and password..", Toast.LENGTH_LONG).show();
                                        }
                                    }else {
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
                                }
                            });
               }else{
                   Toast.makeText(Login.this, "Please enter email and password..", Toast.LENGTH_SHORT).show();
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
                    Intent intent=new Intent(Login.this,SignUp.class);
                    startActivity(intent);

                } else{
                    Toast.makeText(Login.this,R.string.LocationError,Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }
}
