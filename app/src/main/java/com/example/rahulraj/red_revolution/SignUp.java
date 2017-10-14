package com.example.rahulraj.red_revolution;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUp extends AppCompatActivity {
    FloatingActionButton next;
    EditText name,dob,address,city,district,state,pin,phone,emergency_contact1,emergency_contact2,
            blood_group,height,weight,last_blood_donation,last_platete_donation,email,password;
    String Name,Dob,Address,City,District,State,Pin,Phone,Emergency_contact1,Emergency_contact2,
            Blood_group,Height,Weight,Last_blood_donation,Last_platete_donation,Email,Password,Will_of_donor;
    RadioButton radioButton1,radioButton2;
    FirebaseAuth mAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    boolean hasRegistered;
    SharedPreferences sharedPreferences,sharedPreferences1;
    ProgressBar progressBar;



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
        setContentView(R.layout.sign_up);

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
        //phone = (EditText) findViewById(R.id.editText33);
        emergency_contact1 = (EditText) findViewById(R.id.editText34);
        emergency_contact2 = (EditText) findViewById(R.id.editText35);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        mAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user != null) {
                    sendVerificationEmail();
                }
            }
        };

        sharedPreferences = getSharedPreferences("User_details", 0);
        Email = sharedPreferences.getString("Email", "");
        Password = sharedPreferences.getString("Password", "");

        next = (FloatingActionButton) findViewById(R.id.fab);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
          //      Phone = phone.getText().toString();
                Emergency_contact1 = emergency_contact1.getText().toString().trim();
                Emergency_contact2 = emergency_contact2.getText().toString().trim();


                sharedPreferences1 = getSharedPreferences("User_details", 0);
                SharedPreferences.Editor e1 = sharedPreferences1.edit();
                e1.putString("1", Email);
                e1.commit();
                mAuth.createUserWithEmailAndPassword(Email, Password).addOnCompleteListener(SignUp.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (!task.isSuccessful()) {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SignUp.this, "Sign up unsuccessful", Toast.LENGTH_SHORT).show();
                        } else {
                            progressBar.setVisibility(View.GONE);
                            Toast.makeText(SignUp.this, "Successfully created account", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
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


    private void sendVerificationEmail() {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            // email sent
                            Toast.makeText(SignUp.this, "Verification link sent to your mail", Toast.LENGTH_SHORT).show();
                            // after email is sent just logout the user and finish this activity
                            FirebaseAuth.getInstance().signOut();
                            hasRegistered=true;
                            sharedPreferences = getSharedPreferences("IDvalue", 0);
                            SharedPreferences.Editor e=sharedPreferences.edit();
                            e.putBoolean("hasRegstered",hasRegistered);
                            e.putString("name",Name);
                            e.putString("dob",Dob);
                            e.putString("address",Address);
                            e.putString("city",City);
                            e.putString("district",District);
                            e.putString("state",State);
                            e.putString("pin",Pin);
                            e.putString("blood_group",Blood_group);
                            e.putString("height",Height);
                            e.putString("weight",Weight);
                            e.putString("last_blood_donation",Last_blood_donation);
                            e.putString("last_platelet_donation",Last_platete_donation);
                            e.putString("emergency_contact",Emergency_contact1);
                            e.putString("emergency_contact1",Emergency_contact2);
                            e.putString("email",Email);
                            e.putString("will_of_donor",Will_of_donor);
                            e.commit();
                            Intent intent = new Intent(SignUp.this, Login.class);
                            startActivity(intent);
                            finish();
                        } else {
                            // email not sent, so display message and restart the activity or do whatever you wish to do
                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }
}
