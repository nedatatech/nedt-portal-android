package com.nedatatech.datatechportal.fundsactivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;

import com.nedatatech.datatechportal.DatabaseOperations;
import com.nedatatech.datatechportal.R;

public class FundsMain extends Activity {

  //private final String transType = "Check";

  private DatabaseOperations dataOps;
  PopupWindow popupWindow;
  String[] TransType = {"Check", "Cash", "Purchase", "Payment",
          "Borrow"};

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_funds);

    dataOps = new DatabaseOperations(this);
    dataOps.openDB();

    dataOps.fundsCursor = dataOps.getAllFundsItems();

    ListView lvFundsItems = (ListView) findViewById(R.id.lvFundsItems);
    dataOps.fundsAdapter = new FundsAdapter(this, dataOps.fundsCursor);
    lvFundsItems.setAdapter(dataOps.fundsAdapter);
    registerForContextMenu(lvFundsItems);

    //Button buttonNew = (Button) findViewById(R.id.FundsNew_button);
    //buttonNew.setOnClickListener(new View.OnClickListener() {
    //  @Override
    //  public void onClick(View v) {
        //Intent startListActivity = new Intent(com.nedatatech.datatechportal.FundsActivity.FundsMain.this, FundsCheck.class);
        //Intent startListActivity = new Intent(com.nedatatech.datatechportal.FundsActivity.FundsMain.this, FundsCheck.class);
        //startActivity(startListActivity);
     // }
    //});
  }

  public void newButtonPressed(View view){
    //Intent startListActivity = new Intent(com.nedatatech.datatechportal.FundsActivity.FundsMain.this, FundsCheck.class);
    //startActivity(startListActivity);
    // get a reference to the already created main layout
    //LinearLayout mainLayout = (LinearLayout)
    //findViewById(R.id.activity_funds);

    // inflate the layout of the popup window
    LayoutInflater inflater = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
    final ViewGroup nullParent = null;
    View popupView = inflater.inflate(R.layout.transaction_type_select_popup, nullParent);

    // create the popup window
    int width = LinearLayout.LayoutParams.WRAP_CONTENT;
    int height = LinearLayout.LayoutParams.WRAP_CONTENT;

    popupWindow = new PopupWindow(popupView, width, height, true);

    //Spinner popupSpinner = (Spinner)popupView.findViewById(R.id.spTransType);
    ListView listview = (ListView) popupView.findViewById(R.id.lvTransType);


    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, TransType);
 //   adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
 //   popupSpinner.setAdapter(adapter);
    listview.setAdapter(adapter);
    listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
      @Override
      public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        // Get the selected item text from ListView
        String selectedItem = (String) parent.getItemAtPosition(position);
        // Display the selected item text on TextView
        //tv.setText("Your favorite : " + selectedItem);
        switch(selectedItem) {
          case "Check":
            popupWindow.dismiss();
            Intent startListActivity = new Intent(com.nedatatech.datatechportal.fundsactivity.FundsMain.this, FundsCheck.class);
            startListActivity.putExtra("TransType", "Check");
            startActivity(startListActivity);
            break;
          case "Payment":
            popupWindow.dismiss();
            Intent startPaymentActivity = new Intent(com.nedatatech.datatechportal.fundsactivity.FundsMain.this, FundsPayment.class);
            startPaymentActivity.putExtra("TransType", "Payment");
            startActivity(startPaymentActivity);
        }
      }
    });
    // show the popup window
    popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);
  }

  @Override
  public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo)
  {
    super.onCreateContextMenu(menu, v, menuInfo);
    menu.setHeaderTitle("Select The Action");
    menu.add(0, v.getId(), 0, "Delete");
    menu.add(0, v.getId(), 0, "Edit");
    menu.add(0, v.getId(), 0, "View");
  }

  @Override
  public boolean onContextItemSelected(MenuItem item){
    if(item.getTitle()=="Delete"){
      //Toast.makeText(getApplicationContext(),"Deletes the item",Toast.LENGTH_LONG).show();
      final String itemId = dataOps.fundsCursor.getString(dataOps.fundsCursor.getColumnIndex("_id"));
      dataOps.deleteFundsItem(itemId);
      dataOps.refreshFundsList();
    }
    else if(item.getTitle()=="Edit") {
      //Toast.makeText(getApplicationContext(),"Edits the item",Toast.LENGTH_LONG).show();
      String itemId = dataOps.fundsCursor.getString(dataOps.fundsCursor.getColumnIndex("_id"));
      dataOps.getSingleFundsItem(itemId);
      Intent startListActivity = new Intent(com.nedatatech.datatechportal.fundsactivity.FundsMain.this, FundsEdit.class);
      startListActivity.putExtra("itemId", itemId);
      startActivity(startListActivity);
    }else if(item.getTitle()=="View"){
      Intent startListActivity = new Intent(com.nedatatech.datatechportal.fundsactivity.FundsMain.this, FundsView.class);
      startActivity(startListActivity);
    }else{
      return false;
    }
    return true;
  }

  @Override
  protected void onResume() {
    super.onResume();
    dataOps.refreshFundsList();
  }

  @Override
  protected void onDestroy() {
    super.onDestroy();
    dataOps.closeDB();
  }
}
