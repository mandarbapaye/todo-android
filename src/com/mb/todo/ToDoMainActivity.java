package com.mb.todo;

import static com.mb.todo.Constants.ITEM_POS_INTENT_PARAM;
import static com.mb.todo.Constants.ITEM_TEXT_INTENT_PARAM;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import org.apache.commons.io.FileUtils;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.mb.todo.adapter.TwolineAdapter;
import com.mb.todo.model.Todo;

public class ToDoMainActivity extends Activity {
	
	private ArrayList<Todo> todoList;
	private TwolineAdapter todoItemsAdapter;
	private EditText etNewItem;
	private ListView lvItems;
	
	private static final String TODO_FILENAME = "todos.txt";
	private static final int REQUEST_CODE = 20;
	
	// Tracks current menu item
	private int currentListItemIndex;
	// Tracks current contextual action mode
	private ActionMode currentActionMode;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_to_do_main);
		readItemsFromFile();
		
		lvItems = (ListView) findViewById(R.id.lvItems);
		todoItemsAdapter = new TwolineAdapter(this, todoList);
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
			Todo todo = new Todo();
			todo.setTitle(newTodo);
			
			todoItemsAdapter.add(todo);
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
				if (currentActionMode != null) { 
					currentActionMode.finish();
				}
		        currentListItemIndex = pos;
		        currentActionMode = startActionMode(modeCallBack);
		        v.setSelected(true);
		        return true;
			}
		});
	}
	
	private void setupClickHandler() {
		lvItems.setOnItemClickListener(new OnItemClickListener() {
			
			@Override
			public void onItemClick(AdapterView<?> parentContainerView, View v,
									int pos, long id) {
				
				Todo todo = todoList.get(pos);
				todo.setFinished(!todo.isFinished());
				todoItemsAdapter.notifyDataSetChanged();
				saveItemsToFile();
				
			}			
		});
	}
	
	private void readItemsFromFile() {
		
		todoList = new ArrayList<Todo>();
		
		File todosDir = getFilesDir();
		File todoItemsFile = new File(todosDir, TODO_FILENAME);
		
		try {
			List<String> todoLines = FileUtils.readLines(todoItemsFile);
			for (String todoLine : todoLines) {
				Todo todo = new Todo();

				int count = 0;
				Scanner scanner = new Scanner(todoLine);
				scanner.useDelimiter(",");
				while (scanner.hasNext()) {
					String token = scanner.next();
					if (count == 0) {
						todo.setTitle(token);
					} else if (count == 1) {
						todo.setFinished(Boolean.parseBoolean(token));
					}
					count++;
				}
				
				todoList.add(todo);
			}
		} catch (IOException e) {
			todoList = new ArrayList<Todo>();
			e.printStackTrace();
		}		
	}

	private void saveItemsToFile() {
		File todosDir = getFilesDir();
		File todoItemsFile = new File(todosDir, TODO_FILENAME);
		
		try {
			List<String> todoFileLines = new ArrayList<String>();
			for (Todo todo : todoList) {
				todoFileLines.add(todo.toString());
			}
			
			FileUtils.writeLines(todoItemsFile, todoFileLines);
		} catch (IOException e) {
			todoList = new ArrayList<Todo>();
			e.printStackTrace();
		}		
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == REQUEST_CODE && resultCode == RESULT_OK) {
			String newValue = data.getExtras().getString(ITEM_TEXT_INTENT_PARAM);
			int itemPos = data.getExtras().getInt(ITEM_POS_INTENT_PARAM);
			
			Todo newTodo = new Todo();
			newTodo.setTitle(newValue);
			
			todoItemsAdapter.insert(newTodo, itemPos);
			todoList.remove(itemPos + 1);
			todoItemsAdapter.notifyDataSetChanged();
			saveItemsToFile();
		}
	}
	
	
	// Define the callback when ActionMode is activated
	private ActionMode.Callback modeCallBack = new ActionMode.Callback() {
		// Called when the action mode is created; startActionMode() was called
		String itemTitle;
		
		@Override
		public boolean onCreateActionMode(ActionMode mode, Menu menu) {
			itemTitle = todoList.get(currentListItemIndex).getTitle();
			Toast.makeText(ToDoMainActivity.this, "Selected " + itemTitle, Toast.LENGTH_SHORT).show();

			mode.setTitle("Actions");
			mode.getMenuInflater().inflate(R.menu.actions_menu, menu);
			return true;
		}

		// Called each time the action mode is shown.
		@Override
		public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
			return false; // Return false if nothing is done
		}

		// Called when the user selects a contextual menu item
		@Override
		public boolean onActionItemClicked(ActionMode mode, MenuItem item) {			
			switch (item.getItemId()) {
			
				case R.id.menu_edit:

					Intent editItemIntent = new Intent(ToDoMainActivity.this, EditItemActivity.class);
					editItemIntent.putExtra(ITEM_TEXT_INTENT_PARAM, todoList.get(currentListItemIndex).getTitle());					
					editItemIntent.putExtra(ITEM_POS_INTENT_PARAM, currentListItemIndex);
					startActivityForResult(editItemIntent, REQUEST_CODE);

					mode.finish(); // Action picked, so close the contextual menu
					return true;
					
				case R.id.menu_delete:
					todoList.remove(currentListItemIndex);
					todoItemsAdapter.notifyDataSetChanged();
					saveItemsToFile();
					mode.finish();
					return true;
					
				default:
					return false;
			}
		}

		// Called when the user exits the action mode
		@Override
		public void onDestroyActionMode(ActionMode mode) {
			currentActionMode = null; // Clear current action mode
		}
	};	
	
	

}
