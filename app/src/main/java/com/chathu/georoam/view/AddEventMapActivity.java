package com.chathu.georoam.view;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.widget.Toast;

import com.chathu.georoam.R;
import com.chathu.georoam.controller.DeviceLocatorController;
import com.chathu.georoam.controller.PermissionsController;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class AddEventMapActivity extends FragmentActivity implements OnMapReadyCallback {
    private static final String TAG = "ChooseLocation";
    private Boolean permission_granted = false;
    private GoogleMap mMap;
    private static final int PLACE_PICKER_REQUEST = 1;

    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_location_activity);


        // This get Location permissions and initializes the map
        PermissionsController perms = new PermissionsController();
        if (perms.getLocationPermission(AddEventMapActivity.this))
        {
            permission_granted = true;
            initMap();
        }


        // This initializes the place picker at runtime
        placePicker();

    }

    /**
     * This fires use the Google Places API place picker
     */
    private void placePicker(){
        PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
        try{
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        }catch (GooglePlayServicesNotAvailableException e){
            e.printStackTrace();
        }catch (GooglePlayServicesRepairableException f){
            f.printStackTrace();
        }

    }

    /**
     * This will get the data using the Google Places API place picker information, changes it to String data and
     * passes it along to the final activity to post it on the Firebase Database
     * @param requestCode
     * @param resultCode
     * @param data
     */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String locationName = place.getName().toString();
                String locationAddress = place.getAddress().toString();
                Double latitude = place.getLatLng().latitude;
                Double longitude = place.getLatLng().longitude;
                //LatLng locationCoordinates = place.getLatLng();
                String name = getIntent().getStringExtra("EventName");
                String description = getIntent().getStringExtra("EventDescription");
                String startDate = getIntent().getStringExtra("EventStartDate");
                String endDate = getIntent().getStringExtra("EventEndDate");
                String isPrivate = getIntent().getStringExtra("Status");
                //mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(locationCoordinates.latitude, locationCoordinates.longitude),15));
                Intent intent = new Intent( AddEventMapActivity.this, AddEventImageActivity.class );
                intent.putExtra ( "EventName", name );
                intent.putExtra ( "EventDescription", description);
                intent.putExtra("EventStartDate",startDate);
                intent.putExtra("EventEndDate",endDate);
                intent.putExtra ( "LocationName", locationName );
                intent.putExtra ( "LocationAddress", locationAddress );
                //intent.putExtra("LocationCoordinates",locationCoordinates);
                intent.putExtra("latitude",latitude);
                intent.putExtra("longitude",longitude);
                intent.putExtra("Status",isPrivate);
                startActivity(intent);
            }
        }
    }

    /**
     * This is the function that is used to initialize and load the map fragment
     */
    private void initMap(){
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

    }


    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        /**
         * This Will call the Device Locator Controller and gets the device's locations and moves the
         * camera to the current location
         */
        DeviceLocatorController deviceLocatorController = new DeviceLocatorController();
        deviceLocatorController.getDeviceLocation(AddEventMapActivity.this, permission_granted,mMap);

    }



}
