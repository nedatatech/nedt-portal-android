package com.nedatatech.datatechportal;

import android.util.Log;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

// This class will set up and use our specific database operations.
public class DatabaseOperations {

  //Todo: Go through this class and check for things that may need to be done or improved.

  public static final String logTag = "DB_OPERATION_SYSTEM"; // Debug info.

  private SQLiteOpenHelper dbHelper; /* This can be TestDBHelper dbHelper or the system default SQLiteOpenHelper dbHelper. Not sure what the difference is.
                                            Maybe either just methods used in TestDBHelper class or all from SQLiteOpenHelper and the overridden ones in TestDBHelper.*/
  private SQLiteDatabase database;
  private String[] CUSTOMER_ALL_COLUMNS = {DatabaseHelper.COLUMN_ID, DatabaseHelper.COLUMN_FIRST_NAME, DatabaseHelper.COLUMN_LAST_NAME,
          DatabaseHelper.COLUMN_EMAIL, DatabaseHelper.COLUMN_PHONE, DatabaseHelper.COLUMN_STREET, DatabaseHelper.COLUMN_CITY,
          DatabaseHelper.COLUMN_STATE, DatabaseHelper.COLUMN_ZIPCODE};

  public DatabaseOperations(Context context) {
    Log.i(logTag, "Helper class has been accessed"); // Debug info.
    dbHelper = new DatabaseHelper(context); // Can this be getContext() instead of just context??
  }

  public void openDB() {
    Log.i(logTag, " Test Database is opened and accessible"); // Debug info.
    database = dbHelper.getWritableDatabase();
  }

  public void closeDB() {
    Log.i(logTag, " Test Database is closed and inaccessible"); // Debug info.
    dbHelper.close();
  }

  public Customer addCustomer(Customer customer) {
    ContentValues values = new ContentValues();
    //values.put(DatabaseHelper.COLUMN_ID, customer.getCustomerID()); // Not gonna need this unless its decided that we want to be able to set our own id's separate from the primary keys.
    values.put(DatabaseHelper.COLUMN_FIRST_NAME, customer.getCustomerFirstName());
    values.put(DatabaseHelper.COLUMN_LAST_NAME, customer.getCustomerLastName());
    values.put(DatabaseHelper.COLUMN_EMAIL, customer.getCustomerEmail());
    values.put(DatabaseHelper.COLUMN_PHONE, customer.getCustomerPhone());
    values.put(DatabaseHelper.COLUMN_STREET, customer.getCustomerStreet());
    values.put(DatabaseHelper.COLUMN_CITY, customer.getCustomerCity());
    values.put(DatabaseHelper.COLUMN_STATE, customer.getCustomerState());
    values.put(DatabaseHelper.COLUMN_ZIPCODE, customer.getCustomerZipcode());
    long insertID = database.insert(DatabaseHelper.TABLE_CUSTOMERS, null, values); // I think the insert method is where I can edit ways to handle empty inputs for columns that allow it.
    customer.setCustomerID(insertID);
    return customer;
  }

  //ToDo Need to create more methods for searching based on more criteria and being able to return results for more than one match.
  // Finds and returns one search result. Should probably close the cursor in each method also.
  public Customer getCustomer(long searchID) { // Check here for a search error when testing. Spaces around ? may not be necessary.
    Cursor cursor = database.query(DatabaseHelper.TABLE_CUSTOMERS, CUSTOMER_ALL_COLUMNS, DatabaseHelper.COLUMN_ID + " = ?",
            new String[]{String.valueOf(searchID)}, null, null, null, null);
    if (cursor != null) {
      cursor.moveToFirst();
    }
    // Research this for more understanding.
    Customer customerResult = new Customer(Long.parseLong(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
            cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
    cursor.close(); // Is closing the cursor here the proper place? Likely not when searching for multiple results.
    return customerResult;
  }

  // Finds and returns the entire table in a list. Could be an error here when checking for column indexes.
  public List<Customer> getAllCustomers() {
    Cursor cursor = database.query(DatabaseHelper.TABLE_CUSTOMERS, CUSTOMER_ALL_COLUMNS, null, null, null, null, null);

    List<Customer> customerList = new ArrayList<>();
    if (cursor.getCount() > 0) {
      while (cursor.moveToNext()) {
        Customer customer = new Customer();
        customer.setCustomerID(cursor.getLong(cursor.getColumnIndex(DatabaseHelper.COLUMN_ID)));
        customer.setCustomerFirstName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_FIRST_NAME)));
        customer.setCustomerLastName(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_LAST_NAME)));
        customer.setCustomerEmail(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_EMAIL)));
        customer.setCustomerPhone(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_PHONE)));
        customer.setCustomerStreet(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_STREET)));
        customer.setCustomerCity(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_CITY)));
        customer.setCustomerState(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_STATE)));
        customer.setCustomerZipcode(cursor.getString(cursor.getColumnIndex(DatabaseHelper.COLUMN_ZIPCODE)));
        customerList.add(customer);
      }
    } cursor.close(); // Is closing the cursor here the proper place?
    return customerList;
  }

  // Updates an already existing entry in the database.
  public int updateCustomer(Customer customer) {
    ContentValues values = new ContentValues();
    values.put(DatabaseHelper.COLUMN_FIRST_NAME, customer.getCustomerFirstName());
    values.put(DatabaseHelper.COLUMN_LAST_NAME, customer.getCustomerLastName());
    values.put(DatabaseHelper.COLUMN_EMAIL, customer.getCustomerEmail());
    values.put(DatabaseHelper.COLUMN_PHONE, customer.getCustomerPhone());
    values.put(DatabaseHelper.COLUMN_STREET, customer.getCustomerStreet());
    values.put(DatabaseHelper.COLUMN_CITY, customer.getCustomerCity());
    values.put(DatabaseHelper.COLUMN_STATE, customer.getCustomerState());
    values.put(DatabaseHelper.COLUMN_ZIPCODE, customer.getCustomerZipcode());

    return database.update(DatabaseHelper.TABLE_CUSTOMERS, values,
            DatabaseHelper.COLUMN_ID + " = ? ", new String[]{String.valueOf(customer.getCustomerID())});
  }

  // Delete method here.
  public void removeCustomer(Customer customer) {
    database.delete(DatabaseHelper.TABLE_CUSTOMERS, DatabaseHelper.COLUMN_ID + " = " + customer.getCustomerID(), null);
  }
}
