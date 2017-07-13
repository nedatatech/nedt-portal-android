package com.nedatatech.datatechportal;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class MainActivity extends AppCompatActivity {



  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
  }

  public void customerButton(View view){
    Intent customerStart = new Intent(this, CustomerActivity.class);
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
}
