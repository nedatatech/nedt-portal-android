package com.nedatatech.datatechportal.fundsactivity;

import android.content.ContentValues;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nedatatech.datatechportal.DatabaseContract;
import com.nedatatech.datatechportal.R;

import java.util.ArrayList;

public class FundsPayment extends AppCompatActivity {

  String[] accounts = {"Misc", "Cash", "Fuel", "Inventory", "Tim", "Dave"};
  ArrayList<FundsPaymentItem> arrayOfFundsPaymentItems = new ArrayList<>();
  Spinner spFromAcct;
  Spinner spToAcct;
  EditText etPaymentAmt;
  ListView lvPayment;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_funds_payment);

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
    FundsPaymentAdapter itemAdapter = new FundsPaymentAdapter(this, arrayOfFundsPaymentItems);
    String fromAcct = "From Account: " + spFromAcct.getSelectedItem().toString();
    String toAcct = "To Account: " + spToAcct.getSelectedItem().toString();
    String paymentAmt = "Amount: " + etPaymentAmt.getText().toString();
    FundsPaymentItem paymentItem = new FundsPaymentItem(fromAcct,toAcct,paymentAmt);
    itemAdapter.add(paymentItem);
    lvPayment.setAdapter(itemAdapter);
  }

}
