package com.example.rahulraj.red_revolution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.HashMap;
import java.util.Map;

public class OTP extends AppCompatActivity {

    String Name, Dob, Address, City, District, State, Pin, Phone, Emergency_contact, Emergency_contact1,
            Blood_group, Height, Weight, Last_blood_donation, Last_platete_donation, Email, Password, Will_of_donor;
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    RequestQueue requestQueue;
    Button verify;
    String url = "http://www.rahulraj47.esy.es/userregister.php";

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.otp);

        mAuth = FirebaseAuth.getInstance();
        SharedPreferences s = getSharedPreferences("IDvalue", 0);
        Name=s.getString("name","");
        Dob=s.getString("dob","");
        Address=s.getString("address","");
        City=s.getString("city","");
        District=s.getString("district","");
        State=s.getString("state","");
        Pin=s.getString("pin","");
        Emergency_contact=s.getString("emergency_contact","");
        Emergency_contact1=s.getString("emergency_contact1","");
        Blood_group=s.getString("blood_group","");
        Height=s.getString("height","");
        Weight=s.getString("weight","");
        Last_blood_donation=s.getString("last_blood_donation","");
        Last_platete_donation=s.getString("last_platelet_donation","");
        Email=s.getString("email","");
        Password=s.getString("password","");
        Will_of_donor=s.getString("will_of_donor","");
        requestQueue = Volley.newRequestQueue(this);

        verify = (Button) findViewById(R.id.verify);
        verify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(OTP.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser.isEmailVerified()) {
                                Toast.makeText(OTP.this, "Email verified successfully!", Toast.LENGTH_SHORT).show();
                                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String s) {
                                    }
                                }, new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError volleyError) {
                                    }
                                }) {
                                    @Override
                                    protected Map<String, String> getParams() throws AuthFailureError {
                                        Map<String, String> params = new HashMap<String, String>();
                                        params.put("Name", Name);
                                        params.put("Dob", Dob);
                                        params.put("Address", Address);
                                        params.put("City", City);
                                        params.put("District", District);
                                        params.put("State", State);
                                        params.put("Pin", Pin);
                                        params.put("Blood_group", Blood_group);
                                        params.put("Height", Height);
                                        params.put("Weight", Weight);
                                        params.put("Last_blood_donation", Last_blood_donation);
                                        params.put("Last_platelet_donation", Last_platete_donation);
                                        params.put("Emergency_contact", Emergency_contact);
                                        params.put("Emergency_contact1", Emergency_contact1);
                                        params.put("Willness_of_donation", Will_of_donor);
                                        params.put("Email", Email);
                                        params.put("Password", Password);
                                        return params;
                                    }
                                };
                                requestQueue.add(stringRequest);
                                mAuth.signOut();
                                SharedPreferences s1 = getSharedPreferences("User_details", 0);
                                SharedPreferences.Editor e1 = s1.edit();
                                e1.putString("1", "null");
                                e1.commit();
                                Intent intent = new Intent(OTP.this, Login.class);
                                startActivity(intent);
                            } else
                                Toast.makeText(OTP.this, "Email not verified", Toast.LENGTH_SHORT).show();
                        }
                    }
                });


            }
        });


    }
}

