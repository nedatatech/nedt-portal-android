package com.nedatatech.datatechportal;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;


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

  private long searchID;
  private DatabaseOperations dataOps;
  private DatabaseHelper dbHelper; // Not sure if needed here.
  private Customer custSearchResult; // Is this the right type for the results? Array List maybe?

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer_search);
    dataOps = new DatabaseOperations(this);
    dataOps.openDB(); // Needs to be on a different thread for performance. ASyncTask??

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
}
