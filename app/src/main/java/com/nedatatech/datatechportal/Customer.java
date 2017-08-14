package com.nedatatech.datatechportal;

import android.util.Log;

// This class is represents an object stored in the database. In this case a person plus their info.
public class Customer {

  //Todo: Go through this class and check for things that may need to be done or improved.

  public static final String logTag = "CUSTOMER_OBJ_SYSTEM"; // Debug info.

  // Variables for each of the columns set up in the DB.
  private long customerID;
  private String customerFirstName;
  private String customerLastName;
  private String customerEmail;
  private String customerPhone;
  private String customerStreet;
  private String customerCity;
  private String customerState;
  private String customerZipcode;

  // A constructor for a new person that takes the columns in the DB for its parameters.
  public Customer(long customerID, String customerFirstName, String customerLastName, String customerEmail, String customerPhone,
                String customerStreet, String customerCity, String customerState, String customerZipcode) {
    this.customerID = customerID;
    this.customerFirstName = customerFirstName;
    this.customerLastName = customerLastName;
    this.customerEmail = customerEmail;
    this.customerPhone = customerPhone;
    this.customerStreet = customerStreet;
    this.customerCity = customerCity;
    this.customerState = customerState;
    this.customerZipcode = customerZipcode;
  }

  public Customer() {
  } // I think I need this empty constructor to override OBJECT so I can Instantiate this class without the parameters in the other Customer Constructor.

  /* Getters and setters for each of the variables. Setters make the variables point to their passed parameters.
  Also I think in this case they allow us to change value without having to rewrite the whole constructor when used in other classes.
  "Should double check this and update comment when sure." Getters get the result of the variables current value.*/

  public long getCustomerID() {
    return customerID;
  }

  public void setCustomerID(long customerID) {
    this.customerID = customerID;
  }

  public String getCustomerFirstName() {
    return customerFirstName;
  }

  public void setCustomerFirstName(String customerFirstName) {
    this.customerFirstName = customerFirstName;
  }

  public String getCustomerLastName() {
    return customerLastName;
  }

  public void setCustomerLastName(String customerLastName) {
    this.customerLastName = customerLastName;
  }

  public String getCustomerEmail() {
    return customerEmail;
  }

  public void setCustomerEmail(String customerEmail) {
    this.customerEmail = customerEmail;
  }

  public String getCustomerPhone() {
    return customerPhone;
  }

  public void setCustomerPhone(String customerPhone) {
    this.customerPhone = customerPhone;
  }

  public String getCustomerStreet() {
    return customerStreet;
  }

  public void setCustomerStreet(String customerStreet) {
    this.customerStreet = customerStreet;
  }

  public String getCustomerCity() {
    return customerCity;
  }

  public void setCustomerCity(String customerCity) {
    this.customerCity = customerCity;
  }

  public String getCustomerState() {
    return customerState;
  }

  public void setCustomerState(String customerState) {
    this.customerState = customerState;
  }

  public String getCustomerZipcode() {
    return customerZipcode;
  }

  public void setCustomerZipcode(String customerZipcode) {
    this.customerZipcode = customerZipcode;
  }

  // This method will return human readable results of the variables in this class.
  @Override
  public String toString() {
    return "Customer ID: " + getCustomerID() + "\n" + "First Name: " + getCustomerFirstName() + "\n" + "Last Name: " + getCustomerLastName() + "\n" +
            "Email: " + getCustomerEmail() + "\n" + "Phone: " + getCustomerPhone() + "\n" + "Street: " + getCustomerStreet() + "\n" +
            "City: " + getCustomerCity() + "\n" + "State: " + getCustomerState() + "\n" + "Zip Code: " + getCustomerZipcode();
  }
}
