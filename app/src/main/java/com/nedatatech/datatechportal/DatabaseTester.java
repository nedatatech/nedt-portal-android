package com.nedatatech.datatechportal;

import android.app.ListActivity;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;

import java.util.ArrayList;
import java.util.List;

public class DatabaseTester extends ListActivity {

  private DatabaseOperations dataOps;
  private String token;
  private ArrayList<String> tokenList = new ArrayList<>();
  ArrayAdapter<String> tokenAdapter;

  private Button cancelButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer_list_all);
    dataOps = new DatabaseOperations(this);
    dataOps.openDB();
    token = dataOps.getTokenFromDB(BaseColumns._ID,"1");
    // Should close database when access will not be needed anymore. mainactivity?
    tokenList.add(token);
    tokenAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, tokenList);
    setListAdapter(tokenAdapter);

    cancelButton = (Button) findViewById(R.id.cancelListAll_button);
    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish(); // May need some back stack maintenance.
      }
    });
  }
}
