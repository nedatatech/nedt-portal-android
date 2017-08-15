package com.nedatatech.datatechportal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomerMainActivity extends AppCompatActivity {

  private Button buttonAddEdit;
  private Button buttonSearch;
  private Button buttonList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_customer_main);

    buttonAddEdit = (Button) findViewById(R.id.customerEdit_button);
    buttonAddEdit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });

    buttonSearch = (Button) findViewById(R.id.customerSearch_button);
    buttonSearch.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });

    buttonList = (Button) findViewById(R.id.customerList_button);
    buttonList.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {

      }
    });



  }
}
