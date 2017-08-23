package com.nedatatech.datatechportal;

import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.Toast;

import java.util.ArrayList;


public class CustomerSearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
  // ToDo Finish tying up drop down and search method to new search in operation class once that search is updated also.

  private Button searchButton;
  private Button cancelButton;
  private EditText searchIDText; // Remove here and in layout file when all new search functions are updated.
  private EditText searchParamText;
  private ListView custResultView;
  private Spinner paramSpinner;

  private String[] spinnerItems = {"ID", "First Name", "Last Name", "Email", "Phone", "Street", "City", "State", "Zip Code"};
  private String searchType;
  private ArrayAdapter<String> spinnerAdapter;
  private long searchID;
  private DatabaseOperations dataOps;
  private Customer custSearchResult; // Some of these may need to be public if access ends up being needed from, other classes.
  private ArrayList<Customer> custResultList;
  private CustomerAdapter custAdapter;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer_search);
    dataOps = new DatabaseOperations(this);
    dataOps.openDB(); // Needs to be on a different thread for performance. ASyncTask??
    custResultView = (ListView) findViewById(R.id.custSearchResults_listView);
    custResultList = new ArrayList<>(); // Will need to get the method that searches, to iterate through the results and for each result add the customer to the list.

    searchIDText = (EditText) findViewById(R.id.custSearchID_editText);
    searchParamText = (EditText) findViewById(R.id.custSearchParam_editText);

    paramSpinner = (Spinner) findViewById(R.id.custSearch_spinner);
    spinnerAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, spinnerItems);
    spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
    paramSpinner.setAdapter(spinnerAdapter);
    paramSpinner.setOnItemSelectedListener(this);

    searchButton = (Button) findViewById(R.id.custSearch_button);
    searchButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) { // Will need to call the methods from the to do on this classes file.
        searchCustomers();
      }
    });

    cancelButton = (Button) findViewById(R.id.custCancel_button);
    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Close db here depending on which activity this will go back to??
        finish();
      }
    });
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    switch(position) { // Tested by printing string selections to edit text in layout. works perfectly. Rewrite search in database operations to work this way now.
      case 0:
        searchType = BaseColumns._ID;

        break;
      case 1:
        searchType = DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME;

        break;
      case 2:
        searchType = DatabaseContract.CustomerColumns.COLUMN_LAST_NAME;

        break;
      case 3:
        searchType = DatabaseContract.CustomerColumns.COLUMN_EMAIL;

        break;
      case 4:
        searchType = DatabaseContract.CustomerColumns.COLUMN_PHONE;

        break;
      case 5:
        searchType = DatabaseContract.CustomerColumns.COLUMN_STREET;

        break;
      case 6:
        searchType = DatabaseContract.CustomerColumns.COLUMN_CITY;

        break;
      case 7:
        searchType = DatabaseContract.CustomerColumns.COLUMN_STATE;

        break;
      case 8:
        searchType = DatabaseContract.CustomerColumns.COLUMN_ZIPCODE;

        break;
      // Is a default needed?
    }

  }


  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    // Code would go here if the drop down could have nothing selected or if needed to do something when it disappears.
    // This method has to exist for the android framework.
  }

  // Need to rewrite this method also to work with the new spinner drop down.
  public void searchCustomers() { // May be better or easier to do this with a combination of if and switch statements.
    if (searchParamText.getText().toString().trim().length() != 0) { // Need to do better error handling. crashes when searching for a deleted index also index higher than exists.
      searchID = Long.parseLong(searchParamText.getText().toString()); // Error if the line is actually empty??
      if (searchID > 0) { // Could do the if conditions based on the current size of the array index.
        custResultList.clear(); // Clears the array which clears the list view so the new result can be displayed.
        custSearchResult = dataOps.getCustomer(searchID); // Gets the customer result based on ID.
        custResultList.add(custSearchResult); // Adds the customer object to the customer array. This will need to be dynamically repeated for other search criteria.
        custAdapter = new CustomerAdapter(this, custResultList);
        custResultView.setAdapter(custAdapter);
      }
    } else {
      Toast.makeText(this, "Need to Fill in the Search Field!", Toast.LENGTH_LONG).show();
      // ToDo Set up String labels for the fields in the adapter for the list view to display. i.e. ID, Name, Email, etc...
      // ToDo Still errors when the entered ID is greater than the actual size of the database or the array here??
    }
  }
}
