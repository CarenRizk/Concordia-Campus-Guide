package com.example.concordia_campus_guide.LocationFragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.GridView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.concordia_campus_guide.Adapters.FloorPickerAdapter;
import com.example.concordia_campus_guide.ClassConstants;
import com.example.concordia_campus_guide.Interfaces.OnFloorPickerOnClickListener;
import com.example.concordia_campus_guide.Model.BuildingCode;
import com.example.concordia_campus_guide.Model.ClassroomMarkerTag;
import com.example.concordia_campus_guide.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.GroundOverlay;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MapStyleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.maps.android.geojson.GeoJsonFeature;
import com.google.maps.android.geojson.GeoJsonLayer;

import java.util.ArrayList;

import static androidx.core.content.ContextCompat.checkSelfPermission;

public class LocationFragment extends Fragment implements OnFloorPickerOnClickListener {

    MapView mMapView;

    private GoogleMap mMap;
    private LocationFragmentViewModel mViewModel;
    private GeoJsonLayer mLayer;
    private GeoJsonLayer floorLayer;
    private Button loyolaBtn;
    private Button sgwBtn;
    private GridView mFloorPickerGv;

    private static final String TAG = "LocationFragment";
    private Boolean myLocationPermissionsGranted = false;
    private GroundOverlay hallGroundOverlay;
    private GroundOverlay mbGroundOverlay;
    private GroundOverlay vlGroundOverlay;
    private FloorPickerAdapter currentFloorPickerAdapter;

    private Button selectedFloor;
    public static LocationFragment newInstance() {
        return new LocationFragment();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.location_fragment_fragment, container, false);
        initComponent(rootView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        initMap();
        setupClickListeners();
        getLocationPermission();

        return rootView;
    }

    private void initComponent(View rootView) {
        mMapView = rootView.findViewById(R.id.mapView);
        sgwBtn = rootView.findViewById(R.id.SGWBtn);
        loyolaBtn = rootView.findViewById(R.id.loyolaBtn);
        mFloorPickerGv = rootView.findViewById(R.id.FloorPickerGv);
        mFloorPickerGv.setVisibility(View.GONE);
    }

    private void setupFloorPickerAdapter(String buildingCode) {
        Log.i(TAG,"setting up floor picker -> " + BuildingCode.valueOf(buildingCode));
        ArrayList<String> floorsAvailable = (ArrayList<String>) mViewModel.getFloorsAvailable(BuildingCode.valueOf(buildingCode));
        if (floorsAvailable.isEmpty()){
            mFloorPickerGv.setVisibility(View.GONE);
            return;
        }
        mFloorPickerGv.setVisibility(View.VISIBLE);
        currentFloorPickerAdapter = new FloorPickerAdapter(getContext(), floorsAvailable, BuildingCode.valueOf(buildingCode), this);
        mFloorPickerGv.setAdapter(currentFloorPickerAdapter);
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(com.example.concordia_campus_guide.LocationFragment.LocationFragmentViewModel.class);
    }

    private void initMap() {
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            Log.e(TAG,e.getMessage());
        }
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                setMapStyle(googleMap);
                mMap = googleMap;
                setupPolygons(mMap);
                initFloorPlans();
                uiSettingsForMap(mMap);
                zoomInLocation(45.494999, -73.577854);
            }
        });
    }

    private void initFloorPlans() {
        hallGroundOverlay = mMap.addGroundOverlay(mViewModel.getHallBuildingOverlay());
        mbGroundOverlay = mMap.addGroundOverlay(mViewModel.getMBBuildingOverlay());
        vlGroundOverlay = mMap.addGroundOverlay(mViewModel.getVLBuildingOverlay());
    }

    private void setMapStyle(GoogleMap googleMap) {
        try {
            googleMap.setIndoorEnabled(false);
            googleMap.setMapStyle(
                    MapStyleOptions.loadRawResourceStyle(
                            getContext(), mViewModel.getMapStyle()));
        } catch (Resources.NotFoundException e) {
            Log.e("MAPACTIVITY", "Can't find style. Error: ", e);
        }
    }

    private void setupClickListeners() {
        setupLoyolaBtnClickListener();
        setupSGWBtnClickListener();
    }

    private void setupLoyolaBtnClickListener() {
        loyolaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomInLocation(45.458205, -73.640438);
            }
        });
    }

    private void setupSGWBtnClickListener(){
        sgwBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                zoomInLocation(45.494999, -73.577854);
            }
        });
    }

    private void setupPolygons(GoogleMap map) {
        mLayer = mViewModel.loadPolygons(map, getContext(), R.raw.buildingcoordinates);
        mViewModel.setPolygonStyle(mLayer,map);
        mLayer.addLayerToMap();

        setupPolygonClickListener();
        setupBuildingMarkerClickListener(map);
        setupZoomListener(map);
        classRoomCoordinateTool(map);
    }

    private void setupZoomListener(final GoogleMap map) {
        map.setOnCameraMoveListener(new GoogleMap.OnCameraMoveListener() {
            @Override
            public void onCameraMove() {
                if(map.getCameraPosition().zoom > 20){
                    mLayer.removeLayerFromMap();
                }
                else{
                    mLayer.addLayerToMap();
                    setupBuildingMarkerClickListener(map);
                }
            }
        });
    }

    public void setupPolygonClickListener(){
        mLayer.setOnFeatureClickListener(new GeoJsonLayer.GeoJsonOnFeatureClickListener() {
            @Override
            public void onFeatureClick(GeoJsonFeature geoJsonFeature) {
                //TODO: Make function that pops up the info card for the building (via the building-code)
                if(geoJsonFeature != null){
                    //replace code here
                    Log.i(TAG,"Clicked on "+geoJsonFeature.getProperty("code"));
                }
            }
        });
    }

    public boolean setupBuildingMarkerClickListener(GoogleMap map) {
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                setupFloorPickerAdapter(marker.getTag().toString());
                return false;
            }
        });
        return true;
    }

    public boolean setupClassMarkerClickListener(GoogleMap map) {
        map.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                System.out.println(marker.getTag());
                return false;
            }
        });
        return true;
    }


    private void uiSettingsForMap(GoogleMap mMap){
        if(myLocationPermissionsGranted){
            mMap.setMyLocationEnabled(true);
        }
        mMap.getUiSettings().setMyLocationButtonEnabled(true);
        mMap.getUiSettings().setTiltGesturesEnabled(true);
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.getUiSettings().setZoomControlsEnabled(true);
    }

    private void zoomInLocation(double latitude, double longitude) {
        LatLng curr = new LatLng(latitude,longitude);
        float zoomLevel = 18.0f;
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(curr, zoomLevel));
    }

    private void getLocationPermission(){
        String[] permissions = {Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        if(requestPermission()){
            myLocationPermissionsGranted = true;
        }else{
            ActivityCompat.requestPermissions(getActivity(),
                    permissions,
                    ClassConstants.LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    private void classRoomCoordinateTool(GoogleMap map){
        map.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                System.out.println("\"coordinates\" : [" + latLng.longitude + ", " + latLng.latitude + "]");
            }
        });
    }


    private boolean requestPermission(){
        return (checkSelfPermission(getContext(), ClassConstants.FINE_LOCATION) == PackageManager.PERMISSION_GRANTED);
    }
    @Override
    public void onFloorPickerOnClick(int i, View view) {
        if (selectedFloor != null) selectedFloor.setEnabled(true);
        selectedFloor = (Button)view;
        view.setEnabled(false);
        if (floorLayer != null) {
            floorLayer.removeLayerFromMap();
        }
        switch(currentFloorPickerAdapter.getBuildingCode()){
            case H:
                mViewModel.setHallFloorplan(hallGroundOverlay, i==0?9:8);
                // change to class coordinates
                floorLayer = mViewModel.loadPolygons(mMap, getContext(), R.raw.eighthall);
                break;
            case MB:
                mViewModel.setMBFloorplan(mbGroundOverlay, i==0?1:-1);
                // change to class coordinates
                floorLayer = mViewModel.loadPolygons(mMap, getContext(), R.raw.buildingcoordinates);
                break;
            case VL:
                mViewModel.setVLFloorplan(vlGroundOverlay, i==0?2:1);
                // change to class coordinates
                floorLayer = mViewModel.loadPolygons(mMap, getContext(), R.raw.buildingcoordinates);
                break;
                default:
                    return;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if(requestCode == ClassConstants.LOCATION_PERMISSION_REQUEST_CODE)
            myLocationPermissionsGranted = (grantResults.length > 0 &&
                    grantResults[0] == PackageManager.PERMISSION_GRANTED);
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }
}
