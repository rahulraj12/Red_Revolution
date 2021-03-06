package com.example.rahulraj.red_revolution;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {
    FirebaseAuth auth;
    ProgressBar progressBar;
    CheckInternet c1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c1 = new CheckInternet(this);
        boolean check = c1.checkInternet();
        if (!check) {
            Intent intent = new Intent(ForgotPassword.this, NoInternet.class);
            startActivity(intent);
        }
        setContentView(R.layout.forgot_password);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        final EditText email = (EditText) findViewById(R.id.editText);
        Button reset = (Button) findViewById(R.id.reset);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        auth = FirebaseAuth.getInstance();
        reset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=c1.checkInternet();
                if (!check) {
                    Intent intent = new Intent(ForgotPassword.this, NoInternet.class);
                    startActivity(intent);
                } else {
                    String Email = email.getText().toString();
                    if (!TextUtils.isEmpty(Email)) {
                        progressBar.setVisibility(View.VISIBLE);
                        auth.sendPasswordResetEmail(Email)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(ForgotPassword.this, "We have sent you instructions to update your password!", Toast.LENGTH_SHORT).show();
                                        } else {
                                            Toast.makeText(ForgotPassword.this, "Failed to send update email!", Toast.LENGTH_SHORT).show();
                                        }
                                        progressBar.setVisibility(View.GONE);
                                    }
                                });
                    } else {
                        email.setError("Enter your email");
                    }
                }

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
