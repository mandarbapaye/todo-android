package com.mb.todo.notifications;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

import com.mb.todo.model.Todo;
import static com.mb.todo.Constants.TODO_FILENAME;

public class BootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.intent.action.BOOT_COMPLETED")) {
    		scheduleTodoReminders(context);
        }
    }
    
	private void scheduleTodoReminders(Context context) {
		
		File todosDir = context.getFilesDir();
		File todoItemsFile = new File(todosDir, TODO_FILENAME);
		Calendar currentTime = Calendar.getInstance();
		
		try {
			List<String> todoLines = FileUtils.readLines(todoItemsFile);
			for (String todoLine : todoLines) {
				Todo todo = new Todo();

				int count = 0;
				Scanner scanner = new Scanner(todoLine);
				scanner.useDelimiter(",");
				while (scanner.hasNext()) {
					String token = scanner.next();
					if (count == 0) {
						todo.setId(Integer.parseInt(token));
					} else if (count == 1) {
						todo.setTitle(token);
					} else if (count == 2) {
						todo.setFinished(Boolean.parseBoolean(token));
					} else if (count == 3) {
						todo.setDueDateSet(Boolean.parseBoolean(token));
					} else if (count == 4) {
						Calendar dueDate = Calendar.getInstance();
						dueDate.setTimeInMillis(Long.parseLong(token));
						todo.setDueDate(dueDate);
					} else if (count == 5) {
						long finishMs = Long.parseLong(token);
						if (finishMs == 0) {
							todo.setFinishDate(null);
						} else {
							Calendar finishDate = Calendar.getInstance();
							finishDate.setTimeInMillis(finishMs);
							todo.setFinishDate(finishDate);
						}
					} else if (count == 6) {
						long reminderMS = Long.parseLong(token);
						if (reminderMS == 0) {
							todo.setReminderTS(null);
						} else {
							Calendar reminderTS = Calendar.getInstance();
							reminderTS.setTimeInMillis(reminderMS);
							todo.setReminderTS(reminderTS);
						}
					}
					count++;
				}
				
				if (todo.getReminderTS() != null &&
					todo.getReminderTS().after(currentTime)) {

					NotificationsHelper.scheduleReminderAlarm(context, todo);
				}
				
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}