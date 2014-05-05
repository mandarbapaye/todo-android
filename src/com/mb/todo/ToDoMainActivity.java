package com.mb.todo;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.io.FileUtils;

import android.os.Bundle;
import android.app.Activity;
import android.content.Intent;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import static com.mb.todo.Constants.ITEM_TEXT_INTENT_PARAM;
import static com.mb.todo.Constants.ITEM_POS_INTENT_PARAM;

public class ToDoMainActivity extends Activity {
	
	private ArrayList<String> todoItemsList;
	private ArrayAdapter<String> todoItemsAdapter;
	private EditText etNewItem;
	private ListView lvItems;
	
	private static final String TODO_FILENAME = "todos.txt";
	private static final int REQUEST_CODE = 20;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do_main);
		readItemsFromFile();
		
		lvItems = (ListView) findViewById(R.id.lvItems);
		todoItemsAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, todoItemsList);
		lvItems.setAdapter(todoItemsAdapter);
		etNewItem = (EditText) findViewById(R.id.etNewItem);
		
		setupLongClickHandler();
		setupClickHandler();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.to_do_main, menu);
		return true;
	}
	
	public void addTodo(View v) {
		String newTodo = etNewItem.getText().toString();
		
		if (newTodo.trim().isEmpty()) {
			Toast.makeText(this, R.string.invalid_blank_item_label, Toast.LENGTH_SHORT).show();
		} else {
			todoItemsAdapter.add(newTodo);
//			Even adding item to list auto updates the view.
//			todoItemsList.add(newTodo+"-test");
			etNewItem.setText("");		
			saveItemsToFile();
		}
		
	}

	private void setupLongClickHandler() {
		lvItems.setOnItemLongClickListener(new OnItemLongClickListener() {
			
			@Override
			public boolean onItemLongClick(AdapterView<?> parentContainerView, View v,
										   int pos, long id) {			
				todoItemsList.remove(pos);
				todoItemsAdapter.notifyDataSetChanged();
				saveItemsToFile();
				return true;
			}
			
		});
	}
	
	private void setupClickHandler() {
		lvItems.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parentContainerView, View v,
									int pos, long id) {
				
				Intent editItemIntent = new Intent(ToDoMainActivity.this, EditItemActivity.class);
				editItemIntent.putExtra(ITEM_TEXT_INTENT_PARAM, todoItemsList.get(pos));
				editItemIntent.putExtra(ITEM_POS_INTENT_PARAM, pos);
				
				startActivityForResult(editItemIntent, REQUEST_CODE);
			}			
		});
		
	}
	
	
	private void readItemsFromFile() {
		File todosDir = getFilesDir();
		File todoItemsFile = new File(todosDir, TODO_FILENAME);
		
		try {
			todoItemsList = new ArrayList<String>(FileUtils.readLines(todoItemsFile));			
		} catch (IOException e) {
			todoItemsList = new ArrayList<String>();
			e.printStackTrace();
		}		
	}

	private void saveItemsToFile() {
		File todosDir = getFilesDir();
		File todoItemsFile = new File(todosDir, TODO_FILENAME);
		
		try {
			FileUtils.writeLines(todoItemsFile, todoItemsList);
		} catch (IOException e) {
			todoItemsList = new ArrayList<String>();
			e.printStackTrace();
		}		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			String newValue = data.getExtras().getString(ITEM_TEXT_INTENT_PARAM);
			int itemPos = data.getExtras().getInt(ITEM_POS_INTENT_PARAM);
			
			todoItemsAdapter.insert(newValue, itemPos);
			todoItemsList.remove(itemPos + 1);
			todoItemsAdapter.notifyDataSetChanged();
			saveItemsToFile();
		}
	}

}
