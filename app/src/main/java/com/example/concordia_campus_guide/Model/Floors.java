package com.example.concordia_campus_guide.Model;

import java.util.ArrayList;
import java.util.List;

public class Floors {
    private List<Floor> floors;
​
    public Floors(){
        floors = new ArrayList<Floor>();
    }
​
    public List<Floor> getFloors(){
        return floors;
    }
​
    public Floor getFloor(BuildingCode buildingCode, int floorNumber){
        for(Floor floor: floors){
            if(floor.getBuildingCode()== buildingCode && floor.getFloorNumber() == floorNumber){
                return floor;
            }
        }
        return null;
    }
​
    public Floors(List<Floor> floors){
        this.floors = floors;
    }

    public void setFloors(List<Floor> floors){
        this.floors = floors;
    }
}
