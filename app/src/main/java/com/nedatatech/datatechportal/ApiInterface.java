package com.nedatatech.datatechportal;

import android.content.Context;
import android.widget.Toast;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import java.util.HashMap;
import java.util.Map;


import static com.nedatatech.datatechportal.ParseJSON.auth_token;
import static com.nedatatech.datatechportal.InventoryActivity.recordId;

class ApiInterface {
  private static final String api_base_url = "http://www.nedatatech.com:3000/api/nedt-portal";
  private Context mCtx;
  private int method;
  private String json_url;
  private final HashMap<String, String> params = new HashMap<>();
  private final HashMap<String, String> headers = new HashMap<>();

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

  private void sendRequest(){
    StringRequest stringRequest;
    stringRequest = new StringRequest(method, json_url,
      new Response.Listener<String>() {
        @Override
        public void onResponse(String response) {
          showJSON(response);
          Toast.makeText(mCtx, response ,Toast.LENGTH_LONG) .show();
        }
      },
        new Response.ErrorListener() {
          @Override
          public void onErrorResponse(VolleyError error) {
            Toast.makeText(mCtx.getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
          }
        })
        {
          @Override
          public Map<String, String> getHeaders() throws com.android.volley.AuthFailureError {
            return headers;
          }
          @Override
          protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
            return params;
          }
        };
    RequestQueue requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
    requestQueue.add(stringRequest);
  }

  private void showJSON(String json){
    ParseJSON pj = new ParseJSON(json);
    pj.parseJSON();
  }
}