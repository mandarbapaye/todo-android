package com.mb.todo.fragment;

import java.util.Calendar;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.os.Bundle;
import android.widget.DatePicker;

public class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {
	
	private static final String DUE_DATE_ARG = "DueDateArg";
	private OnDateSetListener listener;
	
	public static DatePickerFragment newInstance(long currentDueDate) {
		DatePickerFragment datePickerFragment = new DatePickerFragment();
		Bundle args = new Bundle();
		args.putLong(DUE_DATE_ARG, currentDueDate);
		datePickerFragment.setArguments(args);
		return datePickerFragment;
	}
	
	@Override
	public Dialog onCreateDialog(Bundle savedInstanceState) {
		long currentDateTimeStamp = getArguments().getLong(DUE_DATE_ARG);

		// Use the current date as the default date in the picker
		final Calendar c = Calendar.getInstance();
		c.setTimeInMillis(currentDateTimeStamp);
		
		int year = c.get(Calendar.YEAR);
		int month = c.get(Calendar.MONTH);
		int day = c.get(Calendar.DAY_OF_MONTH);

		// Create a new instance of DatePickerDialog and return it
		return new DatePickerDialog(getActivity(), this, year, month, day);
	}

	public void onDateSet(DatePicker view, int year, int month, int day) {
		Calendar c = Calendar.getInstance();
		c.set(Calendar.YEAR, year);
		c.set(Calendar.MONTH, month);
		c.set(Calendar.DAY_OF_MONTH, day);
		listener.onDateSet(c);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (activity instanceof OnDateSetListener) {
			listener = (OnDateSetListener) activity;
		} else {
			throw new ClassCastException(activity.toString()
					+ " must implement DatePickerFragment.OnDateSetListener");
		}
	}	
	
	public interface OnDateSetListener {
		public void onDateSet(Calendar chosenDate);
	}
	
	
}