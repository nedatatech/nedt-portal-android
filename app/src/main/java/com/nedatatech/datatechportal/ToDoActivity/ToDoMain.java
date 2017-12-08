package com.nedatatech.datatechportal.ToDoActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.nedatatech.datatechportal.DatabaseOperations;
import com.nedatatech.datatechportal.R;

public class ToDoMain extends Activity {

  private DatabaseOperations dataOps;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_to_do_main);

    dataOps = new DatabaseOperations(this);
    dataOps.openDB();

    dataOps.todoCursor = dataOps.getToDoItems();

    ListView lvItems = (ListView) findViewById(R.id.lvItems);
    dataOps.todoAdapter = new ToDoAdapter(this, dataOps.todoCursor);
    lvItems.setAdapter(dataOps.todoAdapter);

    Button buttonAdd = (Button) findViewById(R.id.toDoAdd_button);
    buttonAdd.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent startListActivity = new Intent(ToDoMain.this, ToDoAdd.class);
        startActivity(startListActivity);
      }
    });
  }

  @Override
  protected void onResume() {
    super.onResume();
    dataOps.refreshToDoList();
  }
}
