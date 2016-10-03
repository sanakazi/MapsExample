package com.example.sanakazi.mapsexample.activities;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.sanakazi.mapsexample.R;
import com.example.sanakazi.mapsexample.pojo.Example;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;

public class ShowRoute extends AppCompatActivity {

    private GoogleMap mMap;
    LatLngBounds.Builder builder;
    CameraUpdate cu;
   // private static final String REGISTER_URL="https://maps.googleapis.com/maps/api/directions/json?origin=Boston,MA&destination=Concord,MA&waypoints=Charlestown,MA|Lexington,MA&key=AIzaSyC22GfkHu9FdgT9SwdCWMwKX1a4aohGifM";
    private static  String REGISTER_URL1;
    private static final String TAG=ShowRoute.class.getSimpleName();
    Polyline line;
    LatLng l1,l2;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_route);
        setUpMapIfNeeded();
    }

    private void setUpMapIfNeeded() {
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mMap =mapFragment.getMap();
       // mMap.setMyLocationEnabled(true);
    //    setMarker();
        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                Toast.makeText(getBaseContext(), "Hie",Toast.LENGTH_LONG).show();
            }
        });

         l1 = new LatLng(28.61,77.2099);
         l2 = new LatLng(30.75, 76.78);
        REGISTER_URL1=getDirectionsUrl(l1,l2);

        //add markers
        setMarker();
        //draw routes
        volleyService();
    }


    private void setMarker() {

        List<Marker> markersList = new ArrayList<Marker>();
        Marker Delhi = mMap.addMarker(new MarkerOptions().position(l1).title("Delhi"));
        Marker Chaandigarh = mMap.addMarker(new MarkerOptions().position(l2).title("Chandigarh"));

        /**Put all the markers into arraylist*/
        markersList.add(Delhi);
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

    private void volleyService(){
        StringRequest stringRequest = new StringRequest(Request.Method.POST, REGISTER_URL1,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.w(TAG,response.toString());


                        try {
                            //Remove previous line from map
                            if (line != null) {
                                line.remove();
                            }

                            Example responseJson = new Gson().fromJson(response.toString(), Example.class);


                            // This loop will go through all the results and add marker on each location.
                            for (int i = 0; i < responseJson.getRoutes().size(); i++) {
                                String distance = responseJson.getRoutes().get(i).getLegs().get(i).getDistance().getText();
                                Log.w(TAG,distance.toString());
                                String time = responseJson.getRoutes().get(i).getLegs().get(i).getDuration().getText();
                                //  ShowDistanceDuration.setText("Distance:" + distance + ", Duration:" + time);
                                String encodedString = responseJson.getRoutes().get(0).getOverviewPolyline().getPoints();
                                List<LatLng> list = decodePoly(encodedString);
                                line = mMap.addPolyline(new PolylineOptions()
                                        .addAll(list)
                                        .width(20)
                                        .color(Color.RED)
                                        .geodesic(true)
                                );
                            }
                        } catch (Exception e) {
                            Log.d("onResponse", "There is an error");
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(ShowRoute.this,error.toString(),Toast.LENGTH_LONG).show();
                    }
                })
                ;

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(stringRequest);

    }

    public static String getDirectionsUrl(LatLng origin, LatLng dest){

        // Origin of route
        String str_origin = "origin="+origin.latitude+","+origin.longitude;

        // Destination of route
        String str_dest = "destination="+dest.latitude+","+dest.longitude;

        // Sensor enabled
        String sensor = "type=driving";

        //String key
        String key = "key="+ "AIzaSyA_2XqnBrg0Pm0jhYBwvfPTxg7d-FxBTH0";

        // Building the parameters to the web service
        String parameters = str_origin+"&"+str_dest+"&"+sensor+"&"+key;

        // Output format
        String output = "json";

        // Building the url to the web service
        String url = "https://maps.googleapis.com/maps/api/directions/"+output+"?"+parameters;
        Log.w(TAG,url.toString());
        return url;
    }

    private List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;
            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);
            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                    (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
    }





}

