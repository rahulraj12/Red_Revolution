package com.example.rahulraj.red_revolution;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class UpdatePassword extends AppCompatActivity {
    FirebaseAuth auth;
    ProgressBar progressBar;
    FirebaseAuth.AuthStateListener authStateListener;
    Button updatePassword;
    EditText newPassword,cnfPassword,oldPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_password);
        auth = FirebaseAuth.getInstance();

        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(UpdatePassword.this, Login.class));
                    finish();
                }
            }
        };

        updatePassword= (Button) findViewById(R.id.button5);

        newPassword= (EditText) findViewById(R.id.editText10);
        cnfPassword= (EditText) findViewById(R.id.editText11);
        oldPassword= (EditText) findViewById(R.id.editText12);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                updatePassword.setEnabled(false);
                newPassword.setEnabled(false);
                oldPassword.setEnabled(false);
                cnfPassword.setEnabled(false);
                if (user != null && !newPassword.getText().toString().trim().equals("") && !oldPassword.getText().toString().trim().equals("") && newPassword.getText().toString().trim().equals(cnfPassword.getText().toString().trim()) && !oldPassword.getText().toString().trim().equals(newPassword.getText().toString().trim())) {
                    if (newPassword.getText().toString().trim().length() < 6) {
                        newPassword.setError("Password too short, enter minimum 6 characters");
                        progressBar.setVisibility(View.GONE);
                    } else {
                        AuthCredential credential = EmailAuthProvider
                                .getCredential(user.getEmail(), oldPassword.getText().toString().trim());
                        user.reauthenticate(credential)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            user.updatePassword(newPassword.getText().toString().trim())
                                                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<Void> task) {
                                                            if (task.isSuccessful()) {
                                                                Toast.makeText(UpdatePassword.this, "Password is updated, sign in with new password!", Toast.LENGTH_SHORT).show();
                                                                auth.signOut();
                                                                progressBar.setVisibility(View.GONE);
                                                            } else {
                                                                Toast.makeText(UpdatePassword.this, "Failed to update password!", Toast.LENGTH_SHORT).show();
                                                                progressBar.setVisibility(View.GONE);
                                                                updatePassword.setEnabled(true);
                                                                newPassword.setEnabled(true);
                                                                oldPassword.setEnabled(true);
                                                                cnfPassword.setEnabled(true);
                                                            }
                                                        }
                                                    });
                                        } else {
                                            oldPassword.setError("Enter old password");
                                            progressBar.setVisibility(View.GONE);
                                            updatePassword.setEnabled(true);
                                            newPassword.setEnabled(true);
                                            oldPassword.setEnabled(true);
                                            cnfPassword.setEnabled(true);
                                        }
                                    }
                                });
                    }
                } else if (oldPassword.getText().toString().trim().equals("")) {
                    oldPassword.setError("Enter password");
                    progressBar.setVisibility(View.GONE);
                    updatePassword.setEnabled(true);
                    newPassword.setEnabled(true);
                    oldPassword.setEnabled(true);
                    cnfPassword.setEnabled(true);
                } else if (newPassword.getText().toString().trim().equals("")) {
                    newPassword.setError("Enter password");
                    progressBar.setVisibility(View.GONE);
                    updatePassword.setEnabled(true);
                    newPassword.setEnabled(true);
                    oldPassword.setEnabled(true);
                    cnfPassword.setEnabled(true);
                } else if (oldPassword.getText().toString().trim().equals(newPassword.getText().toString().trim())) {
                    newPassword.setError("New password cannot match old password");
                    progressBar.setVisibility(View.GONE);
                    updatePassword.setEnabled(true);
                    newPassword.setEnabled(true);
                    oldPassword.setEnabled(true);
                    cnfPassword.setEnabled(true);
                } else {
                    cnfPassword.setError("Passwords do not match");
                    progressBar.setVisibility(View.GONE);
                    updatePassword.setEnabled(true);
                    newPassword.setEnabled(true);
                    oldPassword.setEnabled(true);
                    cnfPassword.setEnabled(true);
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authStateListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authStateListener != null) {
            auth.removeAuthStateListener(authStateListener);
        }
    }
}
