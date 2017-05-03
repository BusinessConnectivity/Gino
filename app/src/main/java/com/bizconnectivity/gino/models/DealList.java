package com.bizconnectivity.gino.models;

public class DealList {

    private int dealImage;
    private String dealTitle;
    private String dealLocation;
    private String dealPrice;

    public int getDealImage() {
        return dealImage;
    }

    public void setDealImage(int dealImage) {
        this.dealImage = dealImage;
    }

    public String getDealTitle() {
        return dealTitle;
    }

    public void setDealTitle(String dealTitle) {
        this.dealTitle = dealTitle;
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
}
