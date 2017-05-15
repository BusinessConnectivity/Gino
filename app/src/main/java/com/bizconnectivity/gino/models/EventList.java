package com.bizconnectivity.gino.models;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class EventList {

    @SerializedName("id")
    @Expose
    private String id;

    @SerializedName("name")
    @Expose
    private NameList name;

    @SerializedName("description")
    @Expose
    private DescriptionList description;

    @SerializedName("start")
    @Expose
    private JsonObject start;

    @SerializedName("end")
    @Expose
    private EndDateList end;

    @SerializedName("venue")
    @Expose
    private VenueList venue;

    @SerializedName("organizer")
    @Expose
    private OrganizerList organizer;

    @SerializedName("category")
    @Expose
    private CategoryList category;

    @SerializedName("logo")
    @Expose
    private LogoList logo;

    @SerializedName("url")
    @Expose
    private String url;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public NameList getName() {
        return name;
    }

    public void setName(NameList name) {
        this.name = name;
    }

    public DescriptionList getDescription() {
        return description;
    }

    public void setDescription(DescriptionList description) {
        this.description = description;
    }

    public JsonObject getStart() {
        return start;
    }

    public void setStart(JsonObject start) {
        this.start = start;
    }

    public EndDateList getEnd() {
        return end;
    }

    public void setEnd(EndDateList end) {
        this.end = end;
    }

    public VenueList getVenue() {
        return venue;
    }

    public void setVenue(VenueList venue) {
        this.venue = venue;
    }

    public OrganizerList getOrganizer() {
        return organizer;
    }

    public void setOrganizer(OrganizerList organizer) {
        this.organizer = organizer;
    }

    public CategoryList getCategory() {
        return category;
    }

    public void setCategory(CategoryList category) {
        this.category = category;
    }

    public LogoList getLogo() {
        return logo;
    }

    public void setLogo(LogoList logo) {
        this.logo = logo;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
