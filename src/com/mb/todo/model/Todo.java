package com.mb.todo.model;

import java.io.Serializable;
import java.util.Calendar;

public class Todo implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	private String title;
	private boolean finished = false;
	private boolean dueDateSet = false;
	private Calendar dueDate = Calendar.getInstance();
	private Calendar finishDate;
	
	public Calendar getFinishDate() {
		return finishDate;
	}

	public void setFinishDate(Calendar finishDate) {
		this.finishDate = finishDate;
	}

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
		String outStr =  title + "," + finished + "," + dueDateSet + "," + dueDate.getTimeInMillis() + ",";
		if (finishDate == null) {
			outStr += 0;
		} else {
			outStr += finishDate.getTimeInMillis();
		}
		
		return outStr;
	}

}
