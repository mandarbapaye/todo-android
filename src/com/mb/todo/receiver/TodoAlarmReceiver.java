package com.mb.todo.receiver;

import com.mb.todo.R;

import android.app.NotificationManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;


public class TodoAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		createNotification(context, intent);
	}
	
	private void createNotification(Context context, Intent intent) {
		NotificationCompat.Builder mBuilder = 
				new NotificationCompat.Builder(context).setSmallIcon(R.drawable.ic_checkmark_holo_light).setContentTitle(intent.getDataString()).setContentText("No due date details yet.");

		NotificationManager mNotificationManager = 
	            (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
		// mId allows you to update the notification later on.
		mNotificationManager.notify(100, mBuilder.build());
	}
	

}
