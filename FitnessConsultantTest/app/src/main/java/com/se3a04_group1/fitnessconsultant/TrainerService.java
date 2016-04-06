package com.se3a04_group1.fitnessconsultant;

import android.Manifest;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by Jay on 4/5/2016.
 */
public class TrainerService extends Service {
    private LocationManager locManager;
    private LocationListener locListener = new MyLocationListener();
    static final Double EARTH_RADIUS = 6371.00;

    private boolean gpsEnabled = false;
    private boolean networkEnabled = false;

    private Handler handler = new Handler();
    Thread t;

    @Override
    public IBinder onBind(Intent intent){return null;}

    @Override
    public void onCreate(){

    }

    @Override
    public void onDestroy(){
        super.onDestroy();
        Toast.makeText(this, "Distance Calculator Done", Toast.LENGTH_LONG).show();

        // Calculates v02 Max upon service closure
        SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPref.edit();
        float maxDistance = sharedPref.getFloat(getString(R.string.saved_distance),0);
        editor.putFloat(getString(R.string.tempt_v02Max), calculateV02Max(maxDistance));
        editor.commit();
       // Toast.makeText(this, sharedPref.getFloat(getString(R.string.saved_v02Max),-1.0f) + "", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onStart(Intent intent, int startid){

    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startID){
        Toast.makeText(getApplicationContext(), "Distance Calculator Started", Toast.LENGTH_SHORT).show();

        final Runnable r = new Runnable(){
            public void run(){
                location();
                // Posts updated distance every 5 seconds
                handler.postDelayed(this,5000);
            }
        };
        handler.postDelayed(r,5000);
        return START_STICKY;
    }

    // Grabs locations
    public void location(){
        locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        try{
            gpsEnabled = locManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        } catch(Exception ex){

        }

        try{
            networkEnabled = locManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        } catch(Exception ex){

        }

        if (gpsEnabled){
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,0,0,locListener);
            }
        }

        if (networkEnabled){
            if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED
                    || checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                locManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER,0,0,locListener);
            }
        }
    }

    // Calculates v02 Max
    public float calculateV02Max(float distance){

        return (float)((distance - 504.9)/44.73);
    }



     private class MyLocationListener implements LocationListener {

        double oldLat=0.0;
        double oldLon=0.0;
        double newLat=0.0;
        double newLon=0.0;
        double time=10;
        double maxDistance = 0;

        // Constantly adds to max distance
        @Override
        public void onLocationChanged(Location location) {
            newLat = location.getLongitude();
            newLon = location.getLongitude();
            String longitude = "Longitude " + location.getLongitude();
            String latitude = "Latitude: " + location.getLatitude();
            maxDistance += calculateDistance(newLat, newLon, oldLat, oldLon);

            // Saves maxDistance
            SharedPreferences sharedPref = getSharedPreferences(getString(R.string.preference_file_key), Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putFloat(getString(R.string.saved_distance),(float) maxDistance);
            editor.commit();

            Toast.makeText(getApplicationContext(),oldLat + "\n",Toast.LENGTH_SHORT).show();
            oldLat = newLat;
            oldLon = newLon;
        }

        @Override
        public void onProviderDisabled(String provider) {
            Toast.makeText(getApplicationContext(), "Gps Disabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onProviderEnabled(String provider) {
            Toast.makeText( getApplicationContext(), "Gps Enabled", Toast.LENGTH_SHORT).show();
        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            // called when the status of the GPS provider changes
        }

        // Calculates Distance
        public double calculateDistance(double lat1, double lon1, double lat2, double lon2){
            double radius = EARTH_RADIUS;
            double dLat = Math.toRadians(lat2-lat1);
            double dLon = Math.toRadians(lon2-lon1);
            double a = Math.sin(dLat/2) * Math.sin(dLat/2) + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) * Math.sin(dLon/2) * Math.sin(dLon/2);
            double c = 2* Math.asin(Math.sqrt(a));
            return radius*c;
        }

        public double getMaxDistance(){
            return maxDistance;
        }
    }
}
