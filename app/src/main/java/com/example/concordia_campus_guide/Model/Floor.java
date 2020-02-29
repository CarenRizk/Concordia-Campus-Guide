package com.example.concordia_campus_guide.Model;

public class Floor {
    private BuildingCode buildingCode;
    private int floor;

    public Floor(BuildingCode buildingCode, int floor) {
        this.buildingCode = buildingCode;
        this.floor = floor;
    }

    public BuildingCode getBuildingCode() {
        return buildingCode;
    }

    public void setBuildingCode(BuildingCode buildingCode) {
        this.buildingCode = buildingCode;
    }

    public int getFloor() {
        return floor;
    }

    public void setFloor(int floor) {
        this.floor = floor;
    }
}
