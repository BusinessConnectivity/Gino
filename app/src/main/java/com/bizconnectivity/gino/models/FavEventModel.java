package com.bizconnectivity.gino.models;

import io.realm.RealmList;
import io.realm.RealmObject;
import io.realm.annotations.Index;
import io.realm.annotations.PrimaryKey;

public class FavEventModel extends RealmObject{

    @PrimaryKey
    @Index
    private int userFavEventID;
    private int userID;
    private int eventID;
    private RealmList<EventModel> events;

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

    public RealmList<EventModel> getEvents() {
        return events;
    }

    public void setEvents(RealmList<EventModel> events) {
        this.events = events;
    }
}
