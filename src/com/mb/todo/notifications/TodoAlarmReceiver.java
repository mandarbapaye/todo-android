package com.mb.todo.notifications;

import com.mb.todo.R;
import com.mb.todo.ToDoMainActivity;
import com.mb.todo.model.Todo;
import com.mb.todo.util.Utils;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NotificationCompat;
import static com.mb.todo.Constants.TODO_ITEM;

public class TodoAlarmReceiver extends BroadcastReceiver {

	@Override
	public void onReceive(Context context, Intent intent) {
		createNotification(context, intent);
	}
	
	private void createNotification(Context context, Intent intent) {
		Todo todoItem = (Todo) intent.getExtras().get(TODO_ITEM);
		
		NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(context);
		mBuilder = mBuilder.setSmallIcon(R.drawable.ic_checkmark_holo_light);
		mBuilder = mBuilder.setContentTitle(todoItem.getTitle());
		mBuilder = mBuilder.setContentText(todoItem.isDueDateSet() ? Utils.getDueStatus(todoItem.getDueDate()).getTitle() : context.getResources().getString(R.string.noDueDate_label));

		Intent activityIntent = new Intent(context, ToDoMainActivity.class);
		PendingIntent resultPendingIntent = PendingIntent.getActivity(context, 0, activityIntent, PendingIntent.FLAG_UPDATE_CURRENT);
		mBuilder.setContentIntent(resultPendingIntent);

		NotificationsHelper.launchReminderNotification(context, mBuilder.build(), todoItem);
	}

}
