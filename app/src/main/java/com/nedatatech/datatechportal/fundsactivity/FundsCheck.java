package com.nedatatech.datatechportal.fundsactivity;

import android.content.ContentValues;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nedatatech.datatechportal.DatabaseContract;
import com.nedatatech.datatechportal.DatabaseOperations;
import com.nedatatech.datatechportal.R;
import com.nedatatech.datatechportal.baseactivity.BaseActivity;

import static android.database.DatabaseUtils.dumpCursor;

public class FundsCheck extends BaseActivity {

  private Button buttonCancel;
  private Button buttonSave;
  private DatabaseOperations dataOps;
  private String transType;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_funds_new);

    Intent intent = getIntent();

    transType = intent.getStringExtra("TransType");


    //final ContentValues oldBalances = loadBalances();

//    String inventoryAcctBal = oldBalances.get("inventoryAcctBal").toString();
//    String miscAcctBal = oldBalances.get("miscAcctBal").toString();

    buttonCancel = (Button) findViewById(R.id.btnCancel);
    buttonCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    buttonSave = (Button) findViewById(R.id.btnSave);
    buttonSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dataOps = new DatabaseOperations(FundsCheck.this);
        ContentValues oldBalances = loadBalances();
        dataOps.openDB();


        //dataOps.addTokenToDB(auth_token);
        //EditText editText = (EditText) findViewById(R.id.editTextPriority);
        //String priority = String.valueOf(npPriority.getValue());
        ContentValues transactions = new ContentValues();

        EditText etInventoryCost = (EditText) findViewById(R.id.etInventoryCost);
        String inventoryOwed = etInventoryCost.getText().toString();
        transactions.put(DatabaseContract.FundsHistoryColumns.COLUMN_INVENTORY_ACCT_OWED, Integer.valueOf(etInventoryCost.getText().toString()));

        EditText etFuel = (EditText) findViewById(R.id.etFuel);
        String fuelAcctOwed = etFuel.getText().toString();
        transactions.put(DatabaseContract.FundsHistoryColumns.COLUMN_FUEL_ACCT_OWED, Integer.valueOf(etFuel.getText().toString()));

        EditText etMiscAcctPer = (EditText) findViewById(R.id.etMiscAcctPercentage);
        String miscAcctPer = etMiscAcctPer.getText().toString();
        transactions.put(DatabaseContract.FundsHistoryColumns.COLUMN_MISC_ACCT_PERCENT, Double.valueOf(etMiscAcctPer.getText().toString()));

        EditText etTotalRec = (EditText) findViewById(R.id.etTotalRec);
        String totalRec = etTotalRec.getText().toString();
        transactions.put(DatabaseContract.FundsHistoryColumns.COLUMN_CUST_PAID, Integer.valueOf(etTotalRec.getText().toString()));

        ContentValues values = processAmtRec(transactions, oldBalances);//processAmtRec(Short.valueOf(totalRec),Short.valueOf(fuelAcctOwed), Short.valueOf(inventoryOwed), Double.valueOf(miscAcctPer));

        ContentValues newBalances = updateBalances(oldBalances, values);
        dataOps.storeTransaction(transType,transactions, newBalances, oldBalances);

        /*
        if(dataOps.getSingleFundsItem("1").getCount()>0){
          //PID Found
          ContentValues newBalances = updateBalances(oldBalances, values);
          dataOps.updateFundsItem(newBalances, "1");
          ContentValues historyItem = new ContentValues();
          //historyItem = parseTransactionsToContentValues(transactions);
          dataOps.storeTransaction(transactions,newBalances,oldBalances);
        }else{
          dataOps.saveFundsItem(values);
          //dataOps.storeTransaction();
        }
        */

        dataOps.closeDB();
        finish();
      }
    });
  }

  public ContentValues processAmtRec(ContentValues transaction, ContentValues oldBalances){ //(short totalRec, short fuelAcctOwed, short inventoryAcctOwed, double miscAcctPer){

    double balance = Integer.valueOf(transaction.get(DatabaseContract.FundsHistoryColumns.COLUMN_CUST_PAID).toString());
    balance = balance - Integer.valueOf(transaction.get(DatabaseContract.FundsHistoryColumns.COLUMN_INVENTORY_ACCT_OWED).toString());
    balance = balance - Integer.valueOf(transaction.get(DatabaseContract.FundsHistoryColumns.COLUMN_FUEL_ACCT_OWED).toString());
    double miscAcctOwed =  balance * (Double.valueOf(transaction.get(DatabaseContract.FundsHistoryColumns.COLUMN_MISC_ACCT_PERCENT).toString())/100);
    balance = balance - miscAcctOwed;
    double daveOwed = balance / 2;
    double timOwed = balance /2;

    ContentValues balances = new ContentValues();
    balances.put(DatabaseContract.FundsHistoryColumns.COLUMN_DAVE_BAL, daveOwed);
    balances.put(DatabaseContract.FundsHistoryColumns.COLUMN_TIM_BAL, timOwed);
    balances.put(DatabaseContract.FundsHistoryColumns.COLUMN_INVENTORY_ACCT_BAL, transaction.get(DatabaseContract.FundsHistoryColumns.COLUMN_INVENTORY_ACCT_OWED).toString());
    balances.put(DatabaseContract.FundsHistoryColumns.COLUMN_MISC_ACCT_BAL, miscAcctOwed);
    balances.put(DatabaseContract.FundsHistoryColumns.COLUMN_FUEL_ACCT_BAL, transaction.get(DatabaseContract.FundsHistoryColumns.COLUMN_FUEL_ACCT_OWED).toString());
  return balances;


  }



  public ContentValues updateBalances(ContentValues oldBalances, ContentValues values){
    double daveTotalOwed = Double.valueOf(oldBalances.get(DatabaseContract.FundsHistoryColumns.COLUMN_DAVE_BAL).toString());
    daveTotalOwed = daveTotalOwed + Double.valueOf(values.get(DatabaseContract.FundsHistoryColumns.COLUMN_DAVE_BAL).toString());

    double timTotalOwed = Double.valueOf(oldBalances.get(DatabaseContract.FundsHistoryColumns.COLUMN_TIM_BAL).toString());
    timTotalOwed = timTotalOwed + Double.valueOf(values.get(DatabaseContract.FundsHistoryColumns.COLUMN_TIM_BAL).toString());

    int inventoryTotalOwed = Integer.valueOf(oldBalances.get(DatabaseContract.FundsHistoryColumns.COLUMN_INVENTORY_ACCT_BAL).toString());
    inventoryTotalOwed = inventoryTotalOwed + Integer.valueOf(values.get(DatabaseContract.FundsHistoryColumns.COLUMN_INVENTORY_ACCT_BAL).toString());

    double miscTotalOwed = Double.valueOf(oldBalances.get(DatabaseContract.FundsHistoryColumns.COLUMN_MISC_ACCT_BAL).toString());
    miscTotalOwed = miscTotalOwed + Double.valueOf(values.get(DatabaseContract.FundsHistoryColumns.COLUMN_MISC_ACCT_BAL).toString());

    int fuelTotalOwed = Integer.valueOf(oldBalances.get(DatabaseContract.FundsHistoryColumns.COLUMN_FUEL_ACCT_BAL).toString());
    fuelTotalOwed = fuelTotalOwed + Integer.valueOf(values.get(DatabaseContract.FundsHistoryColumns.COLUMN_FUEL_ACCT_BAL).toString());

    ContentValues newBalances = new ContentValues();
    newBalances.put(DatabaseContract.FundsHistoryColumns.COLUMN_DAVE_BAL, daveTotalOwed);
    newBalances.put(DatabaseContract.FundsHistoryColumns.COLUMN_TIM_BAL, timTotalOwed);
    newBalances.put(DatabaseContract.FundsHistoryColumns.COLUMN_INVENTORY_ACCT_BAL,inventoryTotalOwed);
    newBalances.put(DatabaseContract.FundsHistoryColumns.COLUMN_MISC_ACCT_BAL, miscTotalOwed);
    newBalances.put(DatabaseContract.FundsHistoryColumns.COLUMN_FUEL_ACCT_BAL, fuelTotalOwed);
    return newBalances;

  }

  public ContentValues parseTransactionsToContentValues(ContentValues transactions){
    ContentValues historyItem = new ContentValues();
    historyItem.put(DatabaseContract.FundsHistoryColumns.COLUMN_TRANS_DATE, "12/11/17");
    historyItem.put(DatabaseContract.FundsHistoryColumns.COLUMN_TRANS_TYPE, transType);
    historyItem.putAll(transactions);
    //historyItem.put(DatabaseContract.FundsHistoryColumns.COLUMN_CUST_PAID, transactions.get(DatabaseContract ));
    //historyItem.put(transactions.get(DatabaseContract.FundsDataColumns.));
    return historyItem;
  }

}
