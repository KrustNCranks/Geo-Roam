package com.chathu.georoam.controller;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

public class PermissionsController {

     private static final String TAG = "CustomizeProfile";
    private static final String WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean permission_granted = false;

    class PermissionController{

    }

    /**
     * This will check read write permissions
     */
    private void checkPermission(Activity page) {
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

    }
}
