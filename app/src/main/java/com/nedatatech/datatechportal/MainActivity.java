package com.nedatatech.datatechportal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nedatatech.datatechportal.FundsActivity.FundsMain;

public class MainActivity extends AppCompatActivity {


  private DatabaseOperations dataOps;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    dataOps = new DatabaseOperations(this);
    dataOps.openDB(); // May want to manage this better. Not necessarily needed here and also should be on a background thread for performance when the database gets bigger.
  }

  public void customerButton(View view){
    Intent customerStart = new Intent(this, CustomerMainActivity.class);
    startActivity(customerStart);
  }

  public void jobButton(View view){
    Intent jobStart = new Intent(this, JobActivity.class);
    startActivity(jobStart);
  }

  public void inventoryButton(View view){
    Intent inventoryStart = new Intent(this, InventoryActivity.class);
    startActivity(inventoryStart);
  }

  public void companyButton(View view){
    Intent companyStart = new Intent(this, CompanyActivity.class);
    startActivity(companyStart);
  }

  public void fundsButton(View view){
    Intent fundsStart = new Intent(this, FundsMain.class);
    startActivity(fundsStart);
  }

  // Calling this method just to make sure the App gets closed out and the database closed properly.
  @Override
  public void onBackPressed() {
    finish();
    super.onBackPressed();
    dataOps.closeDB(); /* For now calling this here should guarantee the database isn't still performing any operations in the background and helps with security.
                          Though this may cause problems for having access still in actual database operating activities. need to look more into onBackPressed
                          and if its called here by starting new activities too.*/
  }

  @Override
  protected void onDestroy() {
    super.onDestroy(); // Should close the Database here but only destroy the main activity when the user chooses to close the whole app.
                        // Will need to do some research and testing on this further.
  }
}
/* ToDo Should probably call the onPause and onResume methods for safety and open and close the
ToDo database respectively but will want to test and see how this affects performance.*/
