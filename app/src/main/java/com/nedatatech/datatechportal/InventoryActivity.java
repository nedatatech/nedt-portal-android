package com.nedatatech.datatechportal;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import static com.nedatatech.datatechportal.ParseJSON.auth_token;

public class InventoryActivity extends AppCompatActivity {
  public static String recordId;
  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_inventory);
  }
  public void myClick(View view) {
  EditText mEdit =(EditText) findViewById(R.id.editText);
  recordId = mEdit.getText().toString();
  ApiInterface auth = new ApiInterface();
  if (auth_token == null){
    auth.apiRequest("authenticate",InventoryActivity.this);
  }else{auth.apiRequest("create",InventoryActivity.this);}
  }
}
