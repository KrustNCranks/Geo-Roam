package com.chathu.georoam.view;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.chathu.georoam.R;
import com.chathu.georoam.controller.PermissionsController;
import com.chathu.georoam.model.EventsModel;
import com.chathu.georoam.model.UserEventUpload;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class AddEventImageActivity extends AppCompatActivity {
    private static final String TAG = "PostEvent";
    private static final String WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean permission_granted = false;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private StorageReference storageReference;
    private FirebaseAuth.AuthStateListener authListen;

    private static final int GALLERY_INTENT = 2;
    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    private ImageView eventImage;
    private Button addEvent;
    private Button cancel;

    private PermissionsController permissionsController = PermissionsController.getInstance();
    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_image_activity);

        // This code is for hiding the Action Bar in The Splash Screen Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Adding XML Variables to the Controller in order to manipulate them
        eventImage = (ImageView) findViewById(R.id.eventImage);
        addEvent = (Button) findViewById(R.id.addEventButton);
        cancel = (Button) findViewById(R.id.cancelEventButton);


        // Access the Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        // Initialize Database
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();

        // Initialize Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference();

        // Get userID
        FirebaseUser user = mAuth.getCurrentUser();
        // userID = user.getUid();

        // This is the onClick event for adding pictures
        eventImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        // This is the onClick event for adding an event to the database
        addEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadEventDetails();
            }
        });

        // This is the onClick event for the Cancel Button
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddEventImageActivity.this, AddPostActivity.class));
            }
        });

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
     * This onStart method checks to see if the user is signed in or not, basically checks for a non null value
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();


    }


    /**
     * This is the chooseImage option that creates an image chooser dialog that allows the user to browse through the
     * device gallery to select the image
     */
    private void chooseImage(){
        permissionsController.checkStoragePermission(AddEventImageActivity.this);
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent.createChooser(intent,"Choose your Picture"),PICK_IMAGE_REQUEST);
    }

    /**
     * This onActivity Result works and checks if the request Code is equal to the PICK_IMAGE_REQUEST
     * and checks if an image is chosen and will display it in the ImageView
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                eventImage.setImageBitmap(bitmap);
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }


    /**
     * This method returns the File type Extension of the Image to be uploaded
     * @param uri
     * @return
     */
    private String getFileTypeExtension(Uri uri){
        ContentResolver cr = getContentResolver();
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    /**
     * This is a tricky method that uploads the image and then gets the Image URL in String format and then uploads this to
     * along with the other event information to the Firebase Realtime Database
     */
    private void uploadEventDetails(){
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Your Event is Being Posted");
            progressDialog.show();

            final StorageReference ref = storageReference.child("event_images/"+ UUID.randomUUID().toString()+"."+getFileTypeExtension(filePath));
            UploadTask uploadTask = ref.putFile(filePath);

            uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                @Override
                public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                    if(!task.isSuccessful()){
                        progressDialog.dismiss();
                        throw task.getException();
                    }
                    return ref.getDownloadUrl();
                }
            }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                @Override
                public void onComplete(@NonNull Task<Uri> task) {
                    if(task.isSuccessful()){
                        progressDialog.dismiss();
                        Uri taskResult = task.getResult();
                        UserEventUpload imageUpload = new UserEventUpload(taskResult.toString());
                        // updateDetails(imageUpload.getImageName(),imageUpload.getImageUrl());

                        final String eventName = getIntent().getStringExtra("EventName");
                        final String eventDescription = getIntent().getStringExtra("EventDescription");
                        final String eventStartDate = getIntent().getStringExtra("EventStartDate");
                        final String eventEndDate = getIntent().getStringExtra("EventEndDate");
                        final String locationEvent = getIntent().getStringExtra("LocationName");
                        final String locationAddress = getIntent().getStringExtra("LocationAddress");
                        //final LatLng locationCoordinates = getIntent().getExtras().getParcelable("LocationCoordinates");
                        final Double latitude = getIntent().getDoubleExtra("latitude",0.00);
                        final Double longitude = getIntent().getDoubleExtra("longitude",0.00);
                        final String imageUrl = imageUpload.getImageUrl();
                        final String userID = mAuth.getCurrentUser().getUid();
                        String isPrivate = getIntent().getStringExtra("Status");
                        String isPublic = "public";
                        String isitPrivate = "private";

                        final EventsModel event = new EventsModel(
                                eventName,
                                eventDescription,
                                eventStartDate,
                                eventEndDate,
                                locationEvent,
                                locationAddress,
                                latitude,
                                longitude,
                                imageUrl,
                                userID,
                                isPrivate
                        );

                        if (isPrivate.equals(isPublic)){
                            FirebaseDatabase.getInstance().getReference("Event_Post_Public").child("public_pictures").push().setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(AddEventImageActivity.this, "EVENT CREATED",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddEventImageActivity.this, EventPostedActivity.class));
                                    }else{
                                        // If Fail, it will display and error message as a toast

                                        Toast.makeText(AddEventImageActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }else if (isPrivate.equals(isitPrivate)){
                            // This get an instance of the firebase database and uses this instance to post the object to the real time online database
                            FirebaseDatabase.getInstance().getReference("Event_Post").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push()
                                    .setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // If successfull this will navigate to the get started page
                                        Toast.makeText(AddEventImageActivity.this, "EVENT CREATED",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddEventImageActivity.this, EventPostedActivity.class));
                                    } else {
                                        // If Fail, it will display and error message as a toast
                                        Toast.makeText(AddEventImageActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                    }
                                }
                            });
                        }

                        // This get an instance of the firebase database and uses this instance to post the object to the real time online database
                        FirebaseDatabase.getInstance().getReference("Event_Post").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push()
                                .setValue(event).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // If successfull this will navigate to the get started page
                                    Toast.makeText(AddEventImageActivity.this, "EVENT CREATED",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AddEventImageActivity.this, EventPostedActivity.class));
                                } else {
                                    // If Fail, it will display and error message as a toast
                                    Toast.makeText(AddEventImageActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    }
                }
                    }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(AddEventImageActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });


        }

    }
}
