package com.example.rafzz.projekt1;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    private LatLng mLatLang;
    private LocationRequest locationRequest;

    private static List<LatLng> pathList = new ArrayList<LatLng>();

    private final int REFRESH_INTERVAL = 1000;
    private final int MAP_ZOOM = 14;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        Intent mapIntent = getIntent();
        Bundle bundle = mapIntent.getExtras();
        LatLng latlng = new LatLng(bundle.getDouble(MainActivity.getLATITUDE_MESSAGE()),
                bundle.getDouble(MainActivity.getLONGITUDE_MESSAGE()));
        mLatLang = latlng;

        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latlng, MAP_ZOOM));
        mMap.addMarker(new MarkerOptions().position(latlng));

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(REFRESH_INTERVAL);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(MainActivity.mGoogleApiClient, locationRequest, this);
    }

    @Override
    public void onLocationChanged(Location location) {

        pathList.add(new LatLng(location.getLatitude(),location.getLongitude()));
        mMap.addPolyline(new PolylineOptions()
                .addAll(pathList)
                .width(5).color(Color.BLUE).geodesic(true));
    }

    public void backToMain(View v){
        this.finish();
    }
}


