package com.nedatatech.datatechportal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

  //Todo: Go through this class and check for things that may need to be done or improved.

  public static final String logTag = "HELPER_SYSTEM"; // Debug info.

  // These strings are for Database/Table set up.
  private static final String DATABASE_NAME = "DataTechRecords.db";
  private static final int DATABASE_VERSION = 1;

  public static final String TABLE_CUSTOMERS = "customers";
  // This set of strings defines the column identifiers for the people table. Changing columns identifiers or adding/removing any of them should start here.
  public static final String COLUMN_ID = "customer_id";
  public static final String COLUMN_FIRST_NAME = "first_name";
  public static final String COLUMN_LAST_NAME = "last_name";
  public static final String COLUMN_EMAIL = "email";
  public static final String COLUMN_PHONE = "phone";
  public static final String COLUMN_STREET = "street";
  public static final String COLUMN_CITY = "city";
  public static final String COLUMN_STATE = "state";
  public static final String COLUMN_ZIPCODE = "zip_code";

  /* This String is to help simplify if we need to change the code for when we actually create the table or upgrade it.
   Autoincrement is only necessary to not reuse id's from previously deleted rows. Not as efficient as not declaring it. Will leave empty spaces
   and make a larger file size. Have read that it's also slower. Database Vacuum function can help with these situations.*/
  private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_CUSTOMERS + " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
          + COLUMN_FIRST_NAME + " TEXT NOT NULL , " + COLUMN_LAST_NAME + " TEXT NOT NULL, " + COLUMN_EMAIL + " TEXT, " + COLUMN_PHONE + " TEXT NOT NULL, "
          + COLUMN_STREET + " TEXT NOT NULL, " + COLUMN_CITY + " TEXT NOT NULL, " + COLUMN_STATE + " TEXT NOT NULL, " + COLUMN_ZIPCODE + " TEXT)";
  // Should zip be an integer?

  // This method is required for this class to help with creating the database. LEARN MORE ABOUT THIS.
  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    Log.i(logTag, "Name and Version set-up"); // Debug
  }

  // This method is required for this class to help with creating the table. PROBABLY WANT TO IMPROVE.
  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(CREATE_TABLE);
    Log.i(logTag, "Table has been created"); // Debug
  }

  /*This method is required for this class to help with upgrading the table on an app update.
  Will only call when database version is manually changed here in code. PROBABLY WANT TO IMPROVE.*/
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Have read that some logging is recommended here. Also likely better to use an if to see if the database version has changed.
    db.execSQL("DROP TABLE IF EXISTS " + TABLE_CUSTOMERS + ";"); //ToDo Should use alter table instead if data needs to persist when upgraded.
    db.execSQL(CREATE_TABLE);
  }
}
