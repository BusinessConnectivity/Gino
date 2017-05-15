package com.bizconnectivity.gino.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Pulses {

    @SerializedName("events")
    @Expose
    private List<EventList> events;

    public List<EventList> getEvents() {
        return events;
    }

    public void setEvents(List<EventList> events) {
        this.events = events;
    }
}
