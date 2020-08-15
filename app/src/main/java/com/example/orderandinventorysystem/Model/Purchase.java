package com.example.orderandinventorysystem.Model;

import java.io.Serializable;

public class Purchase implements Serializable {

    private String pID, venID, venName, dDate, pStatus;
    private double pAmount;

    public Purchase(String pID, String venID, String venName, String dDate, String pStatus, double pAmount) {
        this.pID = pID;
        this.venID = venID;
        this.venName = venName;
        this.dDate = dDate;
        this.pStatus = pStatus;
        this.pAmount = pAmount;
    }

    public String getpID() {
        return pID;
    }

    public void setpID(String pID) {
        this.pID = pID;
    }

    public String getVenID() {
        return venID;
    }

    public void setVenID(String venID) {
        this.venID = venID;
    }

    public String getVenName() {
        return venName;
    }

    public void setVenName(String venName) {
        this.venName = venName;
    }

    public String getdDate() {
        return dDate;
    }

    public void setdDate(String dDate) {
        this.dDate = dDate;
    }

    public String getpStatus() {
        return pStatus;
    }

    public void setpStatus(String pStatus) {
        this.pStatus = pStatus;
    }

    public double getpAmount() {
        return pAmount;
    }

    public void setpAmount(double pAmount) {
        this.pAmount = pAmount;
    }
}
