package com.example.rahulraj.red_revolution;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class Login extends AppCompatActivity {
    String lat, lng, url = "https://data.gov.in/node/356981/datastore/export/json", JSON;
    Button signup, login;
    Double d1, d2;
    FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener authStateListener;
    EditText email, password;
    String Email;
    ProgressBar progressBar;
    TextView forgot;
    private static final int MY_PERMISSION_REQUEST_SEND_SMS = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                    finish();
                }
            }
        };
        setContentView(R.layout.login);
        email = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        forgot = (TextView) findViewById(R.id.forgot);
        login = (Button) findViewById(R.id.button9);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        signup = (Button) findViewById(R.id.signup);
        managepermission();

        getJSON();

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
                Email = email.getText().toString().trim();
                View view = getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
                final String Password = password.getText().toString().trim();
                if (!TextUtils.isEmpty(Email) && !TextUtils.isEmpty(Password)) {
                    progressBar.setVisibility(View.VISIBLE);
                    firebaseAuth.signInWithEmailAndPassword(Email, Password)
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
                                    } else {
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
                                }
                            });
                } else {
                    Toast.makeText(Login.this, "Please enter email and password..", Toast.LENGTH_SHORT).show();
                }
            }
        });


        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences s1 = getSharedPreferences("User_details", 0);
                String c = s1.getString("1", "");
                if (!"null".equalsIgnoreCase(c.trim()) && !TextUtils.isEmpty(c.trim())) {
                    AlertDialog.Builder dialogue = new AlertDialog.Builder(Login.this);
                    dialogue.setCancelable(false);
                    dialogue.setTitle("Alert Dialog");
                    dialogue.setMessage("Do you want to verify your email entered previously?");
                    dialogue.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Intent intent = new Intent(Login.this, OTPverification.class);
                            startActivity(intent);
                        }
                    }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            SharedPreferences sharedPreferences2 = getSharedPreferences("User_details", 0);
                            SharedPreferences.Editor e1 = sharedPreferences2.edit();
                            e1.putString("1", "null");
                            e1.commit();
                            Intent intent = new Intent(Login.this, SignUp.class);
                            startActivity(intent);
                        }
                    });
                    AlertDialog alertDialog = dialogue.create();
                    alertDialog.show();
                } else {
                    Intent intent = new Intent(Login.this, SignUp.class);
                    startActivity(intent);
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
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, R.string.LocationError, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == 100) {
            if (grantResults.length == 2 && (grantResults[0] == PackageManager.PERMISSION_GRANTED || grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                startNextActivity();
            } else {
                String message = getResources().getString(R.string.LocationRequest);

                AlertDialog alertDialog = new AlertDialog.Builder(Login.this).create();
                alertDialog.setMessage(message);
                alertDialog.setButton(DialogInterface.BUTTON_POSITIVE, getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent intent = new Intent(android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                        Uri uri = Uri.fromParts("package", getPackageName(), null);
                        intent.setData(uri);
                    }
                });
                alertDialog.show();
            }
        }

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_SEND_SMS:
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                } else {
                    if (ActivityCompat.shouldShowRequestPermissionRationale(this, android.Manifest.permission.SEND_SMS)) {
                        new android.app.AlertDialog.Builder(this)
                                .setTitle(getResources().getString(R.string.SMSPermission))
                                .setMessage(getResources().getString(R.string.NeedSMSPermission)).show();
                    } else {
                        new android.app.AlertDialog.Builder(this)
                                .setTitle(getResources().getString(R.string.PermissionDeniedTitle))
                                .setMessage(getResources().getString(R.string.PermissionDeniedMessage)).show();
                    }
                }


                break;
        }
    }


    private void managepermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.SEND_SMS)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.SEND_SMS}, MY_PERMISSION_REQUEST_SEND_SMS);

        } else {
            sendsms();
        }
    }

    private void sendsms() {
        // Toast.makeText(this, "Send sms feature call", Toast.LENGTH_SHORT).show();
    }
}
