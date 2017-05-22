package com.bizconnectivity.gino.models;

import io.realm.RealmObject;

public class DealModel extends RealmObject{

    private int dealID;
    private String dealName;
    private String dealDescription;
    private String dealPromoStartDate;
    private String dealPromoEndDate;
    private String dealRedeemStartDate;
    private String dealRedeemEndDate;
    private String dealUsualPrice;
    private String dealPromoPrice;
    private String dealLocation;
    private String dealImageFile;
    private String dealImageName;
    private String dealImageExt;
    private int dealCategoryID;
    private int merchantID;

    public int getDealID() {
        return dealID;
    }

    public void setDealID(int dealID) {
        this.dealID = dealID;
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public String getDealDescription() {
        return dealDescription;
    }

    public void setDealDescription(String dealDescription) {
        this.dealDescription = dealDescription;
    }

    public String getDealPromoStartDate() {
        return dealPromoStartDate;
    }

    public void setDealPromoStartDate(String dealPromoStartDate) {
        this.dealPromoStartDate = dealPromoStartDate;
    }

    public String getDealPromoEndDate() {
        return dealPromoEndDate;
    }

    public void setDealPromoEndDate(String dealPromoEndDate) {
        this.dealPromoEndDate = dealPromoEndDate;
    }

    public String getDealRedeemStartDate() {
        return dealRedeemStartDate;
    }

    public void setDealRedeemStartDate(String dealRedeemStartDate) {
        this.dealRedeemStartDate = dealRedeemStartDate;
    }

    public String getDealRedeemEndDate() {
        return dealRedeemEndDate;
    }

    public void setDealRedeemEndDate(String dealRedeemEndDate) {
        this.dealRedeemEndDate = dealRedeemEndDate;
    }

    public String getDealUsualPrice() {
        return dealUsualPrice;
    }

    public void setDealUsualPrice(String dealUsualPrice) {
        this.dealUsualPrice = dealUsualPrice;
    }

    public String getDealPromoPrice() {
        return dealPromoPrice;
    }

    public void setDealPromoPrice(String dealPromoPrice) {
        this.dealPromoPrice = dealPromoPrice;
    }

    public String getDealLocation() {
        return dealLocation;
    }

    public void setDealLocation(String dealLocation) {
        this.dealLocation = dealLocation;
    }

    public String getDealImageFile() {
        return dealImageFile;
    }

    public void setDealImageFile(String dealImageFile) {
        this.dealImageFile = dealImageFile;
    }

    public String getDealImageName() {
        return dealImageName;
    }

    public void setDealImageName(String dealImageName) {
        this.dealImageName = dealImageName;
    }

    public String getDealImageExt() {
        return dealImageExt;
    }

    public void setDealImageExt(String dealImageExt) {
        this.dealImageExt = dealImageExt;
    }

    public int getDealCategoryID() {
        return dealCategoryID;
    }

    public void setDealCategoryID(int dealCategoryID) {
        this.dealCategoryID = dealCategoryID;
    }

    public int getMerchantID() {
        return merchantID;
    }

    public void setMerchantID(int merchantID) {
        this.merchantID = merchantID;
    }
}
