package com.nedatatech.datatechportal.FundsActivity;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nedatatech.datatechportal.DatabaseContract;
import com.nedatatech.datatechportal.DatabaseOperations;
import com.nedatatech.datatechportal.R;

import static android.database.DatabaseUtils.dumpCursor;

public class FundsNew extends AppCompatActivity {

  private Button buttonCancel;
  private Button buttonSave;
  private DatabaseOperations dataOps;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_funds_new);


    final ContentValues oldBalances = loadBalances();

//    String inventoryAcctBal = oldBalances.get("inventoryAcctBal").toString();
//    String miscAcctBal = oldBalances.get("miscAcctBal").toString();

    buttonCancel = (Button) findViewById(R.id.buttonCancel);
    buttonCancel.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        finish();
      }
    });

    buttonSave = (Button) findViewById(R.id.buttonSave);
    buttonSave.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        dataOps = new DatabaseOperations(FundsNew.this);
        dataOps.openDB();


        //dataOps.addTokenToDB(auth_token);
        //EditText editText = (EditText) findViewById(R.id.editTextPriority);
        //String priority = String.valueOf(npPriority.getValue());
        ContentValues transactions = new ContentValues();

        EditText etInventoryCost = (EditText) findViewById(R.id.etInventoryCost);
        String inventoryOwed = etInventoryCost.getText().toString();
        transactions.put("inventoryAcctOwed", Integer.valueOf(etInventoryCost.getText().toString()));

        EditText etFuel = (EditText) findViewById(R.id.etFuel);
        String fuelAcctOwed = etFuel.getText().toString();
        transactions.put("fuelAcctOwed", Integer.valueOf(etFuel.getText().toString()));

        EditText etMiscAcctPer = (EditText) findViewById(R.id.etMiscAcctPercentage);
        String miscAcctPer = etMiscAcctPer.getText().toString();
        transactions.put("miscAcctPer", Double.valueOf(etMiscAcctPer.getText().toString()));

        EditText etTotalRec = (EditText) findViewById(R.id.etTotalRec);
        String totalRec = etTotalRec.getText().toString();
        transactions.put("totalRec", Integer.valueOf(etTotalRec.getText().toString()));

        ContentValues values = processAmtRec(transactions, oldBalances);//processAmtRec(Short.valueOf(totalRec),Short.valueOf(fuelAcctOwed), Short.valueOf(inventoryOwed), Double.valueOf(miscAcctPer));

        if(dataOps.getSingleFundsItem("1").getCount()>0){
          //PID Found
          ContentValues newBalances = updateBalances(oldBalances, values);
          dataOps.updateFundsItem(newBalances, "1");
        }else{
          dataOps.saveFundsItem(values);
        }

        dataOps.closeDB();
        finish();
      }
    });
  }

  public ContentValues processAmtRec(ContentValues transaction, ContentValues oldBalances){ //(short totalRec, short fuelAcctOwed, short inventoryAcctOwed, double miscAcctPer){

    double balance = Integer.valueOf(transaction.get("totalRec").toString());
    balance = balance - Integer.valueOf(transaction.get("inventoryAcctOwed").toString());
    balance = balance - Integer.valueOf(transaction.get("fuelAcctOwed").toString());
    double miscAcctOwed =  balance * (Double.valueOf(transaction.get("miscAcctPer").toString())/100);
    balance = balance - miscAcctOwed;
    double daveOwed = balance / 2;
    double timOwed = balance /2;

    ContentValues balances = new ContentValues();
    balances.put(DatabaseContract.FundsDataColumns.COLUMN_DAVE , daveOwed);
    balances.put(DatabaseContract.FundsDataColumns.COLUMN_TIM, timOwed);
    balances.put(DatabaseContract.FundsDataColumns.COLUMN_INVENTORY, transaction.get("inventoryAcctOwed").toString());
    balances.put(DatabaseContract.FundsDataColumns.COLUMN_MISC, miscAcctOwed);
    balances.put(DatabaseContract.FundsDataColumns.COLUMN_FUEL, transaction.get("fuelAcctOwed").toString());
  return balances;


  }

  public ContentValues loadBalances(){
    ContentValues oldBalances = new ContentValues();

    dataOps = new DatabaseOperations(FundsNew.this);
    dataOps.openDB();

    dataOps.fundsCursor = dataOps.getSingleFundsItem("1");

    if (dataOps.fundsCursor.moveToFirst()) {
      String inventoryAcctBal = dataOps.fundsCursor.getString(dataOps.fundsCursor.getColumnIndexOrThrow(DatabaseContract.FundsDataColumns.COLUMN_INVENTORY));
      String miscAcctBal = dataOps.fundsCursor.getString(dataOps.fundsCursor.getColumnIndexOrThrow(DatabaseContract.FundsDataColumns.COLUMN_MISC));
      String daveAcctBal = dataOps.fundsCursor.getString(dataOps.fundsCursor.getColumnIndexOrThrow(DatabaseContract.FundsDataColumns.COLUMN_DAVE));
      String timAcctBal = dataOps.fundsCursor.getString(dataOps.fundsCursor.getColumnIndexOrThrow(DatabaseContract.FundsDataColumns.COLUMN_TIM));
      String fuelAcctBal = dataOps.fundsCursor.getString(dataOps.fundsCursor.getColumnIndexOrThrow(DatabaseContract.FundsDataColumns.COLUMN_FUEL));
      //dumpCursor(dataOps.fundsCursor);
      oldBalances.put(DatabaseContract.FundsDataColumns.COLUMN_INVENTORY, inventoryAcctBal);
      oldBalances.put(DatabaseContract.FundsDataColumns.COLUMN_MISC, miscAcctBal);
      oldBalances.put(DatabaseContract.FundsDataColumns.COLUMN_DAVE, daveAcctBal);
      oldBalances.put(DatabaseContract.FundsDataColumns.COLUMN_TIM, timAcctBal);
      oldBalances.put(DatabaseContract.FundsDataColumns.COLUMN_FUEL, fuelAcctBal);
    }
    dataOps.closeDB();

    return oldBalances;
  }

  public ContentValues updateBalances(ContentValues oldBalances, ContentValues values){
    double daveTotalOwed = Double.valueOf(oldBalances.get(DatabaseContract.FundsDataColumns.COLUMN_DAVE).toString());
    daveTotalOwed = daveTotalOwed + Double.valueOf(values.get(DatabaseContract.FundsDataColumns.COLUMN_DAVE).toString());

    double timTotalOwed = Double.valueOf(oldBalances.get(DatabaseContract.FundsDataColumns.COLUMN_TIM).toString());
    timTotalOwed = timTotalOwed + Double.valueOf(values.get(DatabaseContract.FundsDataColumns.COLUMN_TIM).toString());

    int inventoryTotalOwed = Integer.valueOf(oldBalances.get(DatabaseContract.FundsDataColumns.COLUMN_INVENTORY).toString());
    inventoryTotalOwed = inventoryTotalOwed + Integer.valueOf(values.get(DatabaseContract.FundsDataColumns.COLUMN_INVENTORY).toString());

    double miscTotalOwed = Double.valueOf(oldBalances.get(DatabaseContract.FundsDataColumns.COLUMN_MISC).toString());
    miscTotalOwed = miscTotalOwed + Double.valueOf(values.get(DatabaseContract.FundsDataColumns.COLUMN_MISC).toString());

    int fuelTotalOwed = Integer.valueOf(oldBalances.get(DatabaseContract.FundsDataColumns.COLUMN_FUEL).toString());
    fuelTotalOwed = fuelTotalOwed + Integer.valueOf(values.get(DatabaseContract.FundsDataColumns.COLUMN_FUEL).toString());

    ContentValues newBalances = new ContentValues();
    newBalances.put(DatabaseContract.FundsDataColumns.COLUMN_DAVE, daveTotalOwed);
    newBalances.put(DatabaseContract.FundsDataColumns.COLUMN_TIM, timTotalOwed);
    newBalances.put(DatabaseContract.FundsDataColumns.COLUMN_INVENTORY,inventoryTotalOwed);
    newBalances.put(DatabaseContract.FundsDataColumns.COLUMN_MISC, miscTotalOwed);
    newBalances.put(DatabaseContract.FundsDataColumns.COLUMN_FUEL, fuelTotalOwed);
    return newBalances;

  }

}
