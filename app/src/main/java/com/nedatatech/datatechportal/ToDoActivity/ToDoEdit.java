package com.nedatatech.datatechportal.ToDoActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.NumberPicker;

import com.nedatatech.datatechportal.DatabaseContract;
import com.nedatatech.datatechportal.DatabaseOperations;
import com.nedatatech.datatechportal.R;

public class ToDoEdit extends AppCompatActivity {

  private Button buttonCancel;
  private Button buttonSave;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_to_do_edit);

    final NumberPicker npPriority = (NumberPicker) findViewById(R.id.npPriority);
    npPriority.setMinValue(1);
    npPriority.setMaxValue(50);
    npPriority.setValue(5);

    DatabaseOperations dataOps = new DatabaseOperations(this);
    dataOps.openDB();

    Intent intent = getIntent();

    final String itemId = intent.getStringExtra("itemId");

    dataOps.todoCursor = dataOps.getSingleToDoItem(itemId);
    if (dataOps.todoCursor.moveToFirst()) {
      String Description = dataOps.todoCursor.getString(dataOps.todoCursor.getColumnIndexOrThrow(DatabaseContract.ToDoDataColumns.COLUMN_DESCRIPTION));
      String Priority = dataOps.todoCursor.getString(dataOps.todoCursor.getColumnIndexOrThrow(DatabaseContract.ToDoDataColumns.COLUMN_PRIORITY));
      String Notes = dataOps.todoCursor.getString(dataOps.todoCursor.getColumnIndexOrThrow(DatabaseContract.ToDoDataColumns.COLUMN_NOTES));
      EditText txtDescription = (EditText) findViewById(R.id.editTextDescription);
      EditText txtNotes = (EditText) findViewById(R.id.editTextNotes);
      txtDescription.setText(Description);
      npPriority.setMinValue(1);
      npPriority.setMaxValue(50);
      npPriority.setValue(Integer.valueOf(Priority));
      txtNotes.setText(Notes);
    }
    //ListView lvItems = (ListView) findViewById(R.id.lvItems);
    //dataOps.todoAdapter = new ToDoAdapter(this, dataOps.todoCursor);
    //lvItems.setAdapter(dataOps.todoAdapter);
    //registerForContextMenu(lvItems);

    buttonCancel = (Button) findViewById(R.id.buttonCancel);
    buttonCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    buttonSave = (Button) findViewById(R.id.buttonSave);
    buttonSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        DatabaseOperations dataOps = new DatabaseOperations(ToDoEdit.this);
        dataOps.openDB();
        //dataOps.addTokenToDB(auth_token);
        //EditText editText = (EditText) findViewById(R.id.editTextPriority);
        String priority = String.valueOf(npPriority.getValue());
        EditText txtDescription = (EditText) findViewById(R.id.editTextDescription);
        String description = txtDescription.getText().toString();
        EditText txtNotes = (EditText) findViewById(R.id.editTextNotes);
        String notes = txtNotes.getText().toString();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ToDoDataColumns.COLUMN_PRIORITY , priority);
        values.put(DatabaseContract.ToDoDataColumns.COLUMN_DESCRIPTION, description);
        values.put(DatabaseContract.ToDoDataColumns.COLUMN_NOTES, notes);
        dataOps.updateToDoItem(values, itemId);
        dataOps.closeDB();
        finish();
      }
    });

  }
}
