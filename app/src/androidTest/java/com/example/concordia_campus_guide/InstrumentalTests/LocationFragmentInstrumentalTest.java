package com.example.concordia_campus_guide.InstrumentalTests;

import android.content.Context;

import com.example.concordia_campus_guide.Activities.MainActivity;
import com.example.concordia_campus_guide.Fragments.LocationFragment.LocationFragmentViewModel;
import com.example.concordia_campus_guide.Models.Building;
import com.google.android.gms.maps.model.BitmapDescriptor;

import org.junit.Rule;
import org.junit.Test;

import static junit.framework.TestCase.assertNotNull;

import androidx.test.filters.SmallTest;
import androidx.test.rule.ActivityTestRule;
import androidx.test.runner.AndroidJUnit4;
import org.junit.Before;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@SmallTest
public class LocationFragmentInstrumentalTest {
    private LocationFragmentViewModel viewModel;
    private Context appContext;


    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void init() {
        viewModel = new LocationFragmentViewModel();
        appContext = mActivityRule.getActivity().getApplicationContext();
    }

    @Test
    public void styleMarker(){
        BitmapDescriptor bitmap1 = viewModel.styleMarker("EV",appContext);
        BitmapDescriptor bitmap2 = viewModel.styleMarker("EV",appContext);
        assertNotNull(bitmap1);
        assertNotNull(bitmap2);
    }

    @Test
    public void setBuildingGroundOverlayOptionsTest(){
        Double[] coordinatecenter = new Double[]{45.495305, -73.578885};
        Building building = new Building(coordinatecenter, new String[]{"S2","1"}, 65, 65, 127, null, "MB", null, null, null, null, null);
        viewModel.setBuildingGroundOverlayOptions(building);
        assertNotNull(building.getGroundOverlayOption());
    }

}


