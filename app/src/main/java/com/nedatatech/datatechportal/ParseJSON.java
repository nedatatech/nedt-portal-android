package com.nedatatech.datatechportal;

import org.json.JSONException;
import org.json.JSONObject;

public class ParseJSON {

  public static String auth_token;

  private String json;

  public ParseJSON(String json){
    this.json = json;
  }

  protected void parseJSON(){
    JSONObject jsonObject;
    try {
      jsonObject = new JSONObject(json);

      auth_token = jsonObject.get("auth_token").toString();

    } catch (JSONException e) {
      e.printStackTrace();
    }
  }
}