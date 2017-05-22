package com.bizconnectivity.gino.models;

import io.realm.RealmObject;

public class MerchantModel extends RealmObject{

    private int merchantID;
    private String merchantName;
    private String merchantCode;
    private String merchantAddress;
    private String merchantPostalCode;
    private String merchantWebsite;
    private String merchantContactNo;
    private String merchantContactPerson;

    public int getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(int merchantID) {
        this.merchantID = merchantID;
    }

    public String getMerchantName() {
        return merchantName;
    }

    public void setMerchantName(String merchantName) {
        this.merchantName = merchantName;
    }

    public String getMerchantCode() {
        return merchantCode;
    }

    public void setMerchantCode(String merchantCode) {
        this.merchantCode = merchantCode;
    }

    public String getMerchantAddress() {
        return merchantAddress;
    }

    public void setMerchantAddress(String merchantAddress) {
        this.merchantAddress = merchantAddress;
    }

    public String getMerchantPostalCode() {
        return merchantPostalCode;
    }

    public void setMerchantPostalCode(String merchantPostalCode) {
        this.merchantPostalCode = merchantPostalCode;
    }

    public String getMerchantWebsite() {
        return merchantWebsite;
    }

    public void setMerchantWebsite(String merchantWebsite) {
        this.merchantWebsite = merchantWebsite;
    }

    public String getMerchantContactNo() {
        return merchantContactNo;
    }

    public void setMerchantContactNo(String merchantContactNo) {
        this.merchantContactNo = merchantContactNo;
    }

    public String getMerchantContactPerson() {
        return merchantContactPerson;
    }

    public void setMerchantContactPerson(String merchantContactPerson) {
        this.merchantContactPerson = merchantContactPerson;
    }
}
