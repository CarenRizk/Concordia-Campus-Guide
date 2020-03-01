package com.example.concordia_campus_guide.Models;

import com.google.android.gms.maps.model.LatLng;

public abstract class Place {
    protected Double[] centerCoordinates;
    protected String displayName;

    public Place(Double[] centerCoordinates) {
        this.centerCoordinates = centerCoordinates;
    }

    public Double[] getCenterCoordinates() {
        return centerCoordinates;
    }

    public LatLng getCenterCoordinatesLatLng() {
        return new LatLng(centerCoordinates[0], centerCoordinates[1]);
    }

    public void setCenterCoordinates(Double[] centerCoordinates) {
        this.centerCoordinates = centerCoordinates;
    }

    public abstract String getDisplayName();
}
