package com.example.rahulraj.red_revolution.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;

/**
 * Created by sagardesai on 27/03/17.
 */
public class VolumeReceiver extends BroadcastReceiver {

    private static final String TAG = VolumeReceiver.class.getSimpleName();

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.media.VOLUME_CHANGED_ACTION")) {
            Log.d(TAG, "volume has changed");
        }
    }
}