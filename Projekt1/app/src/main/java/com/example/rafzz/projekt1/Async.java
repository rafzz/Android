package com.example.rafzz.projekt1;

import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.rafzz.projekt1.MainActivity.isActive;

/**
 * Created by rafzz on 03.12.2016.
 */

public class Async extends AsyncTask<Void,Location,Void> implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private GoogleApiClient mGoogleApiClient;
    private TextView mLongitudeText;
    private TextView mLatitudeText;

    public void set_mMap(GoogleMap mMap) {
        this.mMap = mMap;
    }

    private GoogleMap mMap;

    public Location get_mLastLocation() {
        return mLastLocation;
    }

    private Location mLastLocation;
    private Context con;

    public Location getFirstLocation() {
        return firstLocation;
    }

    private Location firstLocation;

    private boolean markerFlag =false;

    public Async(TextView LongitudeText, TextView LatitudeText, Context con, GoogleMap mMap) {
        this.mLongitudeText = LongitudeText;
        this.mLatitudeText = LatitudeText;
        this.con = con;
        this.mMap = mMap;

    }


    @Override
    protected Void doInBackground(Void... objects) {

        if (ActivityCompat.checkSelfPermission(con, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(con, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return null;
        }
        while(isActive) {
            Location l;
            l = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
            if(firstLocation==null){
                if(l!=null) {
                    firstLocation = l;
                    markerFlag=true;
                }
            }
            if(l!=null) {
                publishProgress(l);
            }


            //return;
            if(false){
                return null;
            }
        }


        return null;
    }

    @Override
    protected void onProgressUpdate(Location... values) {
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(values[0].getLatitude()));
            mLongitudeText.setText(String.valueOf(values[0].getLongitude()));



            if (markerFlag) {
                double lat = firstLocation.getLatitude();
                double lng = firstLocation.getLongitude();


                LatLng ll = new LatLng(lat, lng);
                Marker  m = mMap.addMarker(new MarkerOptions().position(ll));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 14));
                markerFlag = false;
            }
        }
    }

    public void mark(){
        double lat = mLastLocation.getLatitude();
        double lng = mLastLocation.getLongitude();


        LatLng ll = new LatLng(lat, lng);
        Marker  m = mMap.addMarker(new MarkerOptions().position(ll));
    }

    @Override
    protected void onCancelled() {
        mGoogleApiClient.disconnect();
    }

    @Override
    protected void onPreExecute() {
        //super.onPreExecute();

        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(con)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mGoogleApiClient.connect();
        if (mLastLocation != null) {


            double lat = mLastLocation.getLatitude();
            double lng = mLastLocation.getLongitude();

            if (mMap != null) {
                LatLng ll = new LatLng(lat, lng);

                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll, 14));
            }
        }

    }



    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(con, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(con, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }
}
