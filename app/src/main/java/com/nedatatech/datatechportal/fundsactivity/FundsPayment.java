package com.nedatatech.datatechportal.fundsactivity;

import android.content.ContentValues;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nedatatech.datatechportal.DatabaseContract;
import com.nedatatech.datatechportal.R;
import com.nedatatech.datatechportal.baseactivity.BaseActivity;

import java.util.ArrayList;

public class FundsPayment extends BaseActivity {

  String[] accounts = {"Misc", "Cash", "Fuel", "Inventory", "Tim", "Dave"};
  ArrayList<FundsPaymentItem> arrayOfFundsPaymentItems = new ArrayList<>();
  Spinner spFromAcct;
  Spinner spToAcct;
  EditText etPaymentAmt;
  ListView lvPayment;
  private String transType;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_funds_payment);

    transType = "Payment";

    spFromAcct = (Spinner) findViewById(R.id.spFromAcct);
    spToAcct = (Spinner) findViewById(R.id.spToAcct);
    etPaymentAmt = (EditText) findViewById(R.id.etPaymentAmt);
    lvPayment = (ListView) findViewById(R.id.lvPayment);

    ArrayAdapter<String> spFromAcctAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, accounts);
    spFromAcct.setAdapter(spFromAcctAdapter);
    spToAcct.setAdapter(spFromAcctAdapter);

    //match spinner size
    TextView tvFromAcct = (TextView) findViewById(R.id.tvFromAcct);
    TextView tvToAcct = (TextView) findViewById(R.id.tvToAcct);
    tvFromAcct.setHeight(spFromAcct.getHeight());
    tvToAcct.setHeight(spToAcct.getHeight());
  }

  public void addToAcct(View v){
    FundsPaymentAdapter fundsItemAdapter = new FundsPaymentAdapter(this, arrayOfFundsPaymentItems);
    String fromAcct = "From Account: " + spFromAcct.getSelectedItem().toString();
    String toAcct = "To Account: " + spToAcct.getSelectedItem().toString();
    String paymentAmt = "Amount: " + etPaymentAmt.getText().toString();
    FundsPaymentItem paymentItem = new FundsPaymentItem(fromAcct,toAcct,paymentAmt);
    fundsItemAdapter.add(paymentItem);
    lvPayment.setAdapter(fundsItemAdapter);
  }

  public void cancelClick(View v){
    cancel();
  }

  public void saveClick(View v){
    //savePayment();
    ContentValues transaction = new ContentValues();

    String fromAcct = spFromAcct.getSelectedItem().toString();
    String toAcct = spToAcct.getSelectedItem().toString();
    String paymentAmt = etPaymentAmt.getText().toString();

    transaction.put(DatabaseContract.FundsHistoryColumns.COLUMN_TRANS_TYPE, transType);
    transaction.put(DatabaseContract.FundsHistoryColumns.COLUMN_TRANS_DATE, currentDate());
    transaction.put(DatabaseContract.FundsHistoryColumns.COLUMN_TRANS_DATE, currentDate());
    transaction.put(DatabaseContract.FundsHistoryColumns.COLUMN_PAY_FROM_ACCT, fromAcct);
    transaction.put(DatabaseContract.FundsHistoryColumns.COLUMN_PAY_TO_ACCT, toAcct);
    transaction.put(DatabaseContract.FundsHistoryColumns.COLUMN_PAYMENT_AMT, paymentAmt);

    storeTransaction(transaction);
  }

}
