package com.nedatatech.datatechportal.FundsActivity;


import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.nedatatech.datatechportal.DatabaseContract;
import com.nedatatech.datatechportal.R;

public class FundsAdapter extends CursorAdapter {
  public FundsAdapter(Context context, Cursor cursor) {
    super(context, cursor, 0);
  }

  // The newView method is used to inflate a new view and return it,
  // you don't bind any data to the view at this point.
  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return LayoutInflater.from(context).inflate(R.layout.item_funds, parent, false);
  }

  // The bindView method is used to bind all data to a given view
  // such as setting the text on a TextView.
  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    // Find fields to populate in inflated template
    //TextView tvTotalRec = (TextView) view.findViewById(R.id.tvTotalRec);
    TextView tvDate = (TextView) view.findViewById(R.id.tvDate);
    TextView tvTransType = (TextView) view.findViewById(R.id.tvTransType);
    TextView tvFuel = (TextView) view.findViewById(R.id.tvFuel);
    TextView tvInventory = (TextView) view.findViewById(R.id.tvInventory);
    TextView tvMisc = (TextView) view.findViewById(R.id.tvMisc);
    TextView tvTim = (TextView) view.findViewById(R.id.tvTim);
    TextView tvDave = (TextView) view.findViewById(R.id.tvDave);


    // Extract properties from cursor
    String Date = "Transaction Date: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FundsHistoryColumns.COLUMN_TRANS_DATE));
    String transType = "Transaction Type: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FundsHistoryColumns.COLUMN_TRANS_TYPE));
    String fuel = "Fuel Acct: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FundsHistoryColumns.COLUMN_FUEL_ACCT_BAL));
    String inventory = "Inventory Acct: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FundsHistoryColumns.COLUMN_INVENTORY_ACCT_BAL));
    String misc = "Misc Acct: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FundsHistoryColumns.COLUMN_MISC_ACCT_BAL));
    String tim = "Tim Owed: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FundsHistoryColumns.COLUMN_TIM_BAL));
    String dave = "Dave Owed: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FundsHistoryColumns.COLUMN_DAVE_BAL));
    // Populate fields with extracted properties

    tvDate.setText(Date);
    tvTransType.setText(transType);
    tvFuel.setText(fuel);
    tvInventory.setText(inventory);
    tvMisc.setText(misc);
    tvTim.setText(tim);
    tvDave.setText(dave);

    //tvMisc.setText(String.format("Misc Acct: %s", misc));
    //tvTim.setText(String.format("Tim Owed: %s", tim));
    //tvDave.setText(String.format("Dave Owed: %s", dave));

  }

}