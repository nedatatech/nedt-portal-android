package com.nedatatech.datatechportal;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.SyncStateContract;
import android.widget.Toast;
import org.json.JSONException;
import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

//import static com.nedatatech.datatechportal.ParseJSON.auth_token;
import static com.nedatatech.datatechportal.InventoryActivity.recordId;

class ApiInterface {
  private static final String api_base_url = "http://192.168.1.100:3000/api/nedt-portal";
  private Context mCtx;
  private int method;
  private String json_url;
  private final HashMap<String, String> params = new HashMap<>();
  private final HashMap<String, String> headers = new HashMap<>();
  //RequestQueue mRequestQueue = Volley.newRequestQueue; // 'this' is Context
  private SQLiteOpenHelper dbHelper = new DatabaseHelper(mCtx); /* This can be TestDBHelper dbHelper or the system default SQLiteOpenHelper dbHelper. Not sure what the difference is.
                                            Maybe either just methods used in TestDBHelper class or all from SQLiteOpenHelper and the overridden ones in TestDBHelper.*/

  public String auth_token;
  private DatabaseOperations dataOps;

  void apiRequest(String request_type, Context context) {

    mCtx = context;
    switch (request_type) {
      case "authenticate":
        this.method = Request.Method.POST;
        json_url = api_base_url + "/authenticate";
        params.put("email", "example@mail.com");
        params.put("password", "123123123");
        break;
      case "read":
        this.method = Request.Method.GET;
        json_url = api_base_url + "/items/" + recordId;
        headers.put("Authorization:", auth_token);
        break;
      case "update":
        this.method = Request.Method.PUT;
        json_url = api_base_url + "/items/" + recordId;
        headers.put("Authorization:", auth_token);
        params.put("item[name]", "bullshit");
        params.put("item[description]", "also bullshit");
        params.put("item[quantity]", "numbers");
        break;
      case "create":
        this.method = Request.Method.POST;
        json_url = api_base_url + "/items/create";
        headers.put("Authorization:", auth_token);
        params.put("item[name]", "example@mail.com");
        params.put("item[description]", "123123123");
        params.put("item[quantity]", "7");
        break;
      case "destroy":
        this.method = Request.Method.DELETE;
        json_url = api_base_url + "/items/" + recordId;
        headers.put("Authorization:", auth_token);
        break;
    }
    sendRequest();
  }

  private void sendRequest() {
    final JSONObject json = new JSONObject(params);
    dataOps = new DatabaseOperations(mCtx);
    //json.putAll( data );
    JsonObjectRequest jsonObjReq = new JsonObjectRequest(
            method, json_url, json,
            new Response.Listener<JSONObject>() {
              @Override
              public void onResponse(JSONObject response) {
                try {

                  auth_token = response.get("auth_token").toString();
                 //auth_token = response.toString();

                 //String test = "INSERT INTO " + DatabaseContract.ApiDataColumns.TABLE_API_DATA + " (" + DatabaseContract.ApiDataColumns.COLUMN_AUTH_TOKEN +") VALUES" + "(" + auth_token + ");";

                 //database.execSQL("INSERT INTO " + DatabaseContract.ApiDataColumns.TABLE_API_DATA + " (" + DatabaseContract.ApiDataColumns.COLUMN_AUTH_TOKEN +") VALUES" + "(" + auth_token + ");");
                  //dataOps
                  //ContentValues values = new ContentValues();
                  //values.put("auth", auth_token);
                  //dataOps.addToken(auth_token);
                  ApiData test = new ApiData(auth_token);
                  dataOps.addTokenToDB(auth_token);
                 Toast.makeText(mCtx, test.getApiDataToken(),Toast.LENGTH_LONG) .show();


                //msgResponse.setText(response.toString());
                //hideProgressDialog();
                } catch (JSONException e) {
                  e.printStackTrace();
                }
              }
            }, new Response.ErrorListener() {

      @Override
      public void onErrorResponse(VolleyError error) {
        //VolleyLog.d(TAG, "Error: " + error.getMessage());
        //hideProgressDialog();
      }
    }) {

      /*
       * Passing some request headers
       */
      @Override
      public Map<String, String> getHeaders() throws AuthFailureError {
        return headers;
      }

      @Override
      protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        return params;
      }
    };
    //ApplicationController test = new ApplicationController();
    //test.getInstance().addToRequestQueue(jsonObjReq);
    RequestQueue requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
    requestQueue.add(jsonObjReq);
    //test.getInstance().addToRequestQueue(jsonObjReq, "hghc");
  }
}
//    StringRequest stringRequest;
//    stringRequest = new StringRequest(method, json_url,
//      new Response.Listener<String>() {
//        @Override
//        public void onResponse(String response) {
//          showJSON(response);
//          Toast.makeText(mCtx, response ,Toast.LENGTH_LONG) .show();
//        }
//      },
//        new Response.ErrorListener() {
//          @Override
//          public void onErrorResponse(VolleyError error) {
//            Toast.makeText(mCtx.getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
//          }
//        })
//        {
//          @Override
//          public Map<String, String> getHeaders() throws com.android.volley.AuthFailureError {
//            return headers;
//          }
//          @Override
//          protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
//            return params;
//          }
//        };
//    RequestQueue requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
//    requestQueue.add(stringRequest);
//  }

 // private void showJSON(String json){
  //  ParseJSON pj = new ParseJSON(json);
  //  pj.parseJSON();
  //}
//}