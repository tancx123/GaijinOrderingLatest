package com.example.orderandinventorysystem.Model;

public class Vendor {

    private String venID, venName, venIC, companyName, email, phone, mobile,address, state, city,postcode, status;

    public Vendor(String venID, String venName, String venIC, String companyName, String email, String phone, String mobile, String address, String state, String city, String postcode, String status) {
        this.venID = venID;
        this.venName = venName;
        this.venIC = venIC;
        this.companyName = companyName;
        this.email = email;
        this.phone = phone;
        this.mobile = mobile;
        this.address = address;
        this.state = state;
        this.city = city;
        this.postcode = postcode;
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVenIC() {
        return venIC;
    }

    public void setVenIC(String venIC) {
        this.venIC = venIC;
    }

    public String getVenID() {
        return venID;
    }

    public void setVenID(String venID) {
        this.venID = venID;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getPostcode() {
        return postcode;
    }

    public void setPostcode(String postcode) {
        this.postcode = postcode;
    }

    public String getVenName() {
        return venName;
    }

    public void setVenName(String venName) {
        this.venName = venName;
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


}
