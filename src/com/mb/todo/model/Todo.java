package com.mb.todo.model;

public class Todo {
	
	private String title;
	private boolean finished = false;
	
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
		return title + "," + finished;
	}

}
