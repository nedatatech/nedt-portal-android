package com.nedatatech.datatechportal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class InventoryActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_inventory);

    Button buttonAddEdit = (Button) findViewById(R.id.invTextButton);
    buttonAddEdit.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        Authenticate auth = new Authenticate();
        auth.sendRequest("authenticate",InventoryActivity.this) ;
      }
    });
  }
}
