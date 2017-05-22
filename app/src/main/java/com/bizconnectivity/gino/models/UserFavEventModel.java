package com.bizconnectivity.gino.models;

import io.realm.RealmObject;

public class UserFavEventModel extends RealmObject{

    private int userFavEventID;
    private int userID;
    private int eventID;

    public int getUserFavEventID() {
        return userFavEventID;
    }

    public void setUserFavEventID(int userFavEventID) {
        this.userFavEventID = userFavEventID;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getEventID() {
        return eventID;
    }

    public void setEventID(int eventID) {
        this.eventID = eventID;
    }
}
