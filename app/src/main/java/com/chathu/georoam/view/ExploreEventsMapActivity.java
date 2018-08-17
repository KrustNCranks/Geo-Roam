package com.chathu.georoam.view;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.chathu.georoam.R;
import com.chathu.georoam.model.EventsModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlaceAutocompleteFragment;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import de.hdodenhof.circleimageview.CircleImageView;

public class ExploreEventsMapActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String TAG = "MapsActivityController";
    private static final String FINE_LOCATION = Manifest.permission.ACCESS_FINE_LOCATION;
    private static final String COARSE_LOCATION = Manifest.permission.ACCESS_COARSE_LOCATION;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private Boolean permission_granted = false;
    private GoogleMap mMap;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListen;
    private DatabaseReference myRef;
    private String userID;

    /**
     * this is the onCreate function for the Maps Activity, once the activity loads this sets up a fragment and starts loading
     * the map
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.search_map_activity);

        // This get Location permissions and initializes the map
        getLocationPermission();

        // This gets the device location and points it out on the Map
        getDeviceLocation();

        // This helps you search and navigate to a place
        findPlace();


        // Access the Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        // Get userID
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        // This is an Auth listener which pays attention on any change of state,
        authListen = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if(user != null){
                    Log.d(TAG, "Profile user: "+user.getUid());
                }else{
                    Log.d(TAG, "FAILED");
                }
            }
        };
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

       retrieveData();

    }

    /**
     * This Funtions gets the device's locations and moves the camera to the current location
     */
    private void getDeviceLocation(){
        Log.d(TAG,"getDeviceLocation(): TRYING TO GET DEVICE LOCATION");
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
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
                            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(currentLocation.getLatitude(), currentLocation.getLongitude()),8));
                        }
                        else
                        {
                            Log.e(TAG, "onComplete: LOCATION NOT FOUND");
                            Toast.makeText(ExploreEventsMapActivity.this, "Location Could Not Be Found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        }catch (SecurityException e){
            Log.e(TAG,"Security Exception: "+e.getMessage());
        }
    }

    /**
     * This get user permission to access the device location and internet permissions as well and then initializes the map
     */
    private void getLocationPermission(){
        Log.d(TAG,"getLocationPermission: ASKING FOR LOCATION PERMISSIONS");
        String[] permissions = {FINE_LOCATION, COARSE_LOCATION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),COARSE_LOCATION)== PackageManager.PERMISSION_GRANTED){
                Log.d(TAG,"getLocationPermission: BOTH PERMISSIONS GRANTED");
                permission_granted = true;
                // This Calls the function that initializes the map
                initMap();
            }else{
                Log.e(TAG,"getLocationPermission: PERMISSIONS NOT GRANTED");
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }else{
            Log.e(TAG,"getLocationPermission: PERMISSIONS NOT GRANTED");
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    /**
     * This function uses the search bar and finds places within the maps fragment and navigates to the point using the moveCamera() function
     */
    public void findPlace(){
        PlaceAutocompleteFragment autocompleteFragment = (PlaceAutocompleteFragment)
                getFragmentManager().findFragmentById(R.id.place_autocomplete);

        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(Place place) {
                Log.i(TAG, "Place: " + place.getName());
                Log.i(TAG, "Place: " + place.getLatLng());

                moveCamera(place.getLatLng(), place.getName().toString());
            }
            @Override
            public void onError(Status status) {
                Log.i(TAG, "An error occurred: " + status);
            }
        });
    }

    /**
     * This functions moves the camera depending on the latitude and longitude it uses and gets
     * @param latLng
     * @param title
     */
    public void moveCamera(LatLng latLng, String title){
        if(mMap != null)
        {
            Log.d(TAG, "moveCamera: MOVES TO LATITUDE: " + latLng.latitude + ", LONGITUDE: " + latLng.longitude);
            mMap.clear();
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng,15));
            MarkerOptions options = new MarkerOptions().position(latLng).title(title);
            mMap.addMarker(options);
        }
        else
        {
            Toast.makeText(getApplicationContext(),"Map has not loaded properly", Toast.LENGTH_LONG);
            Log.d(TAG,"moveCamera(): MAP IS NULL!");
        }

    }


    /**
     * This is the function creates the custom marker and places it on the map
     * @param context
     * @param bmp
     * @param _name
     * @return
     */
    public static Bitmap createCustomMarker(final Context context, Bitmap bmp, final String _name) {

        final View marker = ((LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.explore_maps_custom_marker, null);


        CircleImageView markerImage = (CircleImageView) marker.findViewById(R.id.user_dp);
        markerImage.setImageBitmap(bmp);
        TextView txt_name = (TextView)marker.findViewById(R.id.name);
        txt_name.setText(_name);

        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        marker.setLayoutParams(new ViewGroup.LayoutParams(52, ViewGroup.LayoutParams.WRAP_CONTENT));
        marker.measure(displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.layout(0, 0, displayMetrics.widthPixels, displayMetrics.heightPixels);
        marker.buildDrawingCache();
        Bitmap bitmap = Bitmap.createBitmap(marker.getMeasuredWidth(), marker.getMeasuredHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        marker.draw(canvas);
        return bitmap;

    }

    /**
     * This is used to get data from the Firebase Database pertaining to the use and pass it to the method
     * that adds the markers on the map
     */
    private void retrieveData(){
        myRef = FirebaseDatabase.getInstance().getReference("Event_Post").child(userID);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
               for(DataSnapshot ds : dataSnapshot.getChildren()){
                   EventsModel eventsModel = dataSnapshot.getValue(EventsModel.class);
                   addCustomMarkerOnMap(eventsModel.getName(), eventsModel.getLatitude(),eventsModel.getLongitude(),eventsModel.getEventImageURL());
               }

            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    /**
     * This is the function to generate Marks on the Map
     * @param name
     * @param latitude
     * @param longitude
     */
    private void addCustomMarkerOnMap(String name, Double latitude, Double longitude, String eventPictureURL){

        final String eventName = name;
        final Double evenLat = latitude;
        final Double eventLong = longitude;
        final String pictureURL = eventPictureURL;

        Picasso.get().load(pictureURL).into(new Target() {
            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                LatLng customMarkerLocationOne = new LatLng(evenLat, eventLong);
                mMap.addMarker(new MarkerOptions().position(customMarkerLocationOne).
                        icon(BitmapDescriptorFactory.fromBitmap(
                                createCustomMarker(ExploreEventsMapActivity.this, bitmap,eventName))));
            }

            @Override
            public void onBitmapFailed(Exception e, Drawable errorDrawable) {

            }

            @Override
            public void onPrepareLoad(Drawable placeHolderDrawable) {

            }
        });


    }

}
