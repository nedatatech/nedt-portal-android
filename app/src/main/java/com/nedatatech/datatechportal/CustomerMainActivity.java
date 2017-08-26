package com.nedatatech.datatechportal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomerMainActivity extends AppCompatActivity {

  private Button buttonAddEdit;
  private Button buttonSearch;
  private Button buttonList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer_main);

    buttonAddEdit = (Button) findViewById(R.id.customerEdit_button);
    buttonAddEdit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent startAddEdit = new Intent(CustomerMainActivity.this, CustomerAddEditActivity.class);
        startActivity(startAddEdit); // May need back stack maintenance.

      }
    });

    buttonSearch = (Button) findViewById(R.id.customerSearch_button);
    buttonSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent startSearchActivity = new Intent(CustomerMainActivity.this, CustomerSearchActivity.class);
        startActivity(startSearchActivity); // May need back stack maintenance.

      }
    });

    buttonList = (Button) findViewById(R.id.customerList_button);
    buttonList.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent startListActivity = new Intent(CustomerMainActivity.this, CustomerListAllActivity.class);
        startActivity(startListActivity);

      }
    });

    /* ToDo Add Code for when to actually kill this activity. Maybe on BACK button.
    * ToDo May be a good place to add some general instructions for how to use the customer table.*/



  }
}
