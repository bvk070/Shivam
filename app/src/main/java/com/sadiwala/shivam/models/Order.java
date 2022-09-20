package com.sadiwala.shivam.models;

import com.google.firebase.firestore.Exclude;
import com.sadiwala.shivam.inputfields.InputFieldValue;

public class Order {

    // Field Names
    public static final String CUSTOMER = "customer";
    public static final String SHOULDER = "shoulder";
    public static final String CHEST = "chest";
    public static final String WAIST = "waist";
    public static final String SLEEVE = "sleeve";
    public static final String LENGTH = "length";
    public static final String NECK_TYPE = "neck_type";
    public static final String NECK_SIZE = "neck_size";
    public static final String PATTERN = "pattern";
    public static final String POCKET_SIZE = "pocket_size";
    public static final String POCKET = "pocket";
    public static final String LENGHA_POCKET = "lengha_pocket";
    public static final String MUNDHO = "mundho";
    public static final String FITTING = "fitting";
    public static final String KOTHO = "kotho";
    public static final String HIPS = "hips";
    public static final String GHER = "gher";
    public static final String CUT = "cut";
    public static final String MORI = "mori";
    public static final String KHRISTAK = "khristak";
    public static final String ILASTIC = "ilastic";
    public static final String CLOTH_DESIGN = "cloth_design";
    public static final String CLOTH_COLOR = "cloth_color";
    public static final String TIMESTAMP = "timestamp";

    private String id;
    private String type;
    private long timestamp;
    private InputFieldValue customer;
    private InputFieldValue shoulder;
    private InputFieldValue chest;
    private InputFieldValue waist;
    private InputFieldValue sleeve;
    private InputFieldValue length;
    private InputFieldValue neckType;
    private InputFieldValue neckSize;
    private InputFieldValue pattern;
    private InputFieldValue pocket;
    private InputFieldValue lenghaPocket;
    private InputFieldValue pocketSize;
    private InputFieldValue mundho;
    private InputFieldValue fitting;
    private InputFieldValue kotho;
    private InputFieldValue hips;
    private InputFieldValue gher;
    private InputFieldValue cut;
    private InputFieldValue mori;
    private InputFieldValue khristak;
    private InputFieldValue ilastic;
    private InputFieldValue clothDesign;
    private InputFieldValue clothColor;

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public InputFieldValue getCustomer() {
        return customer;
    }

    public void setCustomer(InputFieldValue customer) {
        this.customer = customer;
    }

    public InputFieldValue getShoulder() {
        return shoulder;
    }

    public void setShoulder(InputFieldValue shoulder) {
        this.shoulder = shoulder;
    }

    public InputFieldValue getChest() {
        return chest;
    }

    public void setChest(InputFieldValue chest) {
        this.chest = chest;
    }

    public InputFieldValue getWaist() {
        return waist;
    }

    public void setWaist(InputFieldValue waist) {
        this.waist = waist;
    }

    public InputFieldValue getSleeve() {
        return sleeve;
    }

    public void setSleeve(InputFieldValue sleeve) {
        this.sleeve = sleeve;
    }

    public InputFieldValue getLength() {
        return length;
    }

    public void setLength(InputFieldValue length) {
        this.length = length;
    }

    public InputFieldValue getNeckType() {
        return neckType;
    }

    public void setNeckType(InputFieldValue neckType) {
        this.neckType = neckType;
    }

    public InputFieldValue getNeckSize() {
        return neckSize;
    }

    public void setNeckSize(InputFieldValue neckSize) {
        this.neckSize = neckSize;
    }

    public InputFieldValue getPattern() {
        return pattern;
    }

    public void setPattern(InputFieldValue pattern) {
        this.pattern = pattern;
    }

    public InputFieldValue getPocket() {
        return pocket;
    }

    public void setPocket(InputFieldValue pocket) {
        this.pocket = pocket;
    }

    public InputFieldValue getPocketSize() {
        return pocketSize;
    }

    public void setPocketSize(InputFieldValue pocketSize) {
        this.pocketSize = pocketSize;
    }

    public InputFieldValue getMundho() {
        return mundho;
    }

    public void setMundho(InputFieldValue mundho) {
        this.mundho = mundho;
    }

    public InputFieldValue getFitting() {
        return fitting;
    }

    public void setFitting(InputFieldValue fitting) {
        this.fitting = fitting;
    }

    public InputFieldValue getKotho() {
        return kotho;
    }

    public void setKotho(InputFieldValue kotho) {
        this.kotho = kotho;
    }

    public InputFieldValue getHips() {
        return hips;
    }

    public void setHips(InputFieldValue hips) {
        this.hips = hips;
    }

    public InputFieldValue getGher() {
        return gher;
    }

    public void setGher(InputFieldValue gher) {
        this.gher = gher;
    }

    public InputFieldValue getCut() {
        return cut;
    }

    public void setCut(InputFieldValue cut) {
        this.cut = cut;
    }

    public InputFieldValue getMori() {
        return mori;
    }

    public void setMori(InputFieldValue mori) {
        this.mori = mori;
    }

    public InputFieldValue getKhristak() {
        return khristak;
    }

    public void setKhristak(InputFieldValue khristak) {
        this.khristak = khristak;
    }

    public InputFieldValue getIlastic() {
        return ilastic;
    }

    public void setIlastic(InputFieldValue ilastic) {
        this.ilastic = ilastic;
    }

    public InputFieldValue getClothDesign() {
        return clothDesign;
    }

    public void setClothDesign(InputFieldValue clothDesign) {
        this.clothDesign = clothDesign;
    }

    public InputFieldValue getClothColor() {
        return clothColor;
    }

    public void setClothColor(InputFieldValue clothColor) {
        this.clothColor = clothColor;
    }

    public InputFieldValue getLenghaPocket() {
        return lenghaPocket;
    }

    public void setLenghaPocket(InputFieldValue lenghaPocket) {
        this.lenghaPocket = lenghaPocket;
    }

    // Getter Method Names
    public static final String GETCUSTOMER = "getCustomer()";
    public static final String GETSHOULDER = "getShoulder()";
    public static final String GETWAIST = "getWaist()";
    public static final String GETCHEST = "getChest()";
    public static final String GETSLEEVE = "getSleeve()";
    public static final String GETLENGTH = "getLength()";
    public static final String GETNECKTYPE = "getNeckType()";
    public static final String GETNECKSIZE = "getNeckSize()";
    public static final String GETPATTERN = "getPattern()";
    public static final String GETPOCKET = "getPocket()";
    public static final String GETLENGHAPOCKET = "getLenghaPocket()";
    public static final String GETPOCKETSIZE = "getPocketSize()";
    public static final String GETMUNDHO = "getMundho()";
    public static final String GETFITTING = "getFitting()";
    public static final String GETKOTHO = "getKotho()";
    public static final String GETHIPS = "getHips()";
    public static final String GETGHER = "getGher()";
    public static final String GETCUT = "getCut()";
    public static final String GETMORI = "getMori()";
    public static final String GETKHRISTAK = "getKhristak()";
    public static final String GETILASTIC = "getIlastic()";
    public static final String GETCLOTHDESIGN = "getClothDesign()";
    public static final String GETCLOTHCOLOR = "getClothColor()";


}
