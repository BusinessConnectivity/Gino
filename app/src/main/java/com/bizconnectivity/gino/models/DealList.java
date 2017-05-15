package com.bizconnectivity.gino.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DealList extends RealmObject{

    @PrimaryKey
    private int dealID;
    private String dealImageURL;
    private String dealTitle;
    private String dealDescription;
    private String dealLocation;
    private String dealPrice;
    private String dealMerchant;
    private String dealPromotionStart;
    private String dealPromotionEnd;
    private String dealRedeemStart;
    private String dealRedeemEnd;
    private int dealCategoryID;
    private String isFavorite;
    private String isPurchased;
    private String isExpired;

    public int getDealID() {
        return dealID;
    }

    public void setDealID(int dealID) {
        this.dealID = dealID;
    }

    public String getDealImageURL() {
        return dealImageURL;
    }

    public void setDealImageURL(String dealImageURL) {
        this.dealImageURL = dealImageURL;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
    }

    public String getDealDescription() {
        return dealDescription;
    }

    public void setDealDescription(String dealDescription) {
        this.dealDescription = dealDescription;
    }

    public String getDealLocation() {
        return dealLocation;
    }

    public void setDealLocation(String dealLocation) {
        this.dealLocation = dealLocation;
    }

    public String getDealPrice() {
        return dealPrice;
    }

    public void setDealPrice(String dealPrice) {
        this.dealPrice = dealPrice;
    }

    public String getDealMerchant() {
        return dealMerchant;
    }

    public void setDealMerchant(String dealMerchant) {
        this.dealMerchant = dealMerchant;
    }

    public String getDealPromotionEnd() {
        return dealPromotionEnd;
    }

    public void setDealPromotionEnd(String dealPromotionEnd) {
        this.dealPromotionEnd = dealPromotionEnd;
    }

    public String getDealRedeemStart() {
        return dealRedeemStart;
    }

    public void setDealRedeemStart(String dealRedeemStart) {
        this.dealRedeemStart = dealRedeemStart;
    }

    public String getDealPromotionStart() {
        return dealPromotionStart;
    }

    public void setDealPromotionStart(String dealPromotionStart) {
        this.dealPromotionStart = dealPromotionStart;
    }

    public int getDealCategoryID() {
        return dealCategoryID;
    }

    public void setDealCategoryID(int dealCategoryID) {
        this.dealCategoryID = dealCategoryID;
    }

    public String getDealRedeemEnd() {
        return dealRedeemEnd;
    }

    public void setDealRedeemEnd(String dealRedeemEnd) {
        this.dealRedeemEnd = dealRedeemEnd;
    }

    public String getIsFavorite() {
        return isFavorite;
    }

    public void setIsFavorite(String isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getIsPurchased() {
        return isPurchased;
    }

    public void setIsPurchased(String isPurchased) {
        this.isPurchased = isPurchased;
    }

    public String getIsExpired() {
        return isExpired;
    }

    public void setIsExpired(String isExpired) {
        this.isExpired = isExpired;
    }
}
