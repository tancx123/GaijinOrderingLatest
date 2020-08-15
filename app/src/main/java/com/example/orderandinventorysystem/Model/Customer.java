package com.example.orderandinventorysystem.Model;

import java.io.Serializable;

public class Customer implements Serializable {

    private String custID, custName, icNo, email, phone, mobile, companyName, gender, custType, address, postCode, city, state, status;

    public Customer(String custID, String custName, String icNo, String email, String phone, String mobile, String companyName, String gender, String custType, String address, String postCode, String city, String state, String status) {

        this.custID = custID;
        this.custName = custName;
        this.icNo = icNo;
        this.email = email;
        this.phone = phone;
        this.mobile = mobile;
        this.companyName = companyName;
        this.gender = gender;
        this.custType = custType;
        this.address = address;
        this.postCode = postCode;
        this.city = city;
        this.state = state;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getPostCode() {
        return postCode;
    }

    public void setPostCode(String postCode) {
        this.postCode = postCode;
    }

    public String getCustID(){
        return custID;
    }

    public void setCustID(String custID){
        this.custID = custID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCustName() {
        return custName;
    }

    public void setCustName(String custName) {
        this.custName = custName;
    }

    public String getIcNo() {
        return icNo;
    }

    public void setIcNo(String icNo) {
        this.icNo = icNo;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCustType() {
        return custType;
    }

    public void setCustType(String custType) {
        this.custType = custType;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }
}
