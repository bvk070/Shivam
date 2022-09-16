package com.sadiwala.shivam.models;

import com.google.firebase.firestore.Exclude;
import com.sadiwala.shivam.inputfields.InputFieldValue;

public class Customer {

    public static final String NAME = "name";
    public static final String MOBILE = "mobile";
    public static final String ADDRESS = "address";
    public static final String AREA = "area";
    public static final String PINCODE = "pincode";

    private String id;
    private InputFieldValue name;
    private InputFieldValue mobile;
    private InputFieldValue address;
    private InputFieldValue area;
    private InputFieldValue pincode;

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public InputFieldValue getName() {
        return name;
    }

    public void setName(InputFieldValue name) {
        this.name = name;
    }

    public InputFieldValue getMobile() {
        return mobile;
    }

    public void setMobile(InputFieldValue mobile) {
        this.mobile = mobile;
    }

    public InputFieldValue getAddress() {
        return address;
    }

    public void setAddress(InputFieldValue address) {
        this.address = address;
    }

    public InputFieldValue getArea() {
        return area;
    }

    public void setArea(InputFieldValue area) {
        this.area = area;
    }

    public InputFieldValue getPincode() {
        return pincode;
    }

    public void setPincode(InputFieldValue pincode) {
        this.pincode = pincode;
    }
}
