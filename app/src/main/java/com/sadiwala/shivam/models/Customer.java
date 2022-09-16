package com.sadiwala.shivam.models;

import com.google.firebase.firestore.Exclude;

public class Customer {

    public static final String NAME = "name";
    public static final String MOBILE = "mobile";
    public static final String ADDRESS = "address";
    public static final String AREA = "area";
    public static final String PINCODE = "pincode";

    private int id;
    private String name;
    private String mobile;
    private String address;
    private String area;
    private String pincode;

    @Exclude
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getPincode() {
        return pincode;
    }

    public void setPincode(String pincode) {
        this.pincode = pincode;
    }
}
