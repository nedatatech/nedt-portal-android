package com.nedatatech.datatechportal;

import android.content.Intent;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;


public class CustomerSearchActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

  private Button searchButton;
  private Button cancelButton;
  private EditText searchParamText;
  public ListView custResultView;
  private Spinner paramSpinner;

  private final String logtag = "CustomerSearchActivity";
  private String[] spinnerItems = {"ID", "First Name", "Last Name", "Email", "Phone", "Street", "City", "State", "Zip Code"};
  private String selectedSearchType, inputSearchParam;
  private ArrayAdapter<String> spinnerAdapter;
  private DatabaseOperations dataOps;
  private ArrayList<Customer> custResultList;
  private CustomerAdapter custAdapter;


  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer_search);
    dataOps = new DatabaseOperations(this);
    dataOps.openDB(); // Needs to be on a different thread for performance. ASyncTask??
    custResultView = (ListView) findViewById(R.id.custSearchResults_listView);

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

  public void getFromAdapter(Customer customer) {
    Intent resultIntent = new Intent(this, CustomerAddEditActivity.class);
    resultIntent.putExtra(BaseColumns._ID, String.valueOf(customer.getCustomerID()));
    resultIntent.putExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME, customer.getCustomerFirstName());
    resultIntent.putExtra(DatabaseContract.CustomerColumns.COLUMN_LAST_NAME, customer.getCustomerLastName());
    resultIntent.putExtra(DatabaseContract.CustomerColumns.COLUMN_EMAIL, customer.getCustomerEmail());
    resultIntent.putExtra(DatabaseContract.CustomerColumns.COLUMN_PHONE, customer.getCustomerPhone());
    resultIntent.putExtra(DatabaseContract.CustomerColumns.COLUMN_STREET, customer.getCustomerStreet());
    resultIntent.putExtra(DatabaseContract.CustomerColumns.COLUMN_CITY, customer.getCustomerCity());
    resultIntent.putExtra(DatabaseContract.CustomerColumns.COLUMN_STATE, customer.getCustomerState());
    resultIntent.putExtra(DatabaseContract.CustomerColumns.COLUMN_ZIPCODE, customer.getCustomerZipcode());
    Log.v(logtag, resultIntent.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME));
    if(getCallingActivity() != null){
      setResult(RESULT_OK, resultIntent);
      finish();
    } else if(getCallingActivity() == null){
      startActivity(resultIntent);
      // Gonna have to call a method in add edit here to get the text to the edit texts.
      // Or maybe if the request code isnt checked in the addedit result method it might work just setting the result code here in either situation.
      finish();
    }
  }

  @Override
  public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    switch (position) {
      case 0:
        selectedSearchType = BaseColumns._ID;
        break;
      case 1:
        selectedSearchType = DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME;
        break;
      case 2:
        selectedSearchType = DatabaseContract.CustomerColumns.COLUMN_LAST_NAME;
        break;
      case 3:
        selectedSearchType = DatabaseContract.CustomerColumns.COLUMN_EMAIL;
        break;
      case 4:
        selectedSearchType = DatabaseContract.CustomerColumns.COLUMN_PHONE;
        break;
      case 5:
        selectedSearchType = DatabaseContract.CustomerColumns.COLUMN_STREET;
        break;
      case 6:
        selectedSearchType = DatabaseContract.CustomerColumns.COLUMN_CITY;
        break;
      case 7:
        selectedSearchType = DatabaseContract.CustomerColumns.COLUMN_STATE;
        break;
      case 8:
        selectedSearchType = DatabaseContract.CustomerColumns.COLUMN_ZIPCODE;
        break;
      // Is a default needed?
    }
  }

  @Override
  public void onNothingSelected(AdapterView<?> parent) {
    // Code would go here if the drop down could have nothing selected or if needed to do something when it disappears.
    // This method has to exist for the android framework.
  }

  public void searchCustomers() { // May need to still do better error handling. crashes when searching for a deleted index also index higher than exists.
    if (searchParamText.getText().toString().trim().length() != 0) {
      inputSearchParam = searchParamText.getText().toString(); // Error if the line is actually empty??
      if (inputSearchParam != "") {
        custResultList = new ArrayList<>();
        custResultList.clear(); // Clears the array which clears the list view so the new result can be displayed.
        custResultList = dataOps.searchCustomers(selectedSearchType, inputSearchParam);
        Log.v(logtag, custResultList.toString()); // Debug info.
        custAdapter = new CustomerAdapter(this, R.layout.activity_customer_list_element, custResultList);
        Log.v(logtag, custAdapter.toString()); // Debug info.
        custResultView.setAdapter(custAdapter);
      }
    } else {
      Toast.makeText(this, "Need to Fill in the Search Field!", Toast.LENGTH_LONG).show();
      // ToDo Set up String labels for the fields in the adapter for the list view to display. i.e. ID, Name, Email, etc...
      // ToDo Still errors when the entered ID is greater than the actual size of the database or the array here??
    }
  }
}
