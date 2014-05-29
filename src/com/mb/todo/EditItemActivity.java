package com.mb.todo;

import static com.mb.todo.Constants.ITEM_POS_INTENT_PARAM;
import static com.mb.todo.Constants.TODO_ITEM;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import android.app.DialogFragment;
import android.content.Intent;
import android.os.Bundle;
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

import com.mb.todo.fragment.DatePickerFragment;
import com.mb.todo.fragment.TimePickerFragment;
import com.mb.todo.model.Todo;

public class EditItemActivity extends FragmentActivity implements DatePickerFragment.OnDateSetListener,
																  TimePickerFragment.OnTimeSetListener  {
	
	private EditText etEditItem;
	private Button btnSaveEdit;
	
	private CheckBox cbDueDate;
	private ImageButton btnCalendar;
	private TextView tvDueDateLabel;
	
	private CheckBox cbReminder;
	private ImageButton btnReminderDate;
	private ImageButton btnReminderTime;
	private TextView tvReminderTime;
	
	private int editItemPos;
	
	private SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
	private SimpleDateFormat reminderDateFormat = new SimpleDateFormat("yyyy-MM-dd '@' HH:mm", Locale.US);
	
	private boolean chooseDueDate = false;
	private boolean chooseReminderDate = false;
	
	private Todo todoItemToEdit;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		Todo todoItem = (Todo) getIntent().getSerializableExtra(TODO_ITEM);
		todoItemToEdit = todoItem;
		
		editItemPos = getIntent().getIntExtra(ITEM_POS_INTENT_PARAM, -1);
		
		if (editItemPos == -1) {
			finishWithError();
		}
				
		btnSaveEdit = (Button) findViewById(R.id.btnSaveEdit);
		cbDueDate = (CheckBox) findViewById(R.id.cbDueDate);
		etEditItem = (EditText) findViewById(R.id.etEditItem);
		tvDueDateLabel = (TextView) findViewById(R.id.tvDueDateLabel);
		btnCalendar = (ImageButton) findViewById(R.id.btnCalendar);
		
		cbReminder = (CheckBox) findViewById(R.id.cbReminder);
		btnReminderDate = (ImageButton) findViewById(R.id.btnReminderDate);
		btnReminderTime = (ImageButton) findViewById(R.id.btnReminderTime);
		tvReminderTime = (TextView) findViewById(R.id.tvReminderTime);

		etEditItem.setText(todoItem.getTitle());
		etEditItem.requestFocus();
		etEditItem.setSelection(etEditItem.getText().length());
		
		setupTextChangeListener();
		setupCheckboxHandler();
		cbDueDate.setChecked(todoItem.isDueDateSet());
		cbReminder.setChecked(todoItem.getReminderTS() != null);
		
		tvDueDateLabel.setText(dateFormat.format(todoItem.getDueDate().getTime()));
		
		if (todoItem.getReminderTS() != null) {
			tvReminderTime.setText(reminderDateFormat.format(todoItem.getReminderTS().getTime()));
		} else {
			tvReminderTime.setText(R.string.noDateSet_label);
		}
	}

	
	private void setupCheckboxHandler() {
		cbDueDate.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				btnCalendar.setEnabled(isChecked);
			}
		});
		
		cbReminder.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			@Override
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				btnReminderDate.setEnabled(isChecked);
				btnReminderTime.setEnabled(isChecked);
				tvReminderTime.setVisibility(isChecked ? View.VISIBLE : View.GONE);
				if (!isChecked) {
					tvReminderTime.setText(R.string.noDateSet_label);
				}
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
		newTodo.setId(todoItemToEdit.getId());
		newTodo.setTitle(newValue);
		newTodo.setDueDateSet(dueDateSet);
		
		Calendar dueDateCal = getDueDateCalendarInstance();
		newTodo.setDueDate(dueDateCal);
		
		Calendar reminderTime = getReminderDateCalendarInstance();
		newTodo.setReminderTS(reminderTime);
		
		Intent data = new Intent();
		data.putExtra(ITEM_POS_INTENT_PARAM, editItemPos);
		data.putExtra(TODO_ITEM, newTodo);
		
		setResult(RESULT_OK, data);
		finish();
	}
	
	public void showDueDatePickerDialog(View v) {
		Calendar dueDateCal = getDueDateCalendarInstance();
		
		chooseDueDate = true;
	    DialogFragment newFragment = DatePickerFragment.newInstance(dueDateCal.getTimeInMillis());
	    newFragment.show(this.getFragmentManager(), "datePicker");
	}
	
	public void showReminderDatePickerDialog(View v) {
		Calendar reminderDateCal = getReminderDateCalendarInstance();
		if (reminderDateCal == null) {
			reminderDateCal = Calendar.getInstance();
		}
		
		chooseReminderDate = true;
	    DialogFragment newFragment = DatePickerFragment.newInstance(reminderDateCal.getTimeInMillis());
	    newFragment.show(this.getFragmentManager(), "datePicker");
	}
	
	public void showReminderTimePickerDialog(View v) {
		Calendar reminderTimeCal = getReminderDateCalendarInstance();
		if (reminderTimeCal == null) {
			reminderTimeCal = Calendar.getInstance();
		}
		
	    DialogFragment newFragment = TimePickerFragment.newInstance(reminderTimeCal.getTimeInMillis());
	    newFragment.show(this.getFragmentManager(), "timePicker");
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

	private Calendar getReminderDateCalendarInstance() {
		String reminderDateStr = tvReminderTime.getText().toString();
		
		try {
			Date reminderDate = reminderDateFormat.parse(reminderDateStr);
			Calendar reminderDateCal = Calendar.getInstance();
			reminderDateCal.setTimeInMillis(reminderDate.getTime());
			return reminderDateCal;
		} catch (Exception e) {
		}
		return null;
	}

	@Override
	public void onDateSet(Calendar chosenDate) {
		if (chooseReminderDate) {
			chooseReminderDate = false;
			
			Calendar reminderDateCal = getReminderDateCalendarInstance();
			if (reminderDateCal == null) {
				reminderDateCal = Calendar.getInstance();
			}
			
			reminderDateCal.set(Calendar.MONTH, chosenDate.get(Calendar.MONTH));
			reminderDateCal.set(Calendar.DATE, chosenDate.get(Calendar.DATE));
			reminderDateCal.set(Calendar.YEAR, chosenDate.get(Calendar.YEAR));
			tvReminderTime.setText(reminderDateFormat.format(reminderDateCal.getTime()));	

		} else if (chooseDueDate) {
			chooseDueDate = false;
			tvDueDateLabel.setText(dateFormat.format(chosenDate.getTime()));	
		}
	}


	@Override
	public void onTimeSet(Calendar chosenDate) {
		Calendar reminderDateCal = getReminderDateCalendarInstance();
		if (reminderDateCal == null) {
			reminderDateCal = Calendar.getInstance();
		}

		reminderDateCal.set(Calendar.HOUR_OF_DAY, chosenDate.get(Calendar.HOUR_OF_DAY));
		reminderDateCal.set(Calendar.MINUTE, chosenDate.get(Calendar.MINUTE));
		tvReminderTime.setText(reminderDateFormat.format(reminderDateCal.getTime()));	
	}

}
