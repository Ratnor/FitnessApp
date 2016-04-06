package com.se3a04_group1.fitnessconsultant;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Created by Jay on 4/5/2016.
 */
public class v02Activity extends FragmentActivity implements OnMapReadyCallback, LocationListener {

    private GoogleMap mMap;
    Marker now;
    LocationManager locManager;

    Button btnTimer,btnBack;
    TextView textTime,textDistance;
    long startTime = 0;

    //runs without a timer by reposting this handler at the end of the runnable
    Handler timerHandler = new Handler();

    // Timer for v02 run
    Runnable timerRunnable = new Runnable() {
        public void run() {
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            long millis = (startTime + 720000) - System.currentTimeMillis();
            int seconds = (int) (millis / 1000);
            int minutes = seconds / 60;
            seconds = seconds % 60;
            textTime.setText(String.format("%d:%02d", minutes, seconds));

            // Displays total distance traveled
            textDistance.setText(sharedPref.getFloat(getString(R.string.saved_distance), -1.0f) + " Meters");

            // Once the timer finishes, calculate v02 Max
            if(millis <= 0 ){
                SharedPreferences.Editor editor = sharedPref.edit();

                float v02Max = sharedPref.getFloat(getString(R.string.tempt_v02Max),-1);
                editor.putFloat(getString(R.string.tempt_v02Max), v02Max);
                editor.commit();
            }

            timerHandler.postDelayed(this, 500);
        }
    };

    @Override
    // Changes stop to start
    public void onPause() {
        super.onPause();
        timerHandler.removeCallbacks(timerRunnable);
        textTime.setText("start");
    }


    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.trainer_layout);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);
        //getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle("Run Tracker");

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);


        btnTimer = (Button) findViewById(R.id.btnTimer);
        btnBack = (Button) findViewById(R.id.btnTimer);
        textTime = (TextView) findViewById(R.id.textTime);
        textDistance = (TextView) findViewById(R.id.textDistance);

        locManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,this);
            locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,this);
        }
    }

    // Sends back to session
    public void sendSession(View view){
        Intent intent = new Intent(this, SessionActivity.class);
        startActivity(intent);
    }

    //
    public void startStop(View v){
        Button b = (Button) v;
        if (b.getText().equals("stop")) {
            timerHandler.removeCallbacks(timerRunnable);

            // Stops service for calculating distance
            Intent intent = new Intent(this,TrainerService.class);
            stopService(intent);
            b.setText("start");
        } else {
            startTime = System.currentTimeMillis();
            timerHandler.postDelayed(timerRunnable, 0);

            // Starts service for calculating distance
            Intent intent = new Intent(this,TrainerService.class);
            startService(intent);

            b.setText("stop");
        }
    }

    // Listener for buttons
    private View.OnClickListener onClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch(v.getId()){
                case R.id.btnTimer:
                    startStop(v);
                    break;
                case R.id.btnBack:
                    sendSession(v);
                    break;
            }
        }
    };

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public void onLocationChanged(Location location) {

        // Removes previous marker
        if(now != null){
            now.remove();
        }

        // Getting latitude of the current location
        double latitude = location.getLatitude();

        // Getting longitude of the current location
        double longitude = location.getLongitude();

        // Creating a LatLng object for the current location
        LatLng latLng = new LatLng(latitude, longitude);
        now = mMap.addMarker(new MarkerOptions().position(latLng).title("You are here"));
        // Showing the current location in Google Map
        mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));

        // Zoom in the Google Map
        mMap.animateCamera(CameraUpdateFactory.zoomTo(15));
    }

    @Override
    public void onProviderDisabled(String arg0) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onProviderEnabled(String provider) {
        // TODO Auto-generated method stub
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        // TODO Auto-generated method stub
    }
}

