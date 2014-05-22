package com.mb.todo.util;

import java.util.Calendar;

import android.graphics.Color;

import com.mb.todo.pojo.DueStatus;

public class Utils {
	
	public static DueStatus getDueStatus(Calendar dueDateTS) {
		String detailText;
		int detailTextColor;

		Calendar currentTime = Calendar.getInstance();
    	if (currentTime.before(dueDateTS)) {
        	long diffInMS = dueDateTS.getTimeInMillis() - currentTime.getTimeInMillis();
        	long daysToDue = diffInMS / (24 * 60 * 60 * 1000);
        	
        	if (daysToDue < 1) {
            	detailText = "Due Today";
            	detailTextColor = Color.RED;
        	} else {
            	detailText = "Due in " + daysToDue + " day(s).";
            	detailTextColor = daysToDue < 2 ? Color.RED : (daysToDue < 5) ? Color.parseColor("#EEC900") : Color.parseColor("#9CCB19");
        	}                	
    	} else {
        	long diffInMS = currentTime.getTimeInMillis() - dueDateTS.getTimeInMillis();
        	long overdueDays = diffInMS / (24 * 60 * 60 * 1000);
			detailText = "Overdue by " + ++overdueDays + " day(s).";
        	detailTextColor = Color.RED;
    	}
    	
    	DueStatus dueStatus = new DueStatus();
    	dueStatus.setTitle(detailText);
    	dueStatus.setColor(detailTextColor);

    	return dueStatus;
	}

}
