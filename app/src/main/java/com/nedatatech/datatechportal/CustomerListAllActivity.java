package com.nedatatech.datatechportal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.app.ListActivity;
import android.widget.Button;

import java.util.List;

// May want to rewrite this class later to use the customer adapter for the list view so it has the edit buttons.
public class CustomerListAllActivity extends ListActivity {

  private DatabaseOperations dataOps;
  private List<Customer> customerList;
  ArrayAdapter<Customer> custAdapter;

  private Button cancelButton;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer_list_all);
    dataOps = new DatabaseOperations(this);
    dataOps.openDB();
    customerList = dataOps.getAllCustomers();
    // Should close database when access will not be needed anymore. MainActivity?

    custAdapter = new ArrayAdapter<Customer>(this, android.R.layout.simple_list_item_1, customerList);
    setListAdapter(custAdapter);

    cancelButton = (Button) findViewById(R.id.cancelListAll_button);
    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish(); // May need some back stack maintenance.
      }
    });
  }
}
