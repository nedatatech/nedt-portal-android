package com.nedatatech.datatechportal;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
    // Get the data
    Customer customer = getItem(position);
    // Check first for existing view to use. If not create one. Either way move on to populating.
    if(convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.activity_customer_list_element, parent, false);
    }

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
    custID.setText(String.valueOf(customer.getCustomerID()));
    custFName.setText(customer.getCustomerFirstName());
    custLName.setText(customer.getCustomerLastName());
    custEmail.setText(customer.getCustomerEmail());
    custPhone.setText(customer.getCustomerPhone());
    custStreet.setText(customer.getCustomerStreet());
    custCity.setText(customer.getCustomerCity());
    custState.setText(customer.getCustomerState());
    custZip.setText(customer.getCustomerZipcode());

    // The Edit Button that will appear next to each entry in the list. Takes the entry and brings it to AddEdit for editing or deleting.
    Button editButton = (Button) convertView.findViewById(R.id.custEdit_listButton);
    editButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Read more in the example for info on how to get the button to grab the right customer for the item position it should point to.
      }
    });

    return convertView;
    // ToDo Need the edit button to grab the data and send to the AddEdit Activity. Maybe call a method from the search Activity??
  }
}
