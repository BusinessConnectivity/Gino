package com.bizconnectivity.gino.models;

import java.io.Serializable;

import io.realm.RealmObject;
import io.realm.annotations.PrimaryKey;

public class PulseList extends RealmObject implements Serializable{

    @PrimaryKey
    private String pulseID;
    private String pulseDescription;
    private String pulseImage;
    private String pulseTitle;
    private String pulseDatetime;
    private String pulseLocation;
    private String pulseOrganizer;
    private String pulseURL;

    public String getPulseID() {
        return pulseID;
    }

    public void setPulseID(String pulseID) {
        this.pulseID = pulseID;
    }

    public String getPulseDescription() {
        return pulseDescription;
    }

    public void setPulseDescription(String pulseDescription) {
        this.pulseDescription = pulseDescription;
    }

    public String getPulseImage() {
        return pulseImage;
    }

    public void setPulseImage(String pulseImage) {
        this.pulseImage = pulseImage;
    }

    public String getPulseTitle() {
        return pulseTitle;
    }

    public void setPulseTitle(String pulseTitle) {
        this.pulseTitle = pulseTitle;
    }

    public String getPulseDatetime() {
        return pulseDatetime;
    }

    public void setPulseDatetime(String pulseDatetime) {
        this.pulseDatetime = pulseDatetime;
    }

    public String getPulseLocation() {
        return pulseLocation;
    }

    public void setPulseLocation(String pulseLocation) {
        this.pulseLocation = pulseLocation;
    }

    public String getPulseOrganizer() {
        return pulseOrganizer;
    }

    public void setPulseOrganizer(String pulseOrganizer) {
        this.pulseOrganizer = pulseOrganizer;
    }

    public String getPulseURL() {
        return pulseURL;
    }

    public void setPulseURL(String pulseURL) {
        this.pulseURL = pulseURL;
    }
}
