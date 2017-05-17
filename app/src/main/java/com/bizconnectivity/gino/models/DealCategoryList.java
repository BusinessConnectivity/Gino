package com.bizconnectivity.gino.models;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class DealCategoryList extends RealmObject{

    @PrimaryKey
    private int categoryID;
//    private String categoryImageURL;
    private int categoryImageURL;
    private String categoryTitle;

    public int getCategoryID() {
        return categoryID;
    }

    public void setCategoryID(int categoryID) {
        this.categoryID = categoryID;
    }

//    public String getCategoryImageURL() {
//        return categoryImageURL;
//    }

//    public void setCategoryImageURL(String categoryImageURL) {
//        this.categoryImageURL = categoryImageURL;
//    }


    public int getCategoryImageURL() {
        return categoryImageURL;
    }

    public void setCategoryImageURL(int categoryImageURL) {
        this.categoryImageURL = categoryImageURL;
    }

    public String getCategoryTitle() {
        return categoryTitle;
    }

    public void setCategoryTitle(String categoryTitle) {
        this.categoryTitle = categoryTitle;
    }
}
