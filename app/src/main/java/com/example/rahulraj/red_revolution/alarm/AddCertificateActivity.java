package com.example.rahulraj.red_revolution.alarm;

import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.example.rahulraj.red_revolution.R;

public class AddCertificateActivity extends AppCompatActivity {

    private Context mContext;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        setContentView(R.layout.activity_add_certificate);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int width = displayMetrics.widthPixels;

        LinearLayout ll = (LinearLayout) findViewById(R.id.holder);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(width, width);
        ll.setLayoutParams(lp);
        final EditText img = new EditText(mContext);
        img.setBackgroundResource(R.drawable.blood_img_small);
        img.setGravity(Gravity.CENTER);
        img.setEnabled(false);


        String name = getIntent().getStringExtra("name");
        String dt = getIntent().getStringExtra("date");

        String txt = "<p><b><font color='#FFFFFF'>"+name+"</font></b></p><p><font color='#DDDDDD'>"+dt+"</font></p>";

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            img.setText(Html.fromHtml(txt, Html.FROM_HTML_MODE_LEGACY));
        } else {
            img.setText(Html.fromHtml(txt));
        }
        ll.addView(img);


        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(mContext);
                builder.setItems(new CharSequence[]
                                {"Whatsapp", "Facebook", "Twitter"},
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                img.setDrawingCacheEnabled(true);
                                img.buildDrawingCache(true);
                                Uri uri = IntentShareHelper.getLocalBitmapUri(mContext,img);
                                switch (which) {
                                    case 0:
                                        IntentShareHelper.shareOnWhatsapp(AddCertificateActivity.this,"",uri);
                                        break;
                                    case 1:
                                        IntentShareHelper.shareOnFacebook(AddCertificateActivity.this,"",uri);
                                        break;
                                    case 2:
                                        IntentShareHelper.shareOnTwitter(AddCertificateActivity.this,"",uri);
                                        break;
                                }
                                img.setDrawingCacheEnabled(false);
                                img.buildDrawingCache(false);
                            }
                        });
                builder.create().show();

            }
        });
    }

}
