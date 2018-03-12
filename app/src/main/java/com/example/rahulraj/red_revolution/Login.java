package com.example.rahulraj.red_revolution;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
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
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.rahulraj.red_revolution.location.LocationCaptureTask;
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
import java.util.Locale;

public class Login extends AppCompatActivity {
    Button signup, login, emergency;
    EditText email, password;
    Double d1, d2;
    String lat, lng, url = "https://data.gov.in/node/3287321/datastore/export/json", JSON, url1 = "http://www.rahulraj47.esy.es/donor.php";
    String Email;
    FirebaseAuth firebaseAuth;
    RadioButton radioButton, radioButton1;
    ProgressBar progressBar;
    TextView forgot;
    FirebaseAuth.AuthStateListener authStateListener;
    private static final int MY_PERMISSION_REQUEST_SEND_SMS = 10;
    CheckInternet c1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c1 = new CheckInternet(this);
        boolean check = c1.checkInternet();
        if (!check) {
            Intent intent = new Intent(Login.this, NoInternet.class);
            startActivity(intent);
        }
        firebaseAuth = FirebaseAuth.getInstance();

        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if (firebaseAuth.getCurrentUser() != null) {
                    startNextActivity();
                }
            }
        };

        setContentView(R.layout.login);

        signup = (Button) findViewById(R.id.signup);
        login = (Button) findViewById(R.id.button9);
        email = (EditText) findViewById(R.id.editText);
        password = (EditText) findViewById(R.id.editText2);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        radioButton = (RadioButton) findViewById(R.id.radioButton3);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton4);
        forgot = (TextView) findViewById(R.id.forgot);
        radioButton1.setChecked(true);
        managepermission();

        getJSON();

        radioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Configuration config = new Configuration();
                Locale locale = new Locale("hi");
                config.locale = locale;
                getResources().updateConfiguration(config, null);
            }
        });

        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Configuration config = new Configuration();
                Locale locale = new Locale("en");
                config.locale = locale;
                getResources().updateConfiguration(config, null);
            }
        });
        forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Login.this, ForgotPassword.class);
                startActivity(intent);
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = c1.checkInternet();
                if (!check) {
                    Intent intent = new Intent(Login.this, NoInternet.class);
                    startActivity(intent);
                } else {
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
                                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                    @Override
                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                        progressBar.setVisibility(View.GONE);
                                        if (!task.isSuccessful()) {
                                            if (Password.length() < 6) {
                                                password.setError("Password too short, enter minimum 6 characters.");
                                            } else {
                                                Toast.makeText(Login.this, R.string.auth, Toast.LENGTH_LONG).show();
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

                    }
                }
            }
        });

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check = c1.checkInternet();
                if (!check) {
                    Intent intent = new Intent(Login.this, NoInternet.class);
                    startActivity(intent);
                } else {
                    SharedPreferences mPrefs = getSharedPreferences("User_details", 0);
                    String c = mPrefs.getString("1", "");
                    if (!"null".equalsIgnoreCase(c.trim()) && !TextUtils.isEmpty(c.trim())) {
                        AlertDialog.Builder dialogue = new AlertDialog.Builder(Login.this);
                        dialogue.setCancelable(false);
                        dialogue.setTitle("Alert Dialog");
                        dialogue.setMessage("Do you want to verify your email entered previously?");
                        dialogue.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(Login.this, VerifyMail.class);
                                startActivity(intent);
                            }
                        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                SharedPreferences sharedPreferences2 = getSharedPreferences("User_details", 0);
                                SharedPreferences.Editor e1 = sharedPreferences2.edit();
                                e1.putString("1", "null");
                                e1.commit();
                                Intent intent = new Intent(Login.this, Register.class);
                                startActivity(intent);
                            }
                        });
                        AlertDialog alertDialog = dialogue.create();
                        alertDialog.show();
                    } else {
                        Intent intent = new Intent(Login.this, Register.class);
                        startActivity(intent);
                    }
                }

            }
        });

        emergency= (Button) findViewById(R.id.emergency);
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ((ContextCompat.checkSelfPermission(Login.this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(Login.this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED)) {
                    ActivityCompat.requestPermissions(Login.this, new String[]{android.Manifest.permission.ACCESS_COARSE_LOCATION,
                            android.Manifest.permission.ACCESS_FINE_LOCATION}, 100);
                } else {
                    startNextActivity1();
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
                JSON = s;
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
                    editor.putString("Email", email.getText().toString().trim());
                    editor.commit();
                    Intent intent = new Intent(Login.this, Home.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, R.string.LocationError, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    private void startNextActivity1() {
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
                    editor.putString("Email", email.getText().toString().trim());
                    editor.commit();
                    Intent intent = new Intent(Login.this, BloodbankList.class);
                    startActivity(intent);
                } else {
                    Toast.makeText(Login.this, R.string.LocationError, Toast.LENGTH_SHORT).show();
                }
            }
        }.execute();
    }

    @Override
    public void onStart() {
        super.onStart();
        firebaseAuth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            firebaseAuth.removeAuthStateListener(authStateListener);
        }
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
