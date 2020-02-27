package com.example.concordia_campus_guide.Model;

public class ClassroomMarkerTag {
    private BuildingCode buildingCode;
    private int floor;
    private String roomNumber;

    public ClassroomMarkerTag() {
    }

    public ClassroomMarkerTag(BuildingCode buildingCode, int floor, String roomNumber) {
        this.buildingCode = buildingCode;
        this.floor = floor;
        this.roomNumber = roomNumber;
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

    public String getRoomNumber() {
        return roomNumber;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }
}
