package com.example.rahulraj.red_revolution;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class SignUp extends AppCompatActivity {
    FloatingActionButton next;
    EditText name,dob,address,city,district,state,pin,phone,emergency_contact1,emergency_contact2,
            blood_group,height,weight,last_blood_donation,last_platete_donation,email,password;
    String Name,Dob,Address,City,District,State,Pin,Phone,Emergency_contact1,Emergency_contact2,
            Blood_group,Height,Weight,Last_blood_donation,Last_platete_donation,Email,Password,will_of_donor;
    RadioButton radioButton1,radioButton2;


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
        phone = (EditText) findViewById(R.id.editText33);
        emergency_contact1 = (EditText) findViewById(R.id.editText34);
        emergency_contact2 = (EditText) findViewById(R.id.editText35);
        email = (EditText) findViewById(R.id.editText36);
        password = (EditText) findViewById(R.id.editText37);

        next = (FloatingActionButton) findViewById(R.id.fab);
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Name = name.getText().toString();
                Dob = dob.getText().toString();
                Address = address.getText().toString();
                City = city.getText().toString();
                District = district.getText().toString();
                State = state.getText().toString();
                Pin = pin.getText().toString();
                Blood_group = blood_group.getText().toString();
                Height = height.getText().toString();
                Weight = weight.getText().toString();
                Last_blood_donation = last_blood_donation.getText().toString();
                Last_platete_donation = last_platete_donation.getText().toString();
                Phone = phone.getText().toString();
                Emergency_contact1 = emergency_contact1.getText().toString();
                Emergency_contact2 = emergency_contact2.getText().toString();
                Email = email.getText().toString();
                Password = password.getText().toString();
                Intent intent = new Intent(SignUp.this, OTPverification.class);
                intent.putExtra("name",Name);
                intent.putExtra("dob",Dob);
                intent.putExtra("address",Address);
                intent.putExtra("city",City);
                intent.putExtra("district",District);
                intent.putExtra("state",State);
                intent.putExtra("pin",Pin);
                intent.putExtra("blood_group",Blood_group);
                intent.putExtra("height",Height);
                startActivity(intent);
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
                will_of_donor = getResources().getString(R.string.YES);
            }
        });
        radioButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                will_of_donor = getResources().getString(R.string.NO);
            }
        });
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
}
