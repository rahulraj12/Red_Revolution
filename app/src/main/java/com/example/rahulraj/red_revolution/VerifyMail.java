package com.example.rahulraj.red_revolution;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class VerifyMail extends AppCompatActivity {
    FirebaseUser firebaseUser;
    FirebaseAuth mAuth;
    Button button;
    String Email, Password;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.verify_mail);

        sharedPreferences = getSharedPreferences("User_details", 0);
        mAuth = FirebaseAuth.getInstance();
        Email = sharedPreferences.getString("Email", "");
        Password = sharedPreferences.getString("Password", "");

        button = (Button) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signInWithEmailAndPassword(Email, Password).addOnCompleteListener(VerifyMail.this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            firebaseUser = mAuth.getCurrentUser();
                            if (firebaseUser.isEmailVerified()) {
                                Toast.makeText(VerifyMail.this, "Email verified successfully!", Toast.LENGTH_SHORT).show();
                                SharedPreferences s1 = getSharedPreferences("User_details", 0);
                                SharedPreferences.Editor e1 = s1.edit();
                                e1.putString("1", "null");
                                e1.commit();
                                mAuth.signOut();
                                Intent intent = new Intent(VerifyMail.this, SignUp.class);
                                startActivity(intent);
                            } else
                                Toast.makeText(VerifyMail.this, "Email not verified", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
    }
}
