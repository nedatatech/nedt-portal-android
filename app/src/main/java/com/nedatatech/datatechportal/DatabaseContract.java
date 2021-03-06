package com.nedatatech.datatechportal;


import android.provider.BaseColumns;

// Having this whole class is supposed to help the android framework work better with things like query results
// and the count of the results, also content providers and cursor adapters.
// Will end up creating more inner classes here when setting up new tables and their column names.
public class DatabaseContract {

  private DatabaseContract() { // Constructor is private so it's not accidentally instantiated.
  }

  public static class CustomerColumns implements BaseColumns {

    // The name of the Database table.
    public static final String TABLE_CUSTOMERS = "customers";
    // This set of strings defines the column identifiers for the people table. Changing columns identifiers or adding/removing any of them should start here.
    // public static final String COLUMN_ID = _ID; // Possibly need to use and change everywhere like this for the adapter to work better in list views.
    public static final String COLUMN_FIRST_NAME = "first_name";
    public static final String COLUMN_LAST_NAME = "last_name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PHONE = "phone";
    public static final String COLUMN_STREET = "street";
    public static final String COLUMN_CITY = "city";
    public static final String COLUMN_STATE = "state";
    public static final String COLUMN_ZIPCODE = "zip_code";
    // public static final String COLUMN_NOTES = "notes"; // ToDo Will need to add this line and make necessary adjustments. Layouts, Objects, and DB Methods.
    // Radio Button for wether work has been done or not may be helpful. Also will need to be able to list the jobs from that table based on a customer.

    /* This String is to help simplify if we need to change the code for when we actually create the table or upgrade it.
   Autoincrement is only necessary to not reuse id's from previously deleted rows. Not as efficient as not declaring it. Will leave empty spaces
   and make a larger file size. Have read that it's also slower. Database Vacuum function can help with these situations.*/
    public static final String CREATE_CUSTOMER_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_CUSTOMERS + " (" + BaseColumns._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + COLUMN_FIRST_NAME + " TEXT, " + COLUMN_LAST_NAME + " TEXT, " + COLUMN_EMAIL + " TEXT, " + COLUMN_PHONE + " TEXT, "
            + COLUMN_STREET + " TEXT, " + COLUMN_CITY + " TEXT, " + COLUMN_STATE + " TEXT, " + COLUMN_ZIPCODE + " TEXT)";
    // Should zip be an integer? Also May want to be able to leave more columns able to be null. Name and Phone are definitely necessities.

  }
}
