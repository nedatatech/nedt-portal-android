package com.nedatatech.datatechportal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

// ToDo Create some sort of manual to explain how to handle the operations this Activity can perform.
public class CustomerAddEditActivity extends AppCompatActivity {

  // May need to make some of these public depending on where the search stuff is for update and delete methods.
  private EditText customerIDText;
  private EditText firstNameText;
  private EditText lastNameText;
  private EditText emailText;
  private EditText phoneText;
  private EditText streetText;
  private EditText cityText;
  private EditText stateText;
  private EditText zipcodeText;
  private Button addButton;
  private Button searchButton;
  private Button updateButton;
  private Button deleteButton;
  private Button cancelButton;

  private DatabaseOperations dataOps;
  private Customer customerNew;
  private Customer customerOld; // For working in this class with the search result.

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer_addedit);

    dataOps = new DatabaseOperations(this);
    dataOps.openDB(); // Not sure if needed here because main should have already done this unless its killed by onBackPressed? See notes in MainActivity as well.
    customerNew = new Customer();

    customerIDText = (EditText) findViewById(R.id.customerId_editText);
    firstNameText = (EditText) findViewById(R.id.customerFName_editText);
    lastNameText = (EditText) findViewById(R.id.customerLName_editText);
    emailText = (EditText) findViewById(R.id.customerEmail_editText);
    phoneText = (EditText) findViewById(R.id.customerPhone_editText);
    streetText = (EditText) findViewById(R.id.customerStreet_editText);
    cityText = (EditText) findViewById(R.id.customerCity_editText);
    stateText = (EditText) findViewById(R.id.customerState_editText);
    zipcodeText = (EditText) findViewById(R.id.customerZipcode_editText);

    addButton = (Button) findViewById(R.id.add_addEditBtn);
    addButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Customer ID is not needed here because the database is set up to autoincrement it and setting one could cause conflicts. Can be changed if needed.
        customerNew.setCustomerFirstName(firstNameText.getText().toString());
        customerNew.setCustomerLastName(lastNameText.getText().toString());
        customerNew.setCustomerEmail(emailText.getText().toString());
        customerNew.setCustomerPhone(phoneText.getText().toString());
        customerNew.setCustomerStreet(streetText.getText().toString());
        customerNew.setCustomerCity(cityText.getText().toString());
        customerNew.setCustomerState(stateText.getText().toString());
        customerNew.setCustomerZipcode(zipcodeText.getText().toString());
        dataOps.addCustomer(customerNew);
        finish(); // This can go with a back to main or upwards navigation if it turns out we need to manage the back stack better.
        // Should probably close the database here depending on how navigating around the app gets handled.
      }
    });

    searchButton = (Button) findViewById(R.id.search_addEditBtn);
    searchButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent startSearchActivity = new Intent(CustomerAddEditActivity.this, CustomerSearchActivity.class);
        startActivity(startSearchActivity); /* May need to finish here depending on how the search params will be retrieved and returned to this classes layout.*/
      }
    });

    updateButton = (Button) findViewById(R.id.update_addEditBtn);
    updateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });

    deleteButton = (Button) findViewById(R.id.delete_addEditBtn);
    deleteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) { // ToDo Make a Dialog to confirm that deleting is meant to happen. Maybe also move the delete button more away from others.

      }
    });

    cancelButton = (Button) findViewById(R.id.cancel_addEditBtn);
    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dataOps.closeDB(); // Should close the database here in case the user won't come back to this activity.
        finish(); // Kill this activity so multiples aren't produced. (Back Stack).
      }
    });
  }

  public void displayResult(long customerSearchID) { // Will need to pass an array here to display the results I think.


  }




















  // ToDo Make a method for calling the editTexts and pass in the data from the Search Activity into each field. ContentProvider or values.put() with an intent?
}
