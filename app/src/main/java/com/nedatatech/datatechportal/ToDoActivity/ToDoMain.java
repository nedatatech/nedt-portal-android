package com.nedatatech.datatechportal.ToDoActivity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.nedatatech.datatechportal.InventoryActivity;
import com.nedatatech.datatechportal.R;

public class ToDoMain extends AppCompatActivity {

  private Button buttonAdd;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_to_do_main);

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
