package com.mb.todo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.mb.todo.fragment.DatePickerFragment;
import com.mb.todo.model.Todo;

import android.os.Bundle;
import android.app.Activity;
import android.app.DialogFragment;
import android.content.Intent;
import android.support.v4.app.FragmentActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import static com.mb.todo.Constants.ITEM_POS_INTENT_PARAM;
import static com.mb.todo.Constants.TODO_ITEM;

public class EditItemActivity extends FragmentActivity implements DatePickerFragment.OnDateSetListener {
	
	private EditText etEditItem;
	private Button btnSaveEdit;
	private CheckBox cbDueDate;
	private ImageButton btnCalendar;
	private TextView tvDueDateLabel;
	
	private int editItemPos;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		Todo todoItem = (Todo) getIntent().getSerializableExtra(TODO_ITEM);
		editItemPos = getIntent().getIntExtra(ITEM_POS_INTENT_PARAM, -1);
		
		if (editItemPos == -1) {
			finishWithError();
		}
				
		btnSaveEdit = (Button) findViewById(R.id.btnSaveEdit);
		cbDueDate = (CheckBox) findViewById(R.id.cbDueDate);
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		tvDueDateLabel = (TextView) findViewById(R.id.tvDueDateLabel);
		btnCalendar = (ImageButton) findViewById(R.id.btnCalendar);

		etEditItem.setText(todoItem.getTitle());
		etEditItem.requestFocus();
		etEditItem.setSelection(etEditItem.getText().length());
		
		setupTextChangeListener();
		setupCheckboxHandler();
		cbDueDate.setChecked(todoItem.isDueDateSet());
		tvDueDateLabel.setText(dateFormat.format(todoItem.getDueDate().getTime()));
	}

	
	private void setupCheckboxHandler() {
		cbDueDate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				btnCalendar.setEnabled(isChecked);
			}
		});
	}

	private void setupTextChangeListener() {
		etEditItem.addTextChangedListener(new TextWatcher() {

			@Override
			public void onTextChanged(CharSequence s, int start, int before, int count) {
			}
			
			@Override
			public void beforeTextChanged(CharSequence s, int start, int count,
					int after) {
			}
			
			@Override
			public void afterTextChanged(Editable s) {
				String newValue = etEditItem.getText().toString().trim();
				btnSaveEdit.setEnabled(!newValue.isEmpty());
			}
		});
	}

	private void finishWithError() {
		setResult(RESULT_CANCELED);
		finish();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.edit_item, menu);
		return true;
	}
	
	public void onItemEdited(View v) {
		String newValue = etEditItem.getText().toString();
		boolean dueDateSet = cbDueDate.isChecked();
		
		Todo newTodo = new Todo();
		newTodo.setTitle(newValue);
		newTodo.setDueDateSet(dueDateSet);
		
		Calendar dueDateCal = getDueDateCalendarInstance();
		newTodo.setDueDate(dueDateCal);
		
		Intent data = new Intent();
		data.putExtra(ITEM_POS_INTENT_PARAM, editItemPos);
		data.putExtra(TODO_ITEM, newTodo);
		
		setResult(RESULT_OK, data);
		finish();
	}
	
	public void showDatePickerDialog(View v) {
		Calendar dueDateCal = getDueDateCalendarInstance();
		
	    DialogFragment newFragment = DatePickerFragment.newInstance(dueDateCal.getTimeInMillis());
	    newFragment.show(this.getFragmentManager(), "datePicker");
	}
	
	private Calendar getDueDateCalendarInstance() {
		String dueDateStr = tvDueDateLabel.getText().toString();

		Calendar dueDateCal = Calendar.getInstance();
		try {
			Date dueDate = dateFormat.parse(dueDateStr);
			long msIn24Hrs = 24 * 60 * 60 * 1000;
			msIn24Hrs--;
			dueDateCal.setTimeInMillis(dueDate.getTime() + msIn24Hrs);
		} catch (Exception e) {
		}
		
		return dueDateCal;
	}

	@Override
	public void onDateSet(Calendar chosenDate) {
		tvDueDateLabel.setText(dateFormat.format(chosenDate.getTime()));
	}

}
