package com.example.rahulraj.red_revolution;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;

public class FAQ extends AppCompatActivity {
    WebView view;
    CheckInternet c1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        c1 = new CheckInternet(this);
        boolean check = c1.checkInternet();
        if (!check) {
            Intent intent = new Intent(FAQ.this, NoInternet.class);
            startActivity(intent);
        }
        setContentView(R.layout.faq);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        view = (WebView) findViewById(R.id.webview);
        view.getSettings().setJavaScriptEnabled(true);
        view.loadUrl("file:///android_asset/abc.html");
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }


}
