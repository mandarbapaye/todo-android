package com.mb.todo.notifications;

import static com.mb.todo.Constants.TODO_ITEM;
import android.app.AlarmManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;

import com.mb.todo.model.Todo;

public class NotificationsHelper {
	
	public static void scheduleReminderAlarm(Context context, Todo todo) {
		Intent todoIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(todo.getId())), context, TodoAlarmReceiver.class);
		todoIntent.putExtra(TODO_ITEM, todo);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, todoIntent, 0);
		
		getAndroidAlarmManager(context).set(AlarmManager.RTC, todo.getReminderTS().getTimeInMillis(), pendingIntent);
	}
	
	public static void cancelReminderAlarm(Context context, Todo todo) {
		Intent todoIntentToCancel = new Intent(Intent.ACTION_VIEW, Uri.parse(String.valueOf(todo.getId())), context, TodoAlarmReceiver.class);
		PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0, todoIntentToCancel, 0);

		getAndroidAlarmManager(context).cancel(pendingIntent);
	}
	
	public static void clearReminderNotification(Context context, Todo todo) {
		getAndroidNotificationManager(context).cancel(todo.getId());
	}
	
	public static void launchReminderNotification(Context context, Notification notification, Todo todo) {
		getAndroidNotificationManager(context).notify(todo.getId(), notification);
	}

	
	private static NotificationManager getAndroidNotificationManager(Context context) {
		return (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
	}
	
	private static AlarmManager getAndroidAlarmManager(Context context) {
		return (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
	}

}
