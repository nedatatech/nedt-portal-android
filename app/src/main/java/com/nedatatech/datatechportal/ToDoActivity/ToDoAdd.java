package com.nedatatech.datatechportal.ToDoActivity;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nedatatech.datatechportal.DatabaseContract;
import com.nedatatech.datatechportal.DatabaseOperations;
import com.nedatatech.datatechportal.R;

public class ToDoAdd extends AppCompatActivity {

  private Button buttonCancel;
  private Button buttonSave;
  private DatabaseOperations dataOps;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_to_do_add);

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
        dataOps = new DatabaseOperations(ToDoAdd.this);
        dataOps.openDB();
        //dataOps.addTokenToDB(auth_token);
        EditText editText = (EditText) findViewById(R.id.editTextPriority);
        String priority = editText.getText().toString();
        editText = (EditText) findViewById(R.id.editTextDescription);
        String description = editText.getText().toString();
        editText = (EditText) findViewById(R.id.editTextNotes);
        String notes = editText.getText().toString();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ToDoDataColumns.COLUMN_PRIORITY , priority);
        values.put(DatabaseContract.ToDoDataColumns.COLUMN_DESCRIPTION, description);
        values.put(DatabaseContract.ToDoDataColumns.COLUMN_NOTES, notes);
        dataOps.saveToDoItem(values);
        dataOps.closeDB();
      }
    });
  }
}
