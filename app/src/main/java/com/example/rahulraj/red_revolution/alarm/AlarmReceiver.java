package com.example.rahulraj.red_revolution.alarm;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.rahulraj.red_revolution.R;

public class AlarmReceiver extends BroadcastReceiver {

	
	private static String TAG = AlarmReceiver.class.getSimpleName();
	@Override
	public void onReceive(Context ctx, Intent intent) {
		try {
			Bundle bundle = intent.getExtras();
			String message = bundle.getString("alarm_msg");
			Log.d(TAG, "Inside onReceive : msg : " + message);

			if ("SHOW_NOTIFICATION".equalsIgnoreCase(message)) {
				RemoteViews remoteViews = new RemoteViews(ctx.getPackageName(),
						R.layout.custom_notification);


				// Open NotificationView Class on Notification Click
				Intent inten = new Intent(ctx, DonationReminderActivity.class);
				// Send data to NotificationView Class
				inten.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP
						| Intent.FLAG_ACTIVITY_SINGLE_TOP);
//				resultIntent.putExtra("mobile", true);
				// Open NotificationView.java Activity
				PendingIntent pIntent = PendingIntent.getActivity(ctx, 3, inten,
						PendingIntent.FLAG_UPDATE_CURRENT);

				NotificationCompat.Builder builder = new NotificationCompat.Builder(ctx)
						// Set Icon
						.setSmallIcon(R.drawable.ic_notify)
						// Set Ticker Message
						.setTicker(ctx.getString(R.string.app_name))
						// Dismiss Notification
						.setAutoCancel(true)
						// Set PendingIntent into Notification
						.setContentIntent(pIntent)
						// Set RemoteViews into Notification
						.setContent(remoteViews)
						.setSound(Uri.parse("android.resource://"+ctx.getPackageName()+"/"+R.raw.alert));

				// Locate and set the Image into customnotificationtext.xml ImageViews
				remoteViews.setImageViewResource(R.id.notifi_ic,R.mipmap.ic_notification_color);
//				remoteViews.setImageViewResource(R.id.imagenotiright,R.drawable.androidhappy);

				// Locate and set the Text into customnotificationtext.xml TextViews
				remoteViews.setTextViewText(R.id.notifi_title,ctx.getString(R.string.app_name));
				remoteViews.setTextViewText(R.id.notifi_content,"It's been 3 months!");

				// Create Notification Manager
				NotificationManager notificationmanager = (NotificationManager) ctx.getSystemService(Context.NOTIFICATION_SERVICE);
				// Build Notification with Notification Manager
				notificationmanager.notify(3, builder.build());

			}
		} catch (Exception e) {
			Log.d(TAG,"exception in onReceive :  " + e.toString());
			e.printStackTrace();
		}
	}

	
	
}
