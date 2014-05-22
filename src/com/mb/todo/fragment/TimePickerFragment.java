package com.mb.todo.fragment;

import java.util.Calendar;

import com.mb.todo.fragment.DatePickerFragment.OnDateSetListener;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.TimePicker;

public class TimePickerFragment extends DialogFragment
								implements TimePickerDialog.OnTimeSetListener { 

	private static final String REMINDER_TIME_ARG = "ReminderTimeTS";
	private OnTimeSetListener listener;
	
	public static TimePickerFragment newInstance(long currentReminderTime) {
		TimePickerFragment timePickerFragment = new TimePickerFragment();
		Bundle args = new Bundle();
		args.putLong(REMINDER_TIME_ARG, currentReminderTime);
		timePickerFragment.setArguments(args);
		return timePickerFragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		long currentReminderTS = getArguments().getLong(REMINDER_TIME_ARG);

		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(currentReminderTS);
		
		int hour = c.get(Calendar.HOUR_OF_DAY);
		int minute = c.get(Calendar.MINUTE);

		// Create a new instance of DatePickerDialog and return it
		return new TimePickerDialog(getActivity(), this, hour, minute, true);
	}
	
	
	@Override
	public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.HOUR_OF_DAY, hourOfDay);
		c.set(Calendar.MINUTE, minute);
		listener.onTimeSet(c);
	}
	
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnTimeSetListener) {
			listener = (OnTimeSetListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement TimePickerFragment.OnTimeSetListener");
		}
	}
	
	public interface OnTimeSetListener {
		public void onTimeSet(Calendar chosenDate);
	}
	
}
