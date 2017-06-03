package com.bizconnectivity.gino.models;

public class PurchasedDeal {

    private int userDealId;
    private int dealId;
    private String dealName;
    private String imageFile;
    private String dealRedeemEndDate;
    private boolean isExpired;
    private boolean isRedeemed;
    private String redeemedDate;

    public int getUserDealId() {
        return userDealId;
    }

    public void setUserDealId(int userDealId) {
        this.userDealId = userDealId;
    }

    public int getDealId() {
        return dealId;
    }

    public void setDealId(int dealId) {
        this.dealId = dealId;
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getDealRedeemEndDate() {
        return dealRedeemEndDate;
    }

    public void setDealRedeemEndDate(String dealRedeemEndDate) {
        this.dealRedeemEndDate = dealRedeemEndDate;
    }

    public boolean isExpired() {
        return isExpired;
    }

    public void setExpired(boolean expired) {
        isExpired = expired;
    }

    public boolean isRedeemed() {
        return isRedeemed;
    }

    public void setRedeemed(boolean redeemed) {
        isRedeemed = redeemed;
    }

    public String getRedeemedDate() {
        return redeemedDate;
    }

    public void setRedeemedDate(String redeemedDate) {
        this.redeemedDate = redeemedDate;
    }
}
