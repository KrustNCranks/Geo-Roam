package com.chathu.georoam.controller;

import android.app.Activity;
import android.location.Location;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.chathu.georoam.view.SearchMapActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import static android.support.constraint.Constraints.TAG;

public class DeviceLocatorController {

    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean permission_granted = false;

    /**
     * This Funtions gets the device's locations and moves the camera to the current location
     */
    public void getDeviceLocation(final Activity page, Boolean permission_granted, final GoogleMap mMap){
        Log.d(TAG,"getDeviceLocation(): TRYING TO GET DEVICE LOCATION");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(page);
        try{
            if(permission_granted){
                Task location = mFusedLocationProviderClient.getLastLocation();
                location.addOnCompleteListener(new OnCompleteListener() {
                    @Override
                    public void onComplete(@NonNull Task task) {
                        if(task.isSuccessful()){
                            Log.d(TAG,"getDeviceLocation(): LOCATION HAS BEEN FOUND");
                            Location currentLocation = (Location) task.getResult();
                            mMap.setMyLocationEnabled(true);
                            mMap.getUiSettings().setMyLocationButtonEnabled(true);
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),15));
                        }
                        else
                        {
                            Log.e(TAG, "onComplete: LOCATION NOT FOUND");
                            Toast.makeText(page, "Location Could Not Be Found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG,"Security Exception: "+e.getMessage());
        }
    }
}
