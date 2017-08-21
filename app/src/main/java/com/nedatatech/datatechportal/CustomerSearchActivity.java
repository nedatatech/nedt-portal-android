package com.nedatatech.datatechportal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;


public class CustomerSearchActivity extends AppCompatActivity {

  private Button searchButton;
  private Button cancelButton;
  private EditText searchIDText;
  private EditText searchFNameText;
  private EditText searchLNameText;
  private EditText searchEmailText;
  private EditText searchPhoneText;
  private EditText searchStreetText;
  private EditText searchCityText;
  private EditText searchStateText;
  private EditText searchZipText;
  private ListView custResultView;

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
    searchFNameText = (EditText) findViewById(R.id.custSearchFName_editText);
    searchLNameText = (EditText) findViewById(R.id.custSearchLName_editText);
    searchEmailText = (EditText) findViewById(R.id.custSearchEmail_editText);
    searchPhoneText = (EditText) findViewById(R.id.custSearchPhone_editText);
    searchStreetText = (EditText) findViewById(R.id.custSearchStreet_editText);
    searchCityText = (EditText) findViewById(R.id.custSearchCity_editText);
    searchStateText = (EditText) findViewById(R.id.custSearchState_editText);
    searchZipText = (EditText) findViewById(R.id.custSearchZip_editText);

    searchButton = (Button) findViewById(R.id.custSearch_button);
    searchButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) { // Will need to call the methods from the to do on this classes file.
        // Need to clear the list view first in order to free it for a new search without having to leave and reenter the activity.
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

// ToDo Write Methods for searching based on whether a user has entered certain search criteria on certain lines in the layout. Switch or IF Else??
  }

  public void searchCustomers() { // May be better or easier to do this with a combination of if and switch statements.
    searchID = Long.parseLong(searchIDText.getText().toString()); // Error if the line is actually empty??
    if(searchID > 0) { // Need a way of checking if the ID is null or has a value.
      custSearchResult = dataOps.getCustomer(searchID); // Gets the customer result based on ID.
      custResultList.add(custSearchResult); // Adds the customer object to the customer array. This will need to be dynamically repeated for other search criteria.
      custAdapter = new CustomerAdapter(this, custResultList);
      custResultView.setAdapter(custAdapter);
    } else {
      Toast.makeText(this, "Need to Fill in the ID Field!", Toast.LENGTH_LONG).show();
      // ToDo This toast will never show because there is a fatal error when it tries to search with an empty field. Need to Fix.
    }
  }










}
