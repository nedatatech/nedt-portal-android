package com.nedatatech.datatechportal.todoactivity;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;

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

    final NumberPicker npPriority = (NumberPicker) findViewById(R.id.npPriority);
    npPriority.setMinValue(1);
    npPriority.setMaxValue(50);
    npPriority.setValue(5);

    buttonCancel = (Button) findViewById(R.id.btnCancel);
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
        //EditText editText = (EditText) findViewById(R.id.editTextPriority);
        String priority = String.valueOf(npPriority.getValue());
        EditText txtDescription = (EditText) findViewById(R.id.etDescription);
        String description = txtDescription.getText().toString();
        EditText txtNotes = (EditText) findViewById(R.id.etNotes);
        String notes = txtNotes.getText().toString();

        ContentValues values = new ContentValues();
        values.put(DatabaseContract.ToDoDataColumns.COLUMN_PRIORITY , priority);
        values.put(DatabaseContract.ToDoDataColumns.COLUMN_DESCRIPTION, description);
        values.put(DatabaseContract.ToDoDataColumns.COLUMN_NOTES, notes);
        dataOps.saveToDoItem(values);
        dataOps.closeDB();
        finish();
      }
    });
  }
}
