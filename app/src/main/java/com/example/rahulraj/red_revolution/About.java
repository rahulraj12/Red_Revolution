package com.example.rahulraj.red_revolution;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;

public class About extends AppCompatActivity {
    WebView view1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);
        view1 = (WebView) findViewById(R.id.webview2);
        view1.getSettings().setJavaScriptEnabled(true);
        view1.loadUrl("file:///android_asset/done.html");
    }
}
