package com.nedatatech.datatechportal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import static com.nedatatech.datatechportal.ParseJSON.auth_token;

public class InventoryActivity extends AppCompatActivity {
  public static String recordId;
  private Button buttonList;

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_inventory);

    buttonList = (Button) findViewById(R.id.tokenList_button);
    buttonList.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
    Intent startListActivity = new Intent(InventoryActivity.this, DatabaseTester.class);
    startActivity(startListActivity);
      }
    });

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
