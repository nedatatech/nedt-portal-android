package com.nedatatech.datatechportal.ToDoActivity;

import android.app.ListActivity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import com.nedatatech.datatechportal.DatabaseOperations;
import com.nedatatech.datatechportal.InventoryActivity;
import com.nedatatech.datatechportal.R;

import java.util.List;

public class ToDoMain extends ListActivity {

  private Button buttonAdd;
  private DatabaseOperations dataOps;
  private List<String> toDoList;
  ArrayAdapter<String> toDoListAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_to_do_main);

    dataOps = new DatabaseOperations(this);
    dataOps.openDB();
    toDoList = dataOps.getToDoItems();
    // Should close database when access will not be needed anymore. MainActivity?

    toDoListAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, toDoList);
    setListAdapter(toDoListAdapter);

    buttonAdd = (Button) findViewById(R.id.toDoAdd_button);
    buttonAdd.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent startListActivity = new Intent(ToDoMain.this, ToDoAdd.class);
        startActivity(startListActivity);
      }
    });
  }
}
