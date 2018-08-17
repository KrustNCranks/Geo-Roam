package com.chathu.georoam.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.google.android.gms.location.FusedLocationProviderClient;

public class PermissionsController {

     private static final String TAG = "CustomizeProfile";
    private static final String WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean permission_granted = false;
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private FusedLocationProviderClient mFusedLocationProviderClient;


    /**
     * This will check read write permissions
     */
    public boolean checkPermission(Activity page) {
        Log.d(TAG, "getFilePermission: ASKING FOR STORAGE PERMISSIONS");
        String[] permissions = {WRITE_PERMISSION, READ_PERMISSION};

        if (ContextCompat.checkSelfPermission(page.getApplicationContext(), WRITE_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
            if (ContextCompat.checkSelfPermission(page.getApplicationContext(), READ_PERMISSION) == PackageManager.PERMISSION_GRANTED) {
                Log.d(TAG, "checkPermission: BOTH PERMISSIONS GRANTED");
                permission_granted = true;
            } else {
                Log.e(TAG, "checkPermission: PERMISSIONS NOT GRANTED");
                ActivityCompat.requestPermissions(page, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        } else {
            Log.e(TAG, "checkPermission: PERMISSIONS NOT GRANTED");
            ActivityCompat.requestPermissions(page, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
        return permission_granted;
    }

    /**
     * This get user permission to access the device location and internet permissions as well and then initializes the map
     */
    public boolean getLocationPermission(Activity page){
        Log.d(TAG,"getLocationPermission: ASKING FOR LOCATION PERMISSIONS");
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(page.getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(page.getApplicationContext(),COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                Log.d(TAG,"getLocationPermission: BOTH PERMISSIONS GRANTED");
                permission_granted = true;
                // This Calls the function that initializes the map
                // initMap();
            }else{
                Log.e(TAG,"getLocationPermission: PERMISSIONS NOT GRANTED");
                ActivityCompat.requestPermissions(page, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            Log.e(TAG,"getLocationPermission: PERMISSIONS NOT GRANTED");
            ActivityCompat.requestPermissions(page, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
        return permission_granted;
    }
}
