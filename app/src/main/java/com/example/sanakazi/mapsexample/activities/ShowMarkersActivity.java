package com.example.sanakazi.mapsexample.activities;

import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.example.sanakazi.mapsexample.R;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

public class ShowMarkersActivity extends FragmentActivity {

    private GoogleMap mMap;
    LatLngBounds.Builder builder;
    CameraUpdate cu;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_markers);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMap =mapFragment.getMap();
       // mMap.setMyLocationEnabled(true);
        setMarker();
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getBaseContext(), "Hie",Toast.LENGTH_LONG).show();
            }
        });
    }

    private void setMarker() {

        List<Marker> markersList = new ArrayList<Marker>();
        Marker Delhi = mMap.addMarker(new MarkerOptions().position(new LatLng(
                28.61, 77.2099)).title("Delhi"));
        Marker Chaandigarh = mMap.addMarker(new MarkerOptions().position(new LatLng(
                30.75, 76.78)).title("Chandigarh"));
        Marker SriLanka = mMap.addMarker(new MarkerOptions().position(new LatLng(
                7.000, 81.0000)).title("Sri Lanka"));
        Marker America = mMap.addMarker(new MarkerOptions().position(new LatLng(
                38.8833, 77.0167)).title("America"));
        Marker Arab = mMap.addMarker(new MarkerOptions().position(new LatLng(
                24.000, 45.000)).title("Arab"));

        /**Put all the markers into arraylist*/
        markersList.add(Delhi);
        markersList.add(SriLanka);
        markersList.add(America);
        markersList.add(Arab);
        markersList.add(Chaandigarh);


        /**create for loop for get the latLngbuilder from the marker list*/
        builder = new LatLngBounds.Builder();
        for (Marker m : markersList) {
            builder.include(m.getPosition());
        }
        /**initialize the padding for map boundary*/
        int padding = 50;
        /**create the bounds from latlngBuilder to set into map camera*/
        LatLngBounds bounds = builder.build();
        /**create the camera with bounds and padding to set into map*/
        cu = CameraUpdateFactory.newLatLngBounds(bounds, padding);
        /**call the map call back to know map is loaded or not*/
        mMap.setOnMapLoadedCallback(new GoogleMap.OnMapLoadedCallback() {
            @Override
            public void onMapLoaded() {
                /**set animated zoom camera into map*/
                mMap.animateCamera(cu);

            }
        });


    }

}