package com.mb.todo;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.mb.todo.Constants.ITEM_TEXT_INTENT_PARAM;
import static com.mb.todo.Constants.ITEM_POS_INTENT_PARAM;

public class EditItemActivity extends Activity {
	
	private EditText etEditItem;
	private Button btnSaveEdit;
	
	private int editItemPos;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_edit_item);
		
		String editItemText = getIntent().getStringExtra(ITEM_TEXT_INTENT_PARAM);
		editItemPos = getIntent().getIntExtra(ITEM_POS_INTENT_PARAM, -1);
		
		if (editItemPos == -1) {
			finishWithError();
		}
				
		btnSaveEdit = (Button) findViewById(R.id.btnSaveEdit);

		etEditItem = (EditText) findViewById(R.id.etEditItem);
		etEditItem.setText(editItemText);		
		etEditItem.requestFocus();
		etEditItem.setSelection(etEditItem.getText().length());
		
		setupTextChangeListener();
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
		Intent data = new Intent();
		data.putExtra(ITEM_TEXT_INTENT_PARAM, newValue);
		data.putExtra(ITEM_POS_INTENT_PARAM, editItemPos);
		setResult(RESULT_OK, data);
		finish();
	}

}
