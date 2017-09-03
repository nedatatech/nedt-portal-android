package com.nedatatech.datatechportal;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

// ToDo Create some sort of manual to explain how to handle the operations this Activity can perform.
public class CustomerAddEditActivity extends AppCompatActivity {

  // May need to make some of these public depending on where the search stuff is for update and delete methods.
  private TextView customerIDText;
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
  private long idForEdit;
  private final int SEARCH_REQUEST_CODE = 1;
  private int toDialogCode = 0;
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

    customerIDText = (TextView) findViewById(R.id.customerId_TextView);
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
        if (addReqNotEmpty()) {
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
      public void onClick(View v) {
        if(idTextNotEmpty()) {
          toDialogCode = 1;
          confirmDialog("Update", toDialogCode);
        }
        // May end up wanting to close database here and finish the activity so accidental changes arent made. Could just clear the text fields and popup a toast.
      }
    });

    // Will need a toast to make sure the user has searched for the person to delete before they can. will need it to check for input first or rethink the way that update and delete are being laid out.
    deleteButton = (Button) findViewById(R.id.delete_addEditBtn);
    deleteButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        if(idTextNotEmpty()) {
          toDialogCode = 2;
          confirmDialog("Delete", toDialogCode);
        }
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
  private void checkForSearchStart() {
    if(intentFromSearch.getIntExtra("start_from_search", 0) == 0){
      startFromSearchAct = false;
    } else {
      startFromSearchAct = true;
    }
  }

  private boolean addReqNotEmpty() { // Should phone and/or email be required? Empty fields could just clutter the database. Also it would
    // be possible for someone to add the required fields and then go back later and update the results with empty fields. Need to decide on design of this.
    if (firstNameText.getText().toString().trim().length() == 0 | phoneText.getText().toString().trim().length() == 0) {
      String[] fields = {firstNameText.getText().toString(), phoneText.getText().toString()};
      if(fields[0].equals("") | fields[1].equals("")){
        Toast.makeText(this, "Must Enter A First Name and Phone To Add To Record", Toast.LENGTH_SHORT).show();
        return false;
      }
    } return true;
  }

  private Boolean idTextNotEmpty() {
    if(customerIDText.getText().toString().trim().length() == 0){
      String emptyID = customerIDText.getText().toString();
      if(emptyID.equals("")){
        Toast.makeText(this, "First Search For A Customer To Edit", Toast.LENGTH_SHORT).show();
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
      if(resultCode == RESULT_OK){
        // Need to check for empty fields and show a message to the user here and in showResultFromSearch.
        customerIDText.setText(data.getStringExtra(BaseColumns._ID));
        firstNameText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME));
        lastNameText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_LAST_NAME));
        emailText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_EMAIL));
        phoneText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_PHONE));
        streetText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_STREET));
        cityText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_CITY));
        stateText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_STATE));
        zipcodeText.setText(data.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_ZIPCODE));
        searchByID(); // Call from update and/or delete instead? Could Performance be an issue on where to run this search when the database grows?
      }
    }
  }

  // This will only run if the search activity was started from the main customer page first and then the edit button is pressed from there.
  public void showResultFromSearch(Intent intent) {
    customerIDText.setText(intent.getStringExtra(BaseColumns._ID));
    firstNameText.setText(intent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME));
    lastNameText.setText(intent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_LAST_NAME));
    emailText.setText(intent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_EMAIL));
    phoneText.setText(intent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_PHONE));
    streetText.setText(intent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_STREET));
    cityText.setText(intent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_CITY));
    stateText.setText(intent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_STATE));
    zipcodeText.setText(intent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_ZIPCODE));
    searchByID(); // Call from update and/or delete instead? Could Performance be an issue on where to run this search when the database grows?
  }

  private void updateOperation() { // needs error handling for empty ID field.
    customerOld.setCustomerFirstName(firstNameText.getText().toString());
    customerOld.setCustomerLastName(lastNameText.getText().toString());
    customerOld.setCustomerEmail(emailText.getText().toString());
    customerOld.setCustomerPhone(phoneText.getText().toString());
    customerOld.setCustomerStreet(streetText.getText().toString());
    customerOld.setCustomerCity(cityText.getText().toString());
    customerOld.setCustomerState(stateText.getText().toString());
    customerOld.setCustomerZipcode(zipcodeText.getText().toString());
    dataOps.updateCustomer(customerOld);
    toDialogCode = 0; // Set back to default for safety or maybe restart the whole activity to clear the texts.
    Toast.makeText(this, "Customer has been updated.", Toast.LENGTH_SHORT).show();

  }

  private void deleteOperation() { // Need error handling for empty ID field.
    searchByID(); // Redundant due to already calling from showFromSearchResult or onActivityResult? Could Performance be an issue on where to run this search when the database grows?
    dataOps.removeCustomer(customerOld); // Gonna need to confirm that it worked, clear the text fields, and/or restart activity.
    toDialogCode = 0;
    Toast.makeText(this, "Customer has been removed.", Toast.LENGTH_SHORT).show();
  }

  // Needed for delete method. For it to not throw an exception it must have a current customer object from the database to work with.
  private void searchByID() {
    idForEdit = Long.parseLong(customerIDText.getText().toString());
    customerOld = dataOps.getCustomer(idForEdit);
  }

  private void confirmDialog(final String callingOperation, final int requestCode) { // Parameters used to determine which way to handle the dialog text and action.
    LayoutInflater inflater = LayoutInflater.from(this);
    View dialog = inflater.inflate(R.layout.dialog_confirm, null);
    final AlertDialog.Builder aDBuilder = new AlertDialog.Builder(this);
    aDBuilder.setView(dialog);
    final TextView dialogTextView = (TextView) dialog.findViewById(R.id.confirm_textView);
    dialogTextView.setText(getString(R.string.string_confirm) + " " + callingOperation + " " + getString(R.string.string_operation));
    aDBuilder.setCancelable(true).setPositiveButton("CONFIRM", new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        if(requestCode == 1) {
          updateOperation();
          dialog.dismiss();
        }
        if(requestCode == 2) {
          deleteOperation();
          dialog.dismiss();
        }
      }
    }).setNegativeButton(getString(R.string.buttonText_cancel), new DialogInterface.OnClickListener() {
      @Override
      public void onClick(DialogInterface dialog, int which) {
        Toast.makeText(CustomerAddEditActivity.this, callingOperation + " Was Canceled!!", Toast.LENGTH_LONG).show();
        dialog.dismiss();
      }
    }).create().show();
  }

}
