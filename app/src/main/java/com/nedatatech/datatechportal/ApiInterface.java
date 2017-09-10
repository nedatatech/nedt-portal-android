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

  public String auth_token;
  private DatabaseOperations dataOps;
  private Response.ErrorListener defaultErrorListener = new Response.ErrorListener() {
    @Override
    public void onErrorResponse(VolleyError error) {
      Toast.makeText(mCtx, "Default error listener", Toast.LENGTH_LONG).show();
      //VolleyLog.d(TAG, "Error: " + error.getMessage());
      //hideProgressDialog();
    }
  };
  private Response.Listener defaultListener = new Response.Listener<JSONObject>() {
    @Override
    public void onResponse(JSONObject response) {
      try {
        //auth_token = response.get("auth_token").toString();
        response.get("");
        Toast.makeText(mCtx, "Default response listener", Toast.LENGTH_LONG).show();
        //store_Auth_Locally(auth_token);
      } catch (JSONException e) {
        e.printStackTrace();
      }
    }
  };


  /*
TODO: 9/9/17 figure out how to validate a stored auth token before making a request
      or how to detect and respond to a failed request.
*/

void apiRequest(String request_type){ //, Context context) {

    switch (request_type) {
      case "test":
        //testing create

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
        //this.method = Request.Method.PUT;
        json_url = api_base_url + "/items/" + recordId;
        headers.put("Authorization:", auth_token);
        params.put("item[name]", "bullshit");
        params.put("item[description]", "also bullshit");
        params.put("item[quantity]", "numbers");
        //Needs custom listener in place of null
//        sendRequest(Request.Method.PUT, headers, params, null);
        break;
      case "create":
        json_url = api_base_url + "/items/create";
        String token = getAuthFromDB();
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
        //this.method = Request.Method.DELETE;
        json_url = api_base_url + "/items/" + recordId;
        headers.put("Authorization:", auth_token);
        //Needs custom listener in place of null
//        sendRequest(Request.Method.DELETE, headers, params, null);
        break;
    }
  }

  private void validateStoredAuthToken(){
    String token = getAuthFromDB();
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

    sendRequest(Request.Method.POST, headers, params, listener, errorListener);
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
}

  /*
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
                  dataOps.openDB();
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
*/
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