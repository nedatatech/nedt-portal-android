package com.nedatatech.datatechportal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import static com.nedatatech.datatechportal.ParseJSON.auth_token;

public class InventoryActivity extends AppCompatActivity {

  //public String auth_token;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_inventory);
  }
    //Button buttonAddEdit = (Button) findViewById(R.id.invTextButton);
    //buttonAddEdit.setOnClickListener(new View.OnClickListener() {

      // @Override
      public void myClick(View view) {
        ApiInterface auth = new ApiInterface();
        if (auth_token == null){
          auth.apiRequest("authenticate",InventoryActivity.this);
        }else{auth.apiRequest("create",InventoryActivity.this);}

      }
    //});

}
