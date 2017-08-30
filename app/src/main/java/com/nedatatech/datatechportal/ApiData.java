package com.nedatatech.datatechportal;

public class ApiData {

  //Todo: Go through this class and check for things that may need to be done or improved.

  //public static final String logTag = "CUSTOMER_OBJ_SYSTEM"; // Debug info.

  // Variables for each of the columns set up in the DB.
  private String apiDataToken;

  // A constructor for a new person that takes the columns in the DB for its parameters.
  public ApiData(String apiDataToken){
    this.apiDataToken = apiDataToken;
  }

  public ApiData() {
  } // I think I need this empty constructor to override OBJECT so I can Instantiate this class without the parameters in the other Customer Constructor.

  public String getApiDataToken() {
    return apiDataToken;
  }

  public void setApiDataToken(String apiDataToken) {
    this.apiDataToken = apiDataToken;
  }

  // This method will return human readable results of the variables in this class.
  @Override
  public String toString() {
    return "Auth Token:" + apiDataToken;
  }

}
