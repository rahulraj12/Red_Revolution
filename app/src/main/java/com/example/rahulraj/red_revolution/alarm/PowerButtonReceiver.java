package com.example.rahulraj.red_revolution.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.util.Log;


public class PowerButtonReceiver extends BroadcastReceiver {
    private static final String TAG = PowerButtonReceiver.class.getSimpleName();
    private static int powerCount;
    private static Handler handler = null;

    private void sendSms(Context context) {
        Log.d(TAG,"Inside sendSMS");
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "Inside Receiver=" + intent.getAction());
        if (intent.getAction().equals(Intent.ACTION_SCREEN_ON)) {
            if (handler == null) {
                Log.d(TAG, "first time handler");
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler = null;
                        powerCount = 0;
                        Log.d(TAG, "make handler null and power to 0");
                    }
                }, 6000);
            }
            if (handler != null) {
                powerCount++;
                if (powerCount >= 6) {
                    powerCount = 0;
                    Log.d(TAG, "pressed 6 times run the code");
                    sendSms(context);
                }
                Log.d(TAG, "power count=" + powerCount);
            }
            Log.d(TAG, "Screen ON");
        } else if (intent.getAction().equals(Intent.ACTION_SCREEN_OFF)) {
            Log.d(TAG, "Screen OFF");
            if (handler == null) {
                Log.d(TAG, "first time handler");
                handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        handler = null;
                        powerCount = 0;
                        Log.d(TAG, "make handler null and power to 0");
                    }
                }, 6000);
            }
            if (handler != null) {
                powerCount++;
                if (powerCount >= 6) {
                    powerCount = 0;
                    Log.d(TAG, "pressed 6 times run the code");
                    sendSms(context);
                }
                Log.d(TAG, "powercount=" + powerCount);
            }
        }
    }
}
