package com.nedatatech.datatechportal.FundsActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;

import com.nedatatech.datatechportal.DatabaseOperations;
import com.nedatatech.datatechportal.R;
import com.nedatatech.datatechportal.FundsActivity.FundsAdapter;
import com.nedatatech.datatechportal.FundsActivity.FundsNew;
import com.nedatatech.datatechportal.FundsActivity.FundsEdit;
import com.nedatatech.datatechportal.FundsActivity.FundsView;

public class FundsMain extends Activity {

  private DatabaseOperations dataOps;

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

    Button buttonNew = (Button) findViewById(R.id.FundsNew_button);
    buttonNew.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Intent startListActivity = new Intent(com.nedatatech.datatechportal.FundsActivity.FundsMain.this, FundsNew.class);
        startActivity(startListActivity);
      }
    });
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
      Intent startListActivity = new Intent(com.nedatatech.datatechportal.FundsActivity.FundsMain.this, FundsEdit.class);
      startListActivity.putExtra("itemId", itemId);
      startActivity(startListActivity);
    }else if(item.getTitle()=="View"){
      Intent startListActivity = new Intent(com.nedatatech.datatechportal.FundsActivity.FundsMain.this, FundsView.class);
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
