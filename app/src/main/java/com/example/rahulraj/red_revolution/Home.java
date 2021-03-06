package com.example.rahulraj.red_revolution;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.rahulraj.red_revolution.alarm.AlarmReceiver;
import com.example.rahulraj.red_revolution.alarm.DonationDateActivity;
import com.example.rahulraj.red_revolution.alarm.MyService;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class Home extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    Button donor,acceptor,emergency;
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    CheckInternet c1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c1 = new CheckInternet(this);
        boolean check = c1.checkInternet();
        if (!check) {
            Intent intent = new Intent(Home.this, NoInternet.class);
            startActivity(intent);
        }
        setContentView(R.layout.home);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        auth = FirebaseAuth.getInstance();
        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    startActivity(new Intent(Home.this, Login.class));
                    finish();
                }
            }
        };

        donor = (Button) findViewById(R.id.donor);
        acceptor= (Button) findViewById(R.id.acceptor);
        emergency= (Button) findViewById(R.id.emergency);
        donor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ConnectivityManager cm = (ConnectivityManager) getSystemService(CONNECTIVITY_SERVICE);
                NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
                boolean isConnected = activeNetwork != null &&
                        activeNetwork.isConnectedOrConnecting();

                if (!isConnected) {
                    Toast.makeText(Home.this, R.string.InternetError, Toast.LENGTH_SHORT).show();
                    Intent i=new Intent(Home.this,NoInternet.class);
                    startActivity(i); //to do back button handling

                } else {
                    Intent intent = new Intent(Home.this, BloodbankList.class);
                    startActivity(intent);
                }
            }
        });

        acceptor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,AcceptorHome.class);
                startActivity(intent);
            }
        });
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(Home.this,EmergencyHome.class);
                startActivity(intent);
            }
        });

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        setAlarm(this);
        Intent i = new Intent(this, MyService.class);
        startService(i);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.home, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.certs) {
            Intent intent = new Intent(Home.this, DonationDateActivity.class);
            startActivity(intent);
            // Handle the camera action
        } else if (id == R.id.faq) {
            Intent intent=new Intent(Home.this,FAQ.class);
            startActivity(intent);

        } else if (id == R.id.about) {
            Intent intent1=new Intent(Home.this,About.class);
            startActivity(intent1);

        } else if (id == R.id.credits) {

        } else if (id == R.id.phoneverification) {
            Intent intent = new Intent(Home.this, Authentication.class);
            startActivity(intent);
        } else if (id == R.id.updatepassword) {
            Intent intent = new Intent(Home.this, UpdatePassword.class);
            startActivity(intent);
        } else if (id == R.id.logout) {
                auth.signOut();
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public static void setAlarm(Context context) {
        try {
            Intent intent = new Intent();
            Log.d(TAG, "Setting alarm");
            intent.setClass(context, AlarmReceiver.class);
            intent.putExtra("alarm_msg", "SHOW_NOTIFICATION");
            PendingIntent sender = PendingIntent.getBroadcast(context, 123456, intent,
                    PendingIntent.FLAG_CANCEL_CURRENT);

            AlarmManager am = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);

            long alarmInterval = AlarmManager.INTERVAL_HOUR;
            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.MINUTE, 0);
            calendar.add(Calendar.HOUR_OF_DAY, 1);
            calendar.set(Calendar.SECOND, 0);
            SimpleDateFormat format = new SimpleDateFormat("dd-MMM-yy HH:mm:ss");
            Log.d(TAG, "next alarmtime=" + format.format(new Date(calendar.getTimeInMillis())));
            am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), alarmInterval, sender);

        } catch (Exception e) {
            e.printStackTrace();
        }

    }
    private static final String TAG = Home.class.getSimpleName();

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }
}
