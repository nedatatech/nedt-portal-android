package com.nedatatech.datatechportal;

import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

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
  private long userInputID; // Temp variable for searching by user input primary key for now.
  private final int SEARCH_REQUEST_CODE = 1;
  private String CANT_CHANGE_TEXT = "  -  Cannot change, only shown for reference.";
  private String logtag = "Customer Add/Edit";
  private Boolean startFromSearchAct; // may need to set false here to start.
  Intent intentFromSearch;

  @Override
  protected void onCreate(final Bundle savedInstanceState) {
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

    // ToDo Write code to check for empty fields before allowing the add to perform, delete and update should probably do it too.
    // ToDo Need to set ID field editable or not editable based on what the current interaction with the activity is and in the proper places.
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
        if (emptyText()) {
          dataOps.addCustomer(customerNew);
          finish(); // This can go with a back to main or upwards navigation if it turns out we need to manage the back stack better.
        }
        // Should probably close the database here depending on how navigating around the app gets handled.
      }
    });

    searchButton = (Button) findViewById(R.id.search_addEditBtn);
    searchButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent startSearchActivity = new Intent(CustomerAddEditActivity.this, CustomerSearchActivity.class);
        startActivityForResult(startSearchActivity, SEARCH_REQUEST_CODE);
      }
    });

    updateButton = (Button) findViewById(R.id.update_addEditBtn);
    updateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) { // Will need to set the id field editable or not depending on type of operation.
        customerOld.setCustomerFirstName(firstNameText.getText().toString());
        customerOld.setCustomerLastName(lastNameText.getText().toString());
        customerOld.setCustomerEmail(emailText.getText().toString());
        customerOld.setCustomerPhone(phoneText.getText().toString());
        customerOld.setCustomerStreet(streetText.getText().toString());
        customerOld.setCustomerCity(cityText.getText().toString());
        customerOld.setCustomerState(stateText.getText().toString());
        customerOld.setCustomerZipcode(zipcodeText.getText().toString());
        dataOps.updateCustomer(customerOld);
        // May end up wanting to close database here and finish the activity so accidental changes arent made. Could just clear the text fields and popup a toast.
      }
    });

    // Will need a toast to make sure the user has searched for the person to delete before they can. will need it to check for input first or rethink the way that update and delete are being laid out.
    deleteButton = (Button) findViewById(R.id.delete_addEditBtn); // Will need to set the id field editable or not depending on type of operation.
    deleteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) { // ToDo Make a Dialog to confirm that deleting is meant to happen. Maybe also move the delete button more away from others.
        searchByID();
        dataOps.removeCustomer(customerOld); // Gonna need to confirm that it worked or clear the text fields.
      }
    });

    cancelButton = (Button) findViewById(R.id.cancel_addEditBtn);
    cancelButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dataOps.closeDB(); // Should close the database here??
        finish(); // Kill this activity so multiples aren't produced. (Back Stack).
      }
    });
    // The next few lines are ran every time the activity is created and handle the checker method to decide how to handle existing customer data, if any.
    intentFromSearch = getIntent();
    checkForSearchStart();
    if(startFromSearchAct){
      showResultFromSearch(intentFromSearch);
    }
  }

  // This checks for how this activity was started and sets a boolean to keep track.
  private void checkForSearchStart() { // Will need to set the id field editable or not depending on type of operation.
    if(intentFromSearch.getIntExtra("start_from_search", 0) == 0){
      startFromSearchAct = false;
    } else {
      startFromSearchAct = true;
    }
  }

  private boolean emptyText() { // Will need to set the id field editable or not depending on type of operation.
    if (firstNameText.getText().toString().trim().length() == 0 | phoneText.getText().toString().trim().length() == 0) {
      String[] fields = {firstNameText.getText().toString(), phoneText.getText().toString()};
      if(fields[0].equals("") | fields[1].equals("")){
        Toast.makeText(this, "Must Enter A First Name and Phone To Add To Record", Toast.LENGTH_SHORT).show();
        return false;
      }
    } return true;
  }

  // This will only run if this activity is started and then a search is performed from there followed by the edit button next to the desired customer's result.
  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    Log.v(logtag, "Activity Result Triggered");
    if(requestCode == SEARCH_REQUEST_CODE) {
      Log.v(logtag, "Request Code Matched");
      Log.v(logtag, String.valueOf(resultCode));
      if(resultCode == RESULT_OK){ // Will need to set the id field editable or not depending on type of operation.

        // Need to check for empty fields and show a message to the user here and in showResultFromSearch.
        // will need to set Id field editable or not editable here and where ever else it will need to be changed.
        customerIDText.setText(data.getStringExtra(BaseColumns._ID));
        firstNameText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME));
        lastNameText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_LAST_NAME));
        emailText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_EMAIL));
        phoneText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_PHONE));
        streetText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_STREET));
        cityText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_CITY));
        stateText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_STATE));
        zipcodeText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_ZIPCODE));
        searchByID();
      }
    }
  }

  // This will only run if the search activity was started from the main customer page first and then the edit button is pressed from there.
  public void showResultFromSearch(Intent intent) {
    customerIDText.setText(intent.getStringExtra(BaseColumns._ID)); // Will need to set the id field editable or not depending on type of operation.
    firstNameText.setText(intent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME));
    lastNameText.setText(intent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_LAST_NAME));
    emailText.setText(intent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_EMAIL));
    phoneText.setText(intent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_PHONE));
    streetText.setText(intent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_STREET));
    cityText.setText(intent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_CITY));
    stateText.setText(intent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_STATE));
    zipcodeText.setText(intent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_ZIPCODE));
    searchByID();
  }

  // Needed for delete and update methods. For them to not throw an exception they must have a current customer object from the database to work with.
  private void searchByID() { // Will need to set the id field editable or not depending on type of operation.
    userInputID = Long.parseLong(customerIDText.getText().toString());
    customerOld = dataOps.getCustomer(userInputID);
  }

}
