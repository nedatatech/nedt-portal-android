package com.nedatatech.datatechportal.fundsactivity;

public class FundsPaymentItem {
  public String fromAcct;
  public String toAcct;
  public String paymentAmount;

  public FundsPaymentItem(String fromAcct, String toAcct, String paymentAmount) {
    this.fromAcct = fromAcct;
    this.toAcct = toAcct;
    this.paymentAmount = paymentAmount;
  }
}