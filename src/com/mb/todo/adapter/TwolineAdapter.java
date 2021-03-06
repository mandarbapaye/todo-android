package com.mb.todo.adapter;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.mb.todo.R;
import com.mb.todo.model.Todo;
import com.mb.todo.pojo.DueStatus;
import com.mb.todo.util.Utils;

public class TwolineAdapter extends ArrayAdapter<Todo> {

	private List<Todo> todoList_;
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
	
	public TwolineAdapter(Context context, List<Todo> todos) {
		super(context, R.layout.todo_item, R.id.ctvTodoItemTitle, todos);
        this.todoList_ = todos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        CheckedTextView ctvTodoItemTitle = (CheckedTextView) view.findViewById(R.id.ctvTodoItemTitle);
        TextView tvTodoItemDetail = (TextView) view.findViewById(R.id.tvTodoItemDetail);
        
        Todo todo = todoList_.get(position);
        boolean isFinished = todo.isFinished();
        
        ctvTodoItemTitle.setText(todo.getTitle());
        ctvTodoItemTitle.setTextAppearance(super.getContext(), android.R.style.TextAppearance_Medium);

        if (isFinished) {
        	ctvTodoItemTitle.setTextColor(Color.GRAY);
        	ctvTodoItemTitle.setPaintFlags(ctvTodoItemTitle.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
        	ctvTodoItemTitle.setTextColor(Color.BLACK);
        	ctvTodoItemTitle.setPaintFlags(ctvTodoItemTitle.getPaintFlags() & ~Paint.STRIKE_THRU_TEXT_FLAG);
        }
        
        ctvTodoItemTitle.setChecked(isFinished);

        int detailTextColor = Color.GRAY;
        String detailText = super.getContext().getResources().getString(R.string.noDueDate_label);
        
        if (todo.isFinished()) {
        	detailText = "Finished on: " + dateFormat.format(todo.getFinishDate().getTime());
        } else {
            if (todo.isDueDateSet()) {
//            	Calendar currentTime = Calendar.getInstance();
//            	
//            	if (currentTime.before(todo.getDueDate())) {
//                	long diffInMS = todo.getDueDate().getTimeInMillis() - currentTime.getTimeInMillis();
//                	long daysToDue = diffInMS / (24 * 60 * 60 * 1000);
//                	
//                	if (daysToDue < 1) {
//                    	detailText = "Due Today";
//                    	detailTextColor = Color.RED;
//                	} else {
//                    	detailText = "Due in " + daysToDue + " day(s).";
//                    	detailTextColor = daysToDue < 2 ? Color.RED : (daysToDue < 5) ? Color.parseColor("#EEC900") : Color.parseColor("#9CCB19");
//                	}                	
//            	} else {
//                	long diffInMS = currentTime.getTimeInMillis() - todo.getDueDate().getTimeInMillis();
//                	long overdueDays = diffInMS / (24 * 60 * 60 * 1000);
//					detailText = "Overdue by " + ++overdueDays + " day(s).";
//                	detailTextColor = Color.RED;
//            	}
            	
            	DueStatus dueStatus = Utils.getDueStatus(todo.getDueDate());
            	detailText = dueStatus.getTitle();
            	detailTextColor = dueStatus.getColor();
            }
        }
        
        tvTodoItemDetail.setText(detailText);
        tvTodoItemDetail.setTextColor(detailTextColor);
        return view;
    }
}
