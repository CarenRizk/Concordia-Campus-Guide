package com.example.concordia_campus_guide.Model;

public class Floor {

    private BuildingCode buildingCode;
    private int floorNumber;
​
    public Floor() {
    }
​
    public Floor(BuildingCode buildingCode, int floorNumber) {
        this.buildingCode = buildingCode;
        this.floorNumber = floorNumber;
    }
​
    public BuildingCode getBuildingCode() {
        return buildingCode;
    }
​
    public void setBuildingCode(BuildingCode buildingCode) {
        this.buildingCode = buildingCode;
    }
​
    public int getFloorNumber() {
        return floorNumber;
    }
​
    public void setFloorNumber(int floorNumber) {
        this.floorNumber = floorNumber;
    }​
}
