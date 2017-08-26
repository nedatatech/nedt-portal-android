package com.nedatatech.datatechportal;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import java.util.ArrayList;


public class CustomerAdapter extends ArrayAdapter<Customer> {

  public CustomerAdapter(Context context, ArrayList<Customer> customers) {
    super(context, 0, customers);
  }

  @NonNull
  @Override
  public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
    // Check first for existing view to use. If not create one. Either way move on to populating.
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_customer_list_element, parent, false);
    }
    // Get the data
    final Customer customer = getItem(position);
    final long _id = customer.getCustomerID();
    final String firstName = customer.getCustomerFirstName();
    final String lastName = customer.getCustomerLastName();
    final String email = customer.getCustomerEmail();
    final String phone = customer.getCustomerPhone();
    final String street = customer.getCustomerStreet();
    final String city = customer.getCustomerCity();
    final String state = customer.getCustomerState();
    final String zipCode = customer.getCustomerZipcode();
    // Get references to the TextViews in the custom layout for each list item.
    TextView custID = (TextView) convertView.findViewById(R.id.custID_listItem);
    TextView custFName = (TextView) convertView.findViewById(R.id.custFName_listItem);
    TextView custLName = (TextView) convertView.findViewById(R.id.custLName_listItem);
    TextView custEmail = (TextView) convertView.findViewById(R.id.custEmail_listItem);
    TextView custPhone = (TextView) convertView.findViewById(R.id.custPhone_listItem);
    TextView custStreet = (TextView) convertView.findViewById(R.id.custStreet_listItem);
    TextView custCity = (TextView) convertView.findViewById(R.id.custCity_listItem);
    TextView custState = (TextView) convertView.findViewById(R.id.custState_listItem);
    TextView custZip = (TextView) convertView.findViewById(R.id.custZip_listItem);
    // Populate the TextViews. May need to make some changes here to better adapt to Customer Object and search results.
    custID.setText(String.valueOf(_id));
    custFName.setText(firstName);
    custLName.setText(lastName);
    custEmail.setText(email);
    custPhone.setText(phone);
    custStreet.setText(street);
    custCity.setText(city);
    custState.setText(state);
    custZip.setText(zipCode);
    // The Edit Button that will appear next to each entry in the list. Takes the entry and sends it to AddEdit for editing or deleting.
    Button editButton = (Button) convertView.findViewById(R.id.custEdit_listButton);
    editButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent sendCustomer = new Intent(getContext(), CustomerAddEditActivity.class);
        sendCustomer.putExtra(BaseColumns._ID, String.valueOf(_id));
        sendCustomer.putExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME, firstName);
        sendCustomer.putExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME, lastName);
        sendCustomer.putExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME, email);
        sendCustomer.putExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME, phone);
        sendCustomer.putExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME, street);
        sendCustomer.putExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME, city);
        sendCustomer.putExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME, state);
        sendCustomer.putExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME, zipCode);
        getContext().startActivity(sendCustomer);
        // Need back stack help here.
      }
    });
    return convertView;
    // ToDo Need the edit button to grab the data and send to the AddEdit Activity. Maybe call a method from the search Activity??
  }
}
