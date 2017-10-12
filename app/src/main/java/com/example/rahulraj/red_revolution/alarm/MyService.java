package com.example.rahulraj.red_revolution.alarm;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.IBinder;

public class MyService extends Service {
	public MyService() {
	}

	@Override
	public IBinder onBind(Intent intent) {
		throw new UnsupportedOperationException("Not yet implemented");
	}
	
	PowerButtonReceiver msg = new PowerButtonReceiver();

	@Override
	public int onStartCommand(Intent intent, int flags, int startId) {
		registerReceiver(msg, new IntentFilter(Intent.ACTION_SCREEN_ON));
		registerReceiver(msg, new IntentFilter(Intent.ACTION_SCREEN_OFF));
		return START_STICKY;
	}
}
