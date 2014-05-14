package com.mb.todo.adapter;

import java.util.List;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.TextView;

import com.mb.todo.R;
import com.mb.todo.model.Todo;

public class TwolineAdapter extends ArrayAdapter<Todo> {

//	private List<String> todos_;
	private List<Todo> todoList_;
	
//	public TwolineAdapter(Context context, List<String> todos) {
//        //super(context, android.R.layout.simple_list_item_2, android.R.id.text1, todos);		
//		super(context, R.layout.todo_item, R.id.ctvTodoItemTitle, todos);
//        this.todos_ = todos;
//    }
	
	public TwolineAdapter(Context context, List<Todo> todos) {
		super(context, R.layout.todo_item, R.id.ctvTodoItemTitle, todos);
        this.todoList_ = todos;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View view = super.getView(position, convertView, parent);
        CheckedTextView ctvTodoItemTitle = (CheckedTextView) view.findViewById(R.id.ctvTodoItemTitle);
        TextView tvTodoItemDetail = (TextView) view.findViewById(R.id.tvTodoItemDetail);

        ctvTodoItemTitle.setText(todoList_.get(position).getTitle());
        ctvTodoItemTitle.setChecked(todoList_.get(position).isFinished());
        tvTodoItemDetail.setText(String.valueOf(position));
        return view;
    }
}
