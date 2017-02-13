package com.example.rafzz.projekt1;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
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
import android.widget.ToggleButton;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;


import static com.example.rafzz.projekt1.R.id.map;

public class MainActivity extends AppCompatActivity
        implements SensorEventListener,
        OnMapReadyCallback, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final static String LATITUDE_MESSAGE = "latitude";
    private final static String LONGITUDE_MESSAGE = "longitude";

    private final int REFRESH_INTERVAL = 1000;
    private final int MAP_ZOOM = 14;

    private final float NS2S = 1.0f / 1000000000.0f;
    private final float[] DELTA_ROTATION_VECTOR = new float[4];
    private final double EPSILON = 0.00001;
    private float timestamp;

    private static Location mLastLocation;
    protected static GoogleApiClient mGoogleApiClient;

    private TextView LightText;
    private TextView xText;
    private TextView yText;
    private TextView zText;
    private TextView compassText;

    private Camera camera;
    private Camera.Parameters cameraParameters;
    private ToggleButton toggleLightButton;

    private LocationRequest locationRequest;
    private GoogleMap mMap;

    private Sensor lightSensor;
    private Sensor gyroSensor;
    private Sensor compassSensor;

    private SensorManager mSensorManager;
    private TextView mLatitudeText;
    private TextView mLongitudeText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_NOSENSOR);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(map);
        mapFragment.getMapAsync(this);


        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }

        mLatitudeText = (TextView) findViewById(R.id.mLatitudeText);
        mLongitudeText = (TextView) findViewById(R.id.mLongitudeText);
        LightText = (TextView) findViewById(R.id.LightText);
        xText = (TextView) findViewById(R.id.xText);
        yText = (TextView) findViewById(R.id.yText);
        zText = (TextView) findViewById(R.id.zText);

        mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        lightSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT);
        gyroSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE);
        compassSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onResume() {
        mGoogleApiClient.connect();
        super.onResume();
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_LIGHT),
                SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_GYROSCOPE),
                SensorManager.SENSOR_DELAY_NORMAL);
        mSensorManager.registerListener(this, mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD),
                SensorManager.SENSOR_DELAY_NORMAL);

        if (getApplicationContext().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_FLASH)) {
            camera = Camera.open();
            cameraParameters = camera.getParameters();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSensorManager.unregisterListener(this);

        camera.release();
        toggleLightButton = (ToggleButton) findViewById(R.id.toggleLightButton);
        toggleLightButton.setChecked(false);
        toggleLightButton.setSelected(false);
    }

    //lightMethods
    public void light(View button) {
        if ( !button.isSelected() ) {
            cameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_TORCH);
            camera.setParameters(cameraParameters);
            camera.startPreview();
            button.setSelected(true);
        } else if ( button.isSelected() ) {
            cameraParameters.setFlashMode(Camera.Parameters.FLASH_MODE_OFF);
            camera.setParameters(cameraParameters);
            camera.stopPreview();
            button.setSelected(false);
        }
    }
    //lightMethods

    //sensorsMethods
    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_LIGHT) {

            float lux = event.values[0];
            LightText.setText(String.valueOf(lux));
        }

        if (event.sensor.getType() == Sensor.TYPE_GYROSCOPE) {

            if (timestamp != 0) {
                final float dT = (event.timestamp - timestamp) * NS2S;
                float axisX = event.values[0];
                float axisY = event.values[1];
                float axisZ = event.values[2];


                xText.setText(String.valueOf(axisX));
                yText.setText(String.valueOf(axisY));
                zText.setText(String.valueOf(axisZ));

                float omegaMagnitude = (float) Math.sqrt(axisX * axisX + axisY * axisY + axisZ * axisZ);

                if (omegaMagnitude > EPSILON) {
                    axisX /= omegaMagnitude;
                    axisY /= omegaMagnitude;
                    axisZ /= omegaMagnitude;
                }

                float thetaOverTwo = omegaMagnitude * dT / 2.0f;
                float sinThetaOverTwo = (float) Math.sin(thetaOverTwo);
                float cosThetaOverTwo = (float) Math.cos(thetaOverTwo);
                DELTA_ROTATION_VECTOR[0] = sinThetaOverTwo * axisX;
                DELTA_ROTATION_VECTOR[1] = sinThetaOverTwo * axisY;
                DELTA_ROTATION_VECTOR[2] = sinThetaOverTwo * axisZ;
                DELTA_ROTATION_VECTOR[3] = cosThetaOverTwo;
            }
            timestamp = event.timestamp;
            float[] deltaRotationMatrix = new float[9];
            SensorManager.getRotationMatrixFromVector(deltaRotationMatrix, DELTA_ROTATION_VECTOR);

        }

        if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {
            compassText = (TextView) findViewById(R.id.compassText);
            compassText.setText(String.valueOf((event.values[0] + "  ,  " + event.values[1] + "  ,  " + event.values[2])));
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {}
    //sensorMethods

    //mapAndLocationMethods
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                        != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

    public void openMap(View button) {
        Intent mapIntent = new Intent(this, MapsActivity.class);
        Bundle locationBundle = new Bundle();
        locationBundle.putDouble(LATITUDE_MESSAGE, mLastLocation.getLatitude());
        locationBundle.putDouble(LONGITUDE_MESSAGE, mLastLocation.getLongitude());
        mapIntent.putExtras(locationBundle);
        startActivity(mapIntent);
    }

    public static String getLATITUDE_MESSAGE() {
        return LATITUDE_MESSAGE;
    }

    public static String getLONGITUDE_MESSAGE() {
        return LONGITUDE_MESSAGE;
    }

    @Override
    public void onLocationChanged(Location location) {
        mLastLocation = location;
        mLatitudeText.setText(String.valueOf(location.getLatitude()));
        mLongitudeText.setText(String.valueOf(location.getLongitude()));
        double lat = mLastLocation.getLatitude();
        double lng = mLastLocation.getLongitude();
        LatLng latLng = new LatLng(lat, lng);
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, MAP_ZOOM));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = LocationRequest.create();
        locationRequest.setInterval(REFRESH_INTERVAL);

        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            mLatitudeText.setText(String.valueOf(mLastLocation.getLatitude()));
            mLongitudeText.setText(String.valueOf(mLastLocation.getLongitude()));
        }

        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, locationRequest, this);

    }

    @Override
    public void onConnectionSuspended(int i) {}

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {}
    //mapAndLocationMethods
}
