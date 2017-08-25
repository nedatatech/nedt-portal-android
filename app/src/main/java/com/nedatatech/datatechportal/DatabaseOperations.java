package com.nedatatech.datatechportal;

import android.provider.BaseColumns;
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
  private String[] CUSTOMER_ALL_COLUMNS = {BaseColumns._ID, DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME,
          DatabaseContract.CustomerColumns.COLUMN_LAST_NAME, DatabaseContract.CustomerColumns.COLUMN_EMAIL,
          DatabaseContract.CustomerColumns.COLUMN_PHONE, DatabaseContract.CustomerColumns.COLUMN_STREET,
          DatabaseContract.CustomerColumns.COLUMN_CITY, DatabaseContract.CustomerColumns.COLUMN_STATE,
          DatabaseContract.CustomerColumns.COLUMN_ZIPCODE};

  public DatabaseOperations(Context context) {
    Log.i(logTag, "Helper class has been accessed"); // Debug info.
    dbHelper = new DatabaseHelper(context);
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
    // May need some lines for an ID we can set and likely to add a notes column too. Will need to update everywhere if so.
    values.put(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME, customer.getCustomerFirstName());
    values.put(DatabaseContract.CustomerColumns.COLUMN_LAST_NAME, customer.getCustomerLastName());
    values.put(DatabaseContract.CustomerColumns.COLUMN_EMAIL, customer.getCustomerEmail());
    values.put(DatabaseContract.CustomerColumns.COLUMN_PHONE, customer.getCustomerPhone());
    values.put(DatabaseContract.CustomerColumns.COLUMN_STREET, customer.getCustomerStreet());
    values.put(DatabaseContract.CustomerColumns.COLUMN_CITY, customer.getCustomerCity());
    values.put(DatabaseContract.CustomerColumns.COLUMN_STATE, customer.getCustomerState());
    values.put(DatabaseContract.CustomerColumns.COLUMN_ZIPCODE, customer.getCustomerZipcode());
    long insertID = database.insert(DatabaseContract.CustomerColumns.TABLE_CUSTOMERS, null, values); // I think the insert method is where I can edit ways to handle empty inputs for columns that allow it.
    customer.setCustomerID(insertID);
    return customer;
  }

  //ToDo Do testing on the delete related code to see if this is even needed now that the new search method works for any criteria.
  // Finds and returns one search result by Primary Key only.
  public Customer getCustomer(long searchID) { // Needed until final decision on handling delete.
    Cursor cursor = database.query(DatabaseContract.CustomerColumns.TABLE_CUSTOMERS, CUSTOMER_ALL_COLUMNS, BaseColumns._ID + " = ?",
            new String[]{String.valueOf(searchID)}, null, null, null, null);
    if (cursor != null) {
      cursor.moveToFirst();
    }
    // Research this for more understanding.
    Customer customerResult = new Customer(Long.parseLong(cursor.getString(0)), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4),
            cursor.getString(5), cursor.getString(6), cursor.getString(7), cursor.getString(8));
    cursor.close();
    return customerResult;
  }

  // Search method that returns results based on any columns fields and returns any rows that contain a match.
  public ArrayList<Customer> searchCustomers(String columnType, String searchInput) {
    Cursor cursor = database.query(DatabaseContract.CustomerColumns.TABLE_CUSTOMERS, CUSTOMER_ALL_COLUMNS,
            columnType + " LIKE '%" + searchInput + "%'", null, null, null, null, null);
    ArrayList<Customer> customerList = new ArrayList<>();
    if (cursor.getCount() > 0) {
      int cursorCount = cursor.getCount();
      for(int i = 1; i <= cursorCount; i++) {
        cursor.moveToNext();
        Customer customer = new Customer();
        customer.setCustomerID(cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)));
        customer.setCustomerFirstName(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME)));
        customer.setCustomerLastName(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerColumns.COLUMN_LAST_NAME)));
        customer.setCustomerEmail(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerColumns.COLUMN_EMAIL)));
        customer.setCustomerPhone(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerColumns.COLUMN_PHONE)));
        customer.setCustomerStreet(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerColumns.COLUMN_STREET)));
        customer.setCustomerCity(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerColumns.COLUMN_CITY)));
        customer.setCustomerState(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerColumns.COLUMN_STATE)));
        customer.setCustomerZipcode(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerColumns.COLUMN_ZIPCODE)));
        customerList.add(customer);
        Log.v(logTag, "Added Customer to Array");
      }
    } cursor.close();
   return customerList;
  }

  // Finds and returns the entire table in a list.
  public List<Customer> getAllCustomers() {
    Cursor cursor = database.query(DatabaseContract.CustomerColumns.TABLE_CUSTOMERS, CUSTOMER_ALL_COLUMNS, null, null, null, null, null);

    List<Customer> customerList = new ArrayList<>();
    if (cursor.getCount() > 0) {
      while (cursor.moveToNext()) {
        Customer customer = new Customer();
        customer.setCustomerID(cursor.getLong(cursor.getColumnIndex(BaseColumns._ID)));
        customer.setCustomerFirstName(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME)));
        customer.setCustomerLastName(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerColumns.COLUMN_LAST_NAME)));
        customer.setCustomerEmail(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerColumns.COLUMN_EMAIL)));
        customer.setCustomerPhone(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerColumns.COLUMN_PHONE)));
        customer.setCustomerStreet(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerColumns.COLUMN_STREET)));
        customer.setCustomerCity(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerColumns.COLUMN_CITY)));
        customer.setCustomerState(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerColumns.COLUMN_STATE)));
        customer.setCustomerZipcode(cursor.getString(cursor.getColumnIndex(DatabaseContract.CustomerColumns.COLUMN_ZIPCODE)));
        customerList.add(customer);
      }
    } cursor.close();
    return customerList;
  }

  // Updates an already existing entry in the database.
  public int updateCustomer(Customer customer) {
    ContentValues values = new ContentValues();
    values.put(DatabaseContract.CustomerColumns.COLUMN_FIRST_NAME, customer.getCustomerFirstName());
    values.put(DatabaseContract.CustomerColumns.COLUMN_LAST_NAME, customer.getCustomerLastName());
    values.put(DatabaseContract.CustomerColumns.COLUMN_EMAIL, customer.getCustomerEmail());
    values.put(DatabaseContract.CustomerColumns.COLUMN_PHONE, customer.getCustomerPhone());
    values.put(DatabaseContract.CustomerColumns.COLUMN_STREET, customer.getCustomerStreet());
    values.put(DatabaseContract.CustomerColumns.COLUMN_CITY, customer.getCustomerCity());
    values.put(DatabaseContract.CustomerColumns.COLUMN_STATE, customer.getCustomerState());
    values.put(DatabaseContract.CustomerColumns.COLUMN_ZIPCODE, customer.getCustomerZipcode());

    return database.update(DatabaseContract.CustomerColumns.TABLE_CUSTOMERS, values,
            BaseColumns._ID + " = ? ", new String[]{String.valueOf(customer.getCustomerID())});
  }

  // Delete method here.
  public void removeCustomer(Customer customer) {
    database.delete(DatabaseContract.CustomerColumns.TABLE_CUSTOMERS, BaseColumns._ID + " = " + customer.getCustomerID(), null);
  }
}
