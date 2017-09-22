package com.nedatatech.datatechportal;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.nedatatech.datatechportal.ToDoActivity.ToDoMain;

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
    Intent startListActivity = new Intent(InventoryActivity.this, ToDoMain.class);
    startActivity(startListActivity);
      }
    });

  }
  public void myClick(View view) {
  EditText mEdit =(EditText) findViewById(R.id.editText);
  recordId = mEdit.getText().toString();
  ApiInterface auth = new ApiInterface();
  //String test = auth.getAuthFromDB();
  //if (auth_token == null){
  //if(auth.getAuthFromDB().equals("")){

    //Pass current context to auth
    auth.setmCtx(this);
    //authorize user
    //auth.apiRequest("authenticate");
    //execute api request
    auth.apiRequest("create");

  //}else{auth.apiRequest("authenticate",InventoryActivity.this);}
  }
}
