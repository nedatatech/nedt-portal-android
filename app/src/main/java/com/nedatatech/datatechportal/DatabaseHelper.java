package com.nedatatech.datatechportal;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DatabaseHelper extends SQLiteOpenHelper {

  //Todo: Go through this class and check for things that may need to be done or improved.

  public static final String logTag = "HELPER_SYSTEM"; // Debug info.

  // These strings are for Database/Table set up.
  private static final String DATABASE_NAME = "DataTechRecords.db"; // ToDo WHY IS THE NAME CAUSING AN ISSUE ON PHONES??
  private static final int DATABASE_VERSION = 1;

  // This method is required for this class to help with creating the database. LEARN MORE ABOUT THIS.
  public DatabaseHelper(Context context) {
    super(context, DATABASE_NAME, null, DATABASE_VERSION);
    Log.i(logTag, "Name and Version set-up"); // Debug
  }

  // This method is required for this class to help with creating the table. PROBABLY WANT TO IMPROVE.
  @Override
  public void onCreate(SQLiteDatabase db) {
    db.execSQL(DatabaseContract.CustomerColumns.CREATE_CUSTOMER_TABLE);
    Log.i(logTag, "TABLE HAS BEEN CREATED"); // Debug
  }

  /*This method is required for this class to help with upgrading the table on an app update.
  Will only call when database version is manually changed here in code. PROBABLY WANT TO IMPROVE.*/
  @Override
  public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    // Have read that some logging is recommended here. Also likely better to use an if to see if the database version has changed.
    Log.i(logTag, "TABLE UPGRADE ACCESSED"); // Debug
    db.execSQL("DROP TABLE IF EXISTS " + DatabaseContract.CustomerColumns.TABLE_CUSTOMERS + ";"); //ToDo Should use alter table instead if data needs to persist when upgraded.
    db.execSQL(DatabaseContract.CustomerColumns.CREATE_CUSTOMER_TABLE);
  }
}
