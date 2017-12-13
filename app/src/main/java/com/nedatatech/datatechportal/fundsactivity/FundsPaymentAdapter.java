package com.nedatatech.datatechportal.fundsactivity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.nedatatech.datatechportal.R;

import java.util.ArrayList;

public class FundsPaymentAdapter extends ArrayAdapter<FundsPaymentItem> {
  public FundsPaymentAdapter(Context context, ArrayList<FundsPaymentItem> paymentItems) {
    super(context, 0, paymentItems);
  }

  @Override
  public View getView(int position, View convertView, ViewGroup parent) {
    // Get the data item for this position
    FundsPaymentItem paymentItem = getItem(position);
    // Check if an existing view is being reused, otherwise inflate the view
    if (convertView == null) {
      convertView = LayoutInflater.from(getContext()).inflate(R.layout.item_funds_payment, parent, false);
    }
    // Lookup view for data population
    TextView tvFromAcct = (TextView) convertView.findViewById(R.id.tvFromAcct);
    TextView tvToAcct = (TextView) convertView.findViewById(R.id.tvToAcct);
    TextView tvPaymentAmt = (TextView) convertView.findViewById(R.id.tvPaymentAmt);
    // Populate the data into the template view using the data object
    tvFromAcct.setText(paymentItem.fromAcct);
    tvToAcct.setText(paymentItem.toAcct);
    tvPaymentAmt.setText(paymentItem.paymentAmount);
    // Return the completed view to render on screen
    return convertView;
  }
}



/*
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.TextView;

import com.nedatatech.datatechportal.DatabaseContract;
import com.nedatatech.datatechportal.R;

public class FundsPaymentAdapter extends ArrayAdapter {
  FundsPaymentAdapter(Context context, Cursor cursor) {
    super(context, cursor, 0);
  }

  // The newView method is used to inflate a new view and return it,
  // you don't bind any data to the view at this point.
  @Override
  public View newView(Context context, Cursor cursor, ViewGroup parent) {
    return LayoutInflater.from(context).inflate(R.layout.item_funds_payment, parent, false);
  }

  // The bindView method is used to bind all data to a given view
  // such as setting the text on a TextView.
  @Override
  public void bindView(View view, Context context, Cursor cursor) {
    // Find fields to populate in inflated template

    TextView tvFromAcct = (TextView) view.findViewById(R.id.tvFromAcct);
    TextView tvToAcct = (TextView) view.findViewById(R.id.tvToAcct);
    TextView tvPaymentAmt = (TextView) view.findViewById(R.id.tvPaymentAmt);

    // Extract properties from cursor
    String fromAcct = "From Account: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FundsHistoryColumns.COLUMN_PAY_FROM_ACCT));
    String toAcct = "To Account: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FundsHistoryColumns.COLUMN_PAY_TO_ACCT));
    String paymentAmt = "Payment Amount: " + cursor.getString(cursor.getColumnIndexOrThrow(DatabaseContract.FundsHistoryColumns.COLUMN_PAYMENT_AMT));

    // Populate fields with extracted properties
    tvFromAcct.setText(fromAcct);
    tvToAcct.setText(toAcct);
    tvPaymentAmt.setText(paymentAmt);
  }

}
*/