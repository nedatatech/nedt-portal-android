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
import com.android.volley.NetworkResponse;
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
  private static final String api_base_url = "http://192.168.1.101:3000/api/nedt-portal";
  private Context mCtx;
  //private int method;
  private String json_url;
  private final HashMap<String, String> params = new HashMap<>();
  private final HashMap<String, String> headers = new HashMap<>();
  //RequestQueue mRequestQueue = Volley.newRequestQueue; // 'this' is Context
  private SQLiteOpenHelper dbHelper = new DatabaseHelper(mCtx); /* This can be TestDBHelper dbHelper or the system default SQLiteOpenHelper dbHelper. Not sure what the difference is.
                                            Maybe either just methods used in TestDBHelper class or all from SQLiteOpenHelper and the overridden ones in TestDBHelper.*/

  public void setmCtx(Context mCtx) {
    this.mCtx = mCtx;
  }

  private String token;

  public String auth_token;
  private DatabaseOperations dataOps;
  private Response.ErrorListener defaultErrorListener = new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
      //Toast.makeText(mCtx, "Default error listener: " + error.toString() + " || " + error.networkResponse.toString() , Toast.LENGTH_LONG).show();
      String json = null;
      NetworkResponse response = error.networkResponse;
      if(response != null && response.data != null){
        //switch(response.statusCode){
        //  case 500:
            json = new String(response.data);
            //json = trimMessage(json,"message");
            if(json != null) displayMessage(json);
        //    break;
        //}
      }
      switch(error.toString()){
        case "com.android.volley.AuthFailureError":
          json_url = api_base_url + "/authenticate";
          headers.put("Authorization:","N/A");
          params.put("email", "example@mail.com");
          params.put("password", "123123123");
          //getNewAuthToken(headers, params);
        break;
      }
      //VolleyLog.d(TAG, "Error: " + error.getMessage());
      //hideProgressDialog();
    }
  };
  private Response.Listener defaultListener = new Response.Listener<JSONObject>() {
    @Override
    public void onResponse(JSONObject response) {
      try {
        //auth_token = response.get("auth_token").toString();
        response.get("response");
        Toast.makeText(mCtx, "Default response listener: " + response.toString(), Toast.LENGTH_LONG).show();
        //store_Auth_Locally(auth_token);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  };

  public String trimMessage(String json, String key){
    String trimmedString;

    try{
      JSONObject obj = new JSONObject(json);
      trimmedString = obj.getString(key);
    }catch(JSONException e){
      e.printStackTrace();
      return null;
    }
    return trimmedString;
  }

  public void displayMessage(String toastString){
    Toast.makeText(mCtx, toastString , Toast.LENGTH_LONG).show();
  }

  /*
TODO: 9/9/17 figure out how to validate a stored auth token before making a request
      or how to detect and respond to a failed request.
*/

void apiRequest(String request_type){ //, Context context) {

    switch (request_type) {
      //case validate
      case "test":
        break;
      case "validate":
        validateStoredAuthToken();
        //testing validate
        //token = getAuthFromDB();
        ////test failed token
        //token = token + "fail";
        //json_url = api_base_url + "/validate";
        //params.clear();
        //headers.clear();
        //headers.put("Authorization:", token);
        //sendRequest(Request.Method.POST, headers, params, defaultListener, defaultErrorListener);
        break;
      case "authenticate":
        //this.method = request.method.post;
        json_url = api_base_url + "/authenticate";
        headers.put("Authorization:","N/A");
        params.put("email", "example@mail.com");
        params.put("password", "123123123");
        getNewAuthToken(headers, params);
        //Make toast to check if token was stored successfully
        //ApiData test = new ApiData(auth_token);
        //Toast.makeText(mCtx, test.getApiDataToken(), Toast.LENGTH_LONG).show();
        break;
      case "read":
        //this.method = Request.Method.GET;
        json_url = api_base_url + "/items/" + recordId;
        headers.put("Authorization:", auth_token);
        //Needs custom listener in place of null
//        sendRequest(Request.Method.GET, headers, params, null);
        break;
      case "update":
        recordId = "11";
        token = getAuthFromDB();
        json_url = api_base_url + "/items/" + recordId;
        headers.put("Authorization:", token);
        params.put("name", "bullshit2");
        params.put("description", "also bullshit2");
        params.put("quantity", "numbers2");
        //Needs custom listener in place of null
        sendRequest(Request.Method.PUT, headers, params, defaultListener, defaultErrorListener);
        break;
      case "create":
        json_url = api_base_url + "/items/create";
        token = getAuthFromDB();
        headers.clear();
        headers.put("Authorization:", token);
        params.clear();
        params.put("name", "bullshit");
        params.put("description", "also bullshit");
        params.put("quantity", "numbers");
//        params.put("item[min_stock]", "more numbers");
        sendRequest(Request.Method.POST, headers, params, defaultListener, defaultErrorListener);
        break;
      case "destroy":
        recordId = "9";
        token = getAuthFromDB();
        json_url = api_base_url + "/items/" + recordId;
        headers.put("Authorization:", token);
        sendRequest(Request.Method.DELETE, headers, params, defaultListener, defaultErrorListener);
        break;
    }
  }

  private void validateStoredAuthToken(){

    json_url = api_base_url + "/validate";
    params.clear();
    headers.clear();
    token = getAuthFromDB();
    //test failed token
    //token = token + "fail";
    headers.put("Authorization:", token);

    Response.Listener listener = new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        try {
          auth_token = response.get("auth_token").toString();
          store_Auth_Locally(auth_token);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    };

    Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        //VolleyLog.d(TAG, "Error: " + error.getMessage());
        //hideProgressDialog();
      }
    };

    sendRequest(Request.Method.POST, headers, params, defaultListener, defaultErrorListener);
  }

  private void getNewAuthToken(HashMap<String, String> headers, HashMap<String, String> params) {
    Response.Listener listener = new Response.Listener<JSONObject>() {
      @Override
      public void onResponse(JSONObject response) {
        try {
          auth_token = response.get("auth_token").toString();
          store_Auth_Locally(auth_token);
        } catch (JSONException e) {
          e.printStackTrace();
        }
      }
    };

     Response.ErrorListener errorListener = new Response.ErrorListener() {
      @Override
      public void onErrorResponse(VolleyError error) {
        //VolleyLog.d(TAG, "Error: " + error.getMessage());
        //hideProgressDialog();
      }
    };

    sendRequest(Request.Method.POST, headers, params, listener, errorListener);
  }


  private void sendRequest(int method, final HashMap<String, String> headers, final HashMap<String,
                            String> params, Response.Listener listener, Response.ErrorListener errorListener){//Response.Listener listener) {

    final JSONObject json = new JSONObject(params);

    JsonObjectRequest jsonObjReq = new JsonObjectRequest(
            method, json_url, json, listener, errorListener) {
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

    RequestQueue requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
    requestQueue.add(jsonObjReq);
  }

  private void store_Auth_Locally(String auth_token) {
    dataOps = new DatabaseOperations(mCtx);
    dataOps.openDB();
    dataOps.addTokenToDB(auth_token);
    dataOps.closeDB();
  }

  public String getAuthFromDB(){
    dataOps = new DatabaseOperations(mCtx);
    dataOps.openDB();

    //broken
    String token = dataOps.getTokenFromDB("_id", "1");
    dataOps.closeDB();
    return token;
  }

  private void storePendingRequest(){

  }

}
