package com.bizconnectivity.gino.models;

import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class DealCategoryModel extends RealmObject{

    @PrimaryKey
    @Index
    private int dealCategoryID;
    private String dealCategoryName;
    private String imageFile;
    private String imageName;
    private String imageExt;

    public int getDealCategoryID() {
        return dealCategoryID;
    }

    public void setDealCategoryID(int dealCategoryID) {
        this.dealCategoryID = dealCategoryID;
    }

    public String getDealCategoryName() {
        return dealCategoryName;
    }

    public void setDealCategoryName(String dealCategoryName) {
        this.dealCategoryName = dealCategoryName;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public String getImageExt() {
        return imageExt;
    }

    public void setImageExt(String imageExt) {
        this.imageExt = imageExt;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }
}
