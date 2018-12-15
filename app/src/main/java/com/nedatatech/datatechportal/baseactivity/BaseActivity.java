package com.nedatatech.datatechportal.baseactivity;

import android.app.Activity;
import android.content.ContentValues;
import android.text.format.DateFormat;

import com.nedatatech.datatechportal.DatabaseContract;
import com.nedatatech.datatechportal.DatabaseOperations;

public abstract class BaseActivity extends Activity {

/*
====================================================================================================
======================================= Funds Operations ===========================================
VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
*/

  public void savePayment(){
    DatabaseOperations dataOps;
    dataOps = new DatabaseOperations(this);
    dataOps.openDB();
    dataOps.getAllFundsItems();
    dataOps.closeDB();
  }

  public ContentValues loadBalances(){
    DatabaseOperations dataOps;
    ContentValues oldBalances = new ContentValues();

    dataOps = new DatabaseOperations(this);
    dataOps.openDB();

    //dataOps.fundsCursor = dataOps.getSingleFundsItem("1");
    dataOps.fundsCursor = dataOps.getAllFundsItems();


    if (dataOps.fundsCursor.moveToLast()) {
      String inventoryAcctBal = dataOps.fundsCursor.getString(dataOps.fundsCursor.getColumnIndexOrThrow(DatabaseContract.FundsHistoryColumns.COLUMN_INVENTORY_ACCT_BAL));
      String miscAcctBal = dataOps.fundsCursor.getString(dataOps.fundsCursor.getColumnIndexOrThrow(DatabaseContract.FundsHistoryColumns.COLUMN_MISC_ACCT_BAL));
      String daveAcctBal = dataOps.fundsCursor.getString(dataOps.fundsCursor.getColumnIndexOrThrow(DatabaseContract.FundsHistoryColumns.COLUMN_DAVE_BAL));
      String timAcctBal = dataOps.fundsCursor.getString(dataOps.fundsCursor.getColumnIndexOrThrow(DatabaseContract.FundsHistoryColumns.COLUMN_TIM_BAL));
      String fuelAcctBal = dataOps.fundsCursor.getString(dataOps.fundsCursor.getColumnIndexOrThrow(DatabaseContract.FundsHistoryColumns.COLUMN_FUEL_ACCT_BAL));
      //dumpCursor(dataOps.fundsCursor);
      oldBalances.put(DatabaseContract.FundsHistoryColumns.COLUMN_INVENTORY_ACCT_BAL, replaceNull(inventoryAcctBal));
      oldBalances.put(DatabaseContract.FundsHistoryColumns.COLUMN_MISC_ACCT_BAL, replaceNull(miscAcctBal));
      oldBalances.put(DatabaseContract.FundsHistoryColumns.COLUMN_DAVE_BAL, replaceNull(daveAcctBal));
      oldBalances.put(DatabaseContract.FundsHistoryColumns.COLUMN_TIM_BAL, replaceNull(timAcctBal));
      oldBalances.put(DatabaseContract.FundsHistoryColumns.COLUMN_FUEL_ACCT_BAL, replaceNull(fuelAcctBal));
    }else {
      oldBalances.put(DatabaseContract.FundsHistoryColumns.COLUMN_INVENTORY_ACCT_BAL, "0");
      oldBalances.put(DatabaseContract.FundsHistoryColumns.COLUMN_MISC_ACCT_BAL, replaceNull("0"));
      oldBalances.put(DatabaseContract.FundsHistoryColumns.COLUMN_DAVE_BAL, replaceNull("0"));
      oldBalances.put(DatabaseContract.FundsHistoryColumns.COLUMN_TIM_BAL, replaceNull("0"));
      oldBalances.put(DatabaseContract.FundsHistoryColumns.COLUMN_FUEL_ACCT_BAL, replaceNull("0"));
    }
    dataOps.closeDB();

    return oldBalances;
  }

  //public ContentValues updateBalances(ContentValues oldBalances, ContentValues newBalances){


  //  return updatedBalances;
  //}

/*
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
======================================= Funds Operations ===========================================
====================================================================================================
====================================================================================================
======================================= Common Operations ==========================================
VVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVVV
*/

  public String currentDate(){return (DateFormat.format("dd-MM-yyyy hh:mm:ss", new java.util.Date()).toString());}

  public static String replaceNull(String input) {return input == null ? "0" : input;}

  public void cancel(){finish();}

  public void storeTransaction(ContentValues transaction){
    DatabaseOperations dataOps;
    dataOps = new DatabaseOperations(this);
    dataOps.openDB();
    dataOps.saveTransaction(transaction);
    dataOps.closeDB();
  }

/*
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
======================================= Common Operations ==========================================
====================================================================================================
*/

}
