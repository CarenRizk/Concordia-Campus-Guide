package com.example.concordia_campus_guide;

import com.example.concordia_campus_guide.Models.Building;

import org.json.JSONException;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;

public class BuildingTest {

    private Building building;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        List<String> services = new ArrayList<>();

        building = new Building(new Double[]{45.4972685, -73.5789475}, new String[]{"8","9"}, 68, 68, 34, null, "H", null, null, null,services , null);
    }


    @Test
    public void getServicesStringTest(){
        String service = building.getServicesString();
        assertEquals("",service,"");
    }

    @Test
    public void getDepartmentsStringTest(){
        List<String> newDepartments  = new ArrayList<>();
        newDepartments.add("Engineering");
        newDepartments.add("Computer_Science");
        building = new Building(new Double[]{45.4972685, -73.5789475}, new String[]{"8","9"}, 68, 68, 34, null, "H", null, null, newDepartments,  null, null);
        String s2 = building.getDepartmentsString();
        assertEquals("", s2, "Engineering, Computer_Science" );
    }

    @Test
    public void getGeoJsonTest(){
        JSONObject jsonObject = building.getGeoJson();
        String str="";

        try {
            str = jsonObject.getString("code");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertEquals("Testing getGeoJson","H",str);
    }

    @Test
    public void getGeoJsonBuildingsTest(){
        JSONObject jsonObject = building.getGeoJson();
        String str="";

        try {
            str = jsonObject.getString("type");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        assertEquals("Testing getGeoJsonBuildings","FeatureCollection",str);
    }
}

