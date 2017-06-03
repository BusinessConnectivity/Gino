package com.bizconnectivity.gino.models;

public class UserDeal {

    private int userDealId;
    private int userId;
    private int dealId;
    private int quantity;
    private String totalPrice;
    private String subtotalPrice;
    private String createdDate;
    private String imageFile;
    private String dealName;

    public int getUserDealId() {
        return userDealId;
    }

    public void setUserDealId(int userDealId) {
        this.userDealId = userDealId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getDealId() {
        return dealId;
    }

    public void setDealId(int dealId) {
        this.dealId = dealId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getSubtotalPrice() {
        return subtotalPrice;
    }

    public void setSubtotalPrice(String subtotalPrice) {
        this.subtotalPrice = subtotalPrice;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public String getDealName() {
        return dealName;
    }

    public void setDealName(String dealName) {
        this.dealName = dealName;
    }
}
