package com.nedatatech.datatechportal;

import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
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
  private long userInputID; // Temp variable for searching by user input primary key for now.
  private final int SEARCH_REQUEST_CODE = 1;
  private String CANT_CHANGE_TEXT = "  -  Cannot change, only shown for reference.";
  private String logtag = "Customer Add/Edit";

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
      public void onClick(View v) { // startActivity for result likely how this should go then get the result of the search activities edit button.
        Intent startSearchActivity = new Intent(CustomerAddEditActivity.this, CustomerSearchActivity.class);
        startActivityForResult(startSearchActivity, SEARCH_REQUEST_CODE); /* May need to finish here depending on how the search params will be retrieved and returned to this classes layout.*/
        // displayResult(); // Not needed until decided how to get search results from search activity.
        // May be an error when getting the results of the search and returning them here due to the cursor not existing. But the object should persist.
      }
    });

    updateButton = (Button) findViewById(R.id.update_addEditBtn);
    updateButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
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
    deleteButton = (Button) findViewById(R.id.delete_addEditBtn);
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
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) { // NEED TO SET RESULT IN SEARCH ACTIVITY.
    Log.v(logtag, "Activity Result Triggered");
    if(requestCode == SEARCH_REQUEST_CODE) {
      Log.v(logtag, "Request Code Matched");
      Log.v(logtag, String.valueOf(resultCode));
      if(resultCode == RESULT_OK){
        Log.v(logtag, "Result Code Matched"); // Will need to set the data to a customer object then set the text by that in order for update to work.
        // long _id = getIntent().getLongExtra(BaseColumns._ID, 0);
        //customerIDText.setText(getIntent().getStringExtra(BaseColumns._ID) + "%s" + CANT_CHANGE_TEXT); // MAY WANT TO JUST CHANGE THE LAYOUT TO AN UN-EDITABLE TEXT VIEW IN JAVA FOR JUST THIS PART.
        String firstName = data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME);
        Log.v(logtag, firstName);
        firstNameText.setText(firstName);
        lastNameText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_LAST_NAME));
        emailText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_EMAIL));
        phoneText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_PHONE));
        streetText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_STREET));
        cityText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_CITY));
        stateText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_STATE));
        zipcodeText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_ZIPCODE));
      }
    }
  }

  // ToDo Make a method for calling the editTexts and pass in the data from the Search Activity into each field. ContentProvider or values.put() with an intent?
  // ToDo Rewrite this to do what the search activity should to pass editable results to this activity or delete this method when no longer used.
  /*public void displayResult() { // Will need to pass an array here to display the results I think.
    searchByID();
    customerIDText.setText(String.valueOf(customerOld.getCustomerID()));
    firstNameText.setText(customerOld.getCustomerFirstName());
    lastNameText.setText(customerOld.getCustomerLastName());
    emailText.setText(customerOld.getCustomerEmail());
    phoneText.setText(customerOld.getCustomerPhone());
    streetText.setText(customerOld.getCustomerStreet());
    cityText.setText(customerOld.getCustomerCity());
    stateText.setText(customerOld.getCustomerState());
    zipcodeText.setText(customerOld.getCustomerZipcode());
  }*/

  // Needed for delete method for now. May be better to rewrite once search activity is properly linked to this activity.
  public void searchByID() {
    userInputID = Long.parseLong(customerIDText.getText().toString());
    customerOld = dataOps.getCustomer(userInputID);
  }




}
