package com.example.orderandinventorysystem.Model;

import java.io.Serializable;

public class Bill implements Serializable {

    String billID, pID, pCust, billDate;
    double billAmount;

    public Bill(String billID, String pID, String pCust, String billDate, double billAmount) {
        this.billID = billID;
        this.pID = pID;
        this.pCust = pCust;
        this.billDate = billDate;
        this.billAmount = billAmount;
    }

    public String getBillID() {
        return billID;
    }

    public void setBillID(String billID) {
        this.billID = billID;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getpCust() {
        return pCust;
    }

    public void setpCust(String pCust) {
        this.pCust = pCust;
    }

    public String getBillDate() {
        return billDate;
    }

    public void setBillDate(String billDate) {
        this.billDate = billDate;
    }

    public double getBillAmount() {
        return billAmount;
    }

    public void setBillAmount(double billAmount) {
        this.billAmount = billAmount;
    }
}
