package com.example.rahulraj.red_revolution;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

public class About extends AppCompatActivity {
    WebView view1;
    CheckInternet c1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c1 = new CheckInternet(this);
        boolean check = c1.checkInternet();
        if (!check) {
            Intent intent = new Intent(About.this, NoInternet.class);
            startActivity(intent);
        }
        setContentView(R.layout.about);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        view1 = (WebView) findViewById(R.id.webview2);
        view1.getSettings().setJavaScriptEnabled(true);
        view1.loadUrl("file:///android_asset/done.html");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onResume() {
        super.onResume();
        boolean check = c1.checkInternet();
        if (!check) {
            Intent intent = new Intent(About.this, NoInternet.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            view1.loadUrl("file:///android_asset/done.html");
        }
    }
}
