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

public class Authenticate {

  public static final String api_base_url = "http://www.nedatatech.com/api/nedt-portal";
  private static Context mCtx;
  private int method;
  private String json_url;

  protected void sendRequest(String request_type,Context context) {

    mCtx = context;
    switch (request_type ){
      case "authenticate":
        this.method = Request.Method.POST;
        json_url = api_base_url + "/authenticate";
        break;
      case "update":
        //do things
        break;
    }

    StringRequest stringRequest = new StringRequest(method, json_url,
            new Response.Listener<String>() {
              @Override
              public void onResponse(String response) {
                showJSON(response);
              }
            },
            new Response.ErrorListener() {
              @Override
              public void onErrorResponse(VolleyError error) {
                Toast.makeText(mCtx.getApplicationContext(),error.getMessage(),Toast.LENGTH_LONG).show();
              }
            }) {
      @Override
      protected Map<String, String> getParams() throws com.android.volley.AuthFailureError {
        Map<String, String> params = new HashMap();
        params.put("email", "example@mail.com");
        params.put("password", "123123123");
        return params;
      }
    };
    RequestQueue requestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
    requestQueue.add(stringRequest);
  }
  private void showJSON(String json){
    ParseJSON pj = new ParseJSON(json);
    pj.parseJSON();
    Toast.makeText(mCtx.getApplicationContext(), auth_token,Toast.LENGTH_LONG) .show();
  }
}
