package com.example.rahulraj.red_revolution;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.firebase.auth.FirebaseAuth;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class SignUp extends AppCompatActivity {
    FloatingActionButton next;
    EditText name,dob,address,city,district,state,pin,phone,emergency_contact1,emergency_contact2,
            blood_group,height,weight,last_blood_donation,last_platete_donation;
    String Name,Dob,Address,City,District,State,Pin,Phone,Emergency_contact1,Emergency_contact2,
            Blood_group,Height,Weight,Last_blood_donation,Last_platete_donation,Email,Password,Will_of_donor;
    RadioButton radioButton1,radioButton2;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    SharedPreferences sharedPreferences;
    ProgressBar progressBar;
    CheckInternet c1;
    String url="http://www.rahulraj47.esy.es/userregister.php";
    RequestQueue requestQueue;


    Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    Calendar myCalendar1 = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date1 = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar1.set(Calendar.YEAR, year);
            myCalendar1.set(Calendar.MONTH, monthOfYear);
            myCalendar1.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel1();
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c1 = new CheckInternet(this);
        boolean check = c1.checkInternet();
        if (!check) {
            Intent intent = new Intent(SignUp.this, NoInternet.class);
            startActivity(intent);
        }
        setContentView(R.layout.sign_up);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = (EditText) findViewById(R.id.editText3);
        dob = (EditText) findViewById(R.id.editText4);
        address = (EditText) findViewById(R.id.editText5);
        city = (EditText) findViewById(R.id.editText6);
        district = (EditText) findViewById(R.id.editText7);
        state = (EditText) findViewById(R.id.editText8);
        pin = (EditText) findViewById(R.id.editText9);
        blood_group = (EditText) findViewById(R.id.editText28);
        height = (EditText) findViewById(R.id.editText29);
        weight = (EditText) findViewById(R.id.editText30);
        last_blood_donation = (EditText) findViewById(R.id.editText31);
        last_platete_donation = (EditText) findViewById(R.id.editText32);
        radioButton1 = (RadioButton) findViewById(R.id.radioButton);
        radioButton2 = (RadioButton) findViewById(R.id.radioButton2);
        emergency_contact1 = (EditText) findViewById(R.id.editText34);
        emergency_contact2 = (EditText) findViewById(R.id.editText35);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        sharedPreferences = getSharedPreferences("User_details", 0);
        Email = sharedPreferences.getString("Email", "");
        Password = sharedPreferences.getString("Password", "");

        next = (FloatingActionButton) findViewById(R.id.fab);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=c1.checkInternet();
                if (!check) {
                    Intent intent = new Intent(SignUp.this, NoInternet.class);
                    startActivity(intent);
                } else {
                    progressBar.setVisibility(View.VISIBLE);
                    Name = name.getText().toString().trim();
                    Dob = dob.getText().toString().trim();
                    Address = address.getText().toString().trim();
                    City = city.getText().toString().trim();
                    District = district.getText().toString().trim();
                    State = state.getText().toString().trim();
                    Pin = pin.getText().toString().trim();
                    Blood_group = blood_group.getText().toString().trim();
                    Height = height.getText().toString().trim();
                    Weight = weight.getText().toString().trim();
                    Last_blood_donation = last_blood_donation.getText().toString().trim();
                    Last_platete_donation = last_platete_donation.getText().toString().trim();
                    Emergency_contact1 = emergency_contact1.getText().toString().trim();
                    Emergency_contact2 = emergency_contact2.getText().toString().trim();

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
                            params.put("Emergency_contact", Emergency_contact1);
                            params.put("Emergency_contact1", Emergency_contact2);
                            params.put("Willness_of_donation", Will_of_donor);
                            params.put("Email", Email);
                            return params;
                        }
                    };
                    requestQueue.add(stringRequest);

                }
            }
        });

        last_blood_donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignUp.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
        last_platete_donation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(SignUp.this, date1, myCalendar1
                        .get(Calendar.YEAR), myCalendar1.get(Calendar.MONTH),
                        myCalendar1.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        radioButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Will_of_donor = getResources().getString(R.string.YES);
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Will_of_donor = getResources().getString(R.string.NO);
            }
        });
    }


    public void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthListener);
    }

    public void onStop() {
        super.onStop();
        mAuth.removeAuthStateListener(mAuthListener);
    }

    private void updateLabel() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);

        last_blood_donation.setText(sdf.format(myCalendar.getTime()));
    }

    private void updateLabel1() {

        String myFormat = "MM/dd/yy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.US);


        last_platete_donation.setText(sdf.format(myCalendar1.getTime()));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }


}
