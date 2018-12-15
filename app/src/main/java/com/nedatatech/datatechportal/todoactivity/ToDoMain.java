package com.nedatatech.datatechportal.todoactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
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

    dataOps.todoCursor = dataOps.getAllToDoItems();

    ListView lvItems = (ListView) findViewById(R.id.lvItems);
    dataOps.todoAdapter = new ToDoAdapter(this, dataOps.todoCursor);
    lvItems.setAdapter(dataOps.todoAdapter);
    registerForContextMenu(lvItems);

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
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
  {
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.setHeaderTitle("Select The Action");
    menu.add(0, v.getId(), 0, "Delete");
    menu.add(0, v.getId(), 0, "Edit");
    menu.add(0, v.getId(), 0, "View");
  }

  @Override
  public boolean onContextItemSelected(MenuItem item){
    if(item.getTitle()=="Delete"){
      //Toast.makeText(getApplicationContext(),"Deletes the item",Toast.LENGTH_LONG).show();
      final String itemId = dataOps.todoCursor.getString(dataOps.todoCursor.getColumnIndex("_id"));
      dataOps.deleteToDoItem(itemId);
      dataOps.refreshToDoList();
    }
    else if(item.getTitle()=="Edit") {
      //Toast.makeText(getApplicationContext(),"Edits the item",Toast.LENGTH_LONG).show();
      String itemId = dataOps.todoCursor.getString(dataOps.todoCursor.getColumnIndex("_id"));
      dataOps.getSingleToDoItem(itemId);
      Intent startListActivity = new Intent(ToDoMain.this, ToDoEdit.class);
      startListActivity.putExtra("itemId", itemId);
      startActivity(startListActivity);
    }else if(item.getTitle()=="View"){
      Intent startListActivity = new Intent(ToDoMain.this, ToDoView.class);
      startActivity(startListActivity);
    }else{
      return false;
    }
    return true;
  }

  @Override
  protected void onResume() {
    super.onResume();
    dataOps.refreshToDoList();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    dataOps.closeDB();
  }
}