package com.example.rafzz.projekt1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Camera;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import static com.example.rafzz.projekt1.R.id.map;


public class MainActivity extends AppCompatActivity implements SensorEventListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener, OnMapReadyCallback {


    private GoogleMap mMap;

    private GoogleApiClient mGoogleApiClient;
    private Location mLastLocation;
    private SensorManager mSensorManager;

    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);



        LightText = (TextView) findViewById(R.id.LightText);
        xText = (TextView) findViewById(R.id.xText);
        yText = (TextView) findViewById(R.id.yText);
        zText = (TextView) findViewById(R.id.zText);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);

        lightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        gyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        compassSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);



    }
    //maps------------------------------------------------------------------------------------------
    public void openMaps(View v){
        Intent i = new Intent(this, MapsActivity.class);
        Bundle bun = new Bundle();
        bun.putDouble("lat",mLastLocation.getLatitude());
        bun.putDouble("lng",mLastLocation.getLongitude());
        i.putExtras(bun);
        startActivity(i);
    }

    //maps------------------------------------------------------------------------------------------

    //flashlight------------------------------------------------------------------------------------
    private Camera c;
    private Camera.Parameters parameters;

    public void light(View v){
        if(!v.isSelected()) {
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            c.setParameters(parameters);
            c.startPreview();
            v.setSelected(true);
        }else if(v.isSelected()){
            parameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            c.setParameters(parameters);
            c.stopPreview();
            v.setSelected(false);
        }
    }
    //flashlight------------------------------------------------------------------------------------

    //GPS-------------------------------------------------------------------------------------------
    private TextView mLatitudeText;
    private TextView mLongitudeText;

    protected void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    protected void onStop() {
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        mLatitudeText = (TextView) findViewById(R.id.mLatitudeText);
        mLongitudeText = (TextView) findViewById(R.id.mLongitudeText);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }

        double lat = mLastLocation.getLatitude();
        double lng = mLastLocation.getLongitude();


        LatLng ll = new LatLng(lat,lng);
        Marker m = mMap.addMarker(new MarkerOptions().position(ll).title("Sart_Marker"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,14));

    }

    public void refresh(View view) {

        mLatitudeText = (TextView) findViewById(R.id.mLatitudeText);
        mLongitudeText = (TextView) findViewById(R.id.mLongitudeText);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            //Double lat = mLastLocation.getLatitude();
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }
        //recreate();
        mGoogleApiClient.disconnect();
        mGoogleApiClient.connect();
        mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
        mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));

    }

    @Override
    public void onConnectionSuspended(int i) {}
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
    //GPS-------------------------------------------------------------------------------------------




    //light sensor, gyroscope, compass--------------------------------------------------------------
    private static final float NS2S = 1.0f / 1000000000.0f;
    private final float[] deltaRotationVector = new float[4];
    private final double EPSILON = 0.00001;
    private float timestamp;

    private TextView LightText;
    private TextView xText;
    private TextView yText;
    private TextView zText;

    private Sensor lightSensor;
    private Sensor gyroSensor;
    private Sensor compassSensor;

    @Override
    public void onSensorChanged(SensorEvent event) {
        // The light sensor returns a single value.
        // Many sensors return 3 values, one for each axis.

        if(event.sensor.getType()==Sensor.TYPE_LIGHT){

            float lux = event.values[0];
            LightText.setText(String.valueOf(lux));
        }

        if(event.sensor.getType()==Sensor.TYPE_GYROSCOPE) {

            if (timestamp != 0) {
                final float dT = (event.timestamp - timestamp) * NS2S;
                // Axis of the rotation sample, not normalized yet.
                float axisX = event.values[0];
                float axisY = event.values[1];
                float axisZ = event.values[2];


                xText.setText(String.valueOf(axisX));
                yText.setText(String.valueOf(axisY));
                zText.setText(String.valueOf(axisZ));

                // Calculate the angular speed of the sample
                float omegaMagnitude = (float) Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

                // Normalize the rotation vector if it's big enough to get the axis
                // (that is, EPSILON should represent your maximum allowable margin of error)
                if (omegaMagnitude > EPSILON) {
                    axisX /= omegaMagnitude;
                    axisY /= omegaMagnitude;
                    axisZ /= omegaMagnitude;
                }

                // Integrate around this axis with the angular speed by the timestep
                // in order to get a delta rotation from this sample over the timestep
                // We will convert this axis-angle representation of the delta rotation
                // into a quaternion before turning it into the rotation matrix.
                float thetaOverTwo = omegaMagnitude * dT / 2.0f;
                float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
                float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
                deltaRotationVector[0] = sinThetaOverTwo * axisX;
                deltaRotationVector[1] = sinThetaOverTwo * axisY;
                deltaRotationVector[2] = sinThetaOverTwo * axisZ;
                deltaRotationVector[3] = cosThetaOverTwo;
            }
            timestamp = event.timestamp;
            float[] deltaRotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, deltaRotationVector);
            // User code should concatenate the delta rotation we computed with the current rotation
            // in order to get the updated rotation.
            // rotationCurrent = rotationCurrent * deltaRotationMatrix;

        }

        if(event.sensor.getType()==Sensor.TYPE_MAGNETIC_FIELD) {
            TextView compassText = (TextView) findViewById(R.id.compassText);
            compassText.setText(String.valueOf((event.values[0]+"  ,  "+event.values[1]+"  ,  "+event.values[2])));
        }

    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}

    protected void onResume(){
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE), SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD), SensorManager.SENSOR_DELAY_NORMAL);
        mGoogleApiClient.connect();

        if(getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            c = Camera.open();   //flashlight
            parameters = c.getParameters();

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);
        c.release();//flashlight
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        //LatLng sydney = new LatLng(-34, 151);
        //mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
        //mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mMap.setMyLocationEnabled(true);
        /*
        double lat = mLastLocation.getLatitude();
        double lng = mLastLocation.getLongitude();


        LatLng ll = new LatLng(lat,lng);
        Marker m = mMap.addMarker(new MarkerOptions().position(ll).title("Sart_Marker"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(ll,14));
        */
    }
    //light sensor, gyroscope, compass--------------------------------------------------------------


}
