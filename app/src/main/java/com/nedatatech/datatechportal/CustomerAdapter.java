package com.nedatatech.datatechportal;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.provider.BaseColumns;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

import java.util.ArrayList;


public class CustomerAdapter extends ArrayAdapter<Customer> {
  private Context context;
  private int layoutResourceID;
  private ArrayList<Customer> dataListItem = new ArrayList<>();


  public CustomerAdapter(Context context, int layoutResourceID, ArrayList<Customer> dataListItem) {
    super(context, layoutResourceID, dataListItem);
    this.layoutResourceID = layoutResourceID;
    this.context = context;
    this.dataListItem = dataListItem;
  }

  @Override
  public View getView(final int position, View convertView, ViewGroup parent) {
    View row = convertView;
    CustomerHolder holder = null;
    if (row == null) {
      LayoutInflater inflater = ((Activity) context).getLayoutInflater();
      row = inflater.inflate(layoutResourceID, parent, false);
      holder = new CustomerHolder();
      holder.custID = (TextView) row.findViewById(R.id.custID_listItem);
      holder.custFName = (TextView) row.findViewById(R.id.custFName_listItem);
      holder.custLName = (TextView) row.findViewById(R.id.custLName_listItem);
      holder.custEmail = (TextView) row.findViewById(R.id.custEmail_listItem);
      holder.custPhone = (TextView) row.findViewById(R.id.custPhone_listItem);
      holder.custStreet = (TextView) row.findViewById(R.id.custStreet_listItem);
      holder.custCity = (TextView) row.findViewById(R.id.custCity_listItem);
      holder.custState = (TextView) row.findViewById(R.id.custState_listItem);
      holder.custZip = (TextView) row.findViewById(R.id.custZip_listItem);
      holder.editButton = (Button) row.findViewById(R.id.custEdit_listButton);
      // another line for the button if it has to go here. holder.button.
      row.setTag(holder);
    } else {
      holder = (CustomerHolder) row.getTag();
    }
    // setting the holders variables for the array list and the individual lines for each customer based on the holder's position and tag
    holder.customer = dataListItem.get(position);
    holder.cust_id = dataListItem.get(position).getCustomerID();
    holder.cust_first = dataListItem.get(position).getCustomerFirstName();
    holder.cust_last = dataListItem.get(position).getCustomerLastName();
    holder.cust_email = dataListItem.get(position).getCustomerEmail();
    holder.cust_phone = dataListItem.get(position).getCustomerPhone();
    holder.cust_street = dataListItem.get(position).getCustomerStreet();
    holder.cust_city = dataListItem.get(position).getCustomerCity();
    holder.cust_state = dataListItem.get(position).getCustomerState();
    holder.cust_zip = dataListItem.get(position).getCustomerZipcode();
    // setting the text to show based on the holder's list position and tag.
    holder.custID.setText(String.valueOf(holder.cust_id));
    holder.custFName.setText(holder.cust_first);
    holder.custLName.setText(holder.cust_last);
    holder.custEmail.setText(holder.cust_email);
    holder.custPhone.setText(holder.cust_phone);
    holder.custStreet.setText(holder.cust_street);
    holder.custCity.setText(holder.cust_city);
    holder.custState.setText(holder.cust_state);
    holder.custZip.setText(holder.cust_zip);
    // Listener for the view based on the holder's list position and tag.
    holder.editButton.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        // Toast.makeText((CustomerSearchActivity)getContext(), String.valueOf(dataListItem.get(position).getCustomerFirstName()), Toast.LENGTH_SHORT).show();
        CustomerSearchActivity se = (CustomerSearchActivity)getContext();
        se.getFromAdapter(dataListItem.get(position)); // Maybe good to learn more about the positions, tags, and the holder class for custom adapters in general.
      }
    });
    return row;
  }

  // Not sure yet if all these fields are actually needed to get the info in other classes based on the holder being the tag for each list object.
  public static final class CustomerHolder {
    Customer customer;
    Long cust_id;
    String cust_first;
    String cust_last;
    String cust_email;
    String cust_phone;
    String cust_street;
    String cust_city;
    String cust_state;
    String cust_zip;
    TextView custID;
    TextView custFName;
    TextView custLName;
    TextView custEmail;
    TextView custPhone;
    TextView custStreet;
    TextView custCity;
    TextView custState;
    TextView custZip;
    Button editButton;
  }
} // ToDo Need the edit button to grab the data and send to the AddEdit Activity. Maybe call a method from the search Activity??
  /*public CustomerAdapter(Context context, ArrayList<Customer> customers) {
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
        Intent sendCustomer = new Intent(getContext(), CustomerAddEditActivity.class);         //new Intent(getContext(), CustomerAddEditActivity.class);
        ((CustomerSearchActivity)getContext()).setResult(Activity.RESULT_OK, sendCustomer);
        sendCustomer.putExtra(BaseColumns._ID, String.valueOf(_id));
        sendCustomer.putExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME, firstName);
        sendCustomer.putExtra(DatabaseContract.CustomerColumns.COLUMN_LAST_NAME, lastName);
        sendCustomer.putExtra(DatabaseContract.CustomerColumns.COLUMN_EMAIL, email);
        sendCustomer.putExtra(DatabaseContract.CustomerColumns.COLUMN_PHONE, phone);
        sendCustomer.putExtra(DatabaseContract.CustomerColumns.COLUMN_STREET, street);
        sendCustomer.putExtra(DatabaseContract.CustomerColumns.COLUMN_CITY, city);
        sendCustomer.putExtra(DatabaseContract.CustomerColumns.COLUMN_STATE, state);
        sendCustomer.putExtra(DatabaseContract.CustomerColumns.COLUMN_ZIPCODE, zipCode);
        Log.v("CSADResult", sendCustomer.toString());
        Log.v("CSADResult", sendCustomer.getStringExtra(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME.toString()));
        //getContext().startActivity(sendCustomer);
        // Need back stack help here. This works  for now but probably not optimized. Should depend on where the search activity is called from too.
        ((CustomerSearchActivity)getContext()).finish();
      }
    });
    return convertView;*/
  //}
