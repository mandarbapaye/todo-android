package com.mb.todo.model;

import java.io.Serializable;
import java.util.Calendar;

public class Todo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String title;
	private boolean finished = false;
	private boolean dueDateSet = false;
	private Calendar dueDate = Calendar.getInstance();
	
	public boolean isDueDateSet() {
		return dueDateSet;
	}

	public void setDueDateSet(boolean dueDateSet) {
		this.dueDateSet = dueDateSet;
	}

	public Calendar getDueDate() {
		return dueDate;
	}

	public void setDueDate(Calendar dueDate) {
		this.dueDate = dueDate;
	}

	public String getTitle() {
		return title;
	}
	
	public void setTitle(String title) {
		this.title = title;
	}
	
	public boolean isFinished() {
		return finished;
	}
	
	public void setFinished(boolean finished) {
		this.finished = finished;
	}
	
	@Override
	public String toString() {
		return title + "," + finished + "," + dueDateSet + "," + dueDate.getTimeInMillis();
	}

}
