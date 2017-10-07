package com.example.rahulraj.red_revolution;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;

public class SignUp extends AppCompatActivity {
    Button generateOtp;
    EditText phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sign_up);

        phone = (EditText) findViewById(R.id.editText33);
        generateOtp = (Button) findViewById(R.id.generate_otp);
//        PhoneAuthProvider.getInstance().verifyPhoneNumber(
//                phone,
//                60,
//                TimeUnit.SECONDS,
//                this,
//        );
    }
}
