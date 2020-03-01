package com.example.concordia_campus_guide.Models;

import java.util.List;

class Floor extends Place {
    private String number;
    private float altitude;
    private List<Room> rooms;
    public Floor(Double[] coordinates, String number, float altitude) {
        super(coordinates);
        this.number = number;
        this.altitude = altitude;
    }

    public String getDisplayName(){
        return number;
    }
}
