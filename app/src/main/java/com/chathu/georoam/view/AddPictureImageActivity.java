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
import com.chathu.georoam.model.LatLong;
import com.chathu.georoam.model.Pictures;
import com.chathu.georoam.model.UserPictureUpload;
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

public class AddPictureImageActivity extends AppCompatActivity {


    private static final String TAG = "PostPicture";
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

    private ImageView imagePicture;
    private Button addPicture;
    private Button cancel;
    private String userID;
    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_picture_image_activity);

        // This code is for hiding the Action Bar in The Splash Screen Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Adding XML Variables to the Controller in order to manipulate them
        imagePicture = (ImageView) findViewById(R.id.picture);
        addPicture = (Button) findViewById(R.id.addPictureButton);
        cancel = (Button) findViewById(R.id.cancelPictureButton);

        // This is the onClick event for adding pictures
        imagePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        // This is the onClick event for adding an event to the database
        addPicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadEventDetails();
            }
        });

        // This is the onClick event for the Cancel Button
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AddPictureImageActivity.this, AddPostActivity.class));
            }
        });


        // Access the Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        // Initialize Database
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference();

        // Initialize Firebase Storage
        storageReference = FirebaseStorage.getInstance().getReference();

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
     * This onStart method checks to see if the user is signed in or not, basically checks for a non null value
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }

    /**
     * This will check read write permissions
     */
    private void checkPermission(){
        Log.d(TAG,"getFilePermission: ASKING FOR STORAGE PERMISSIONS");
        String[] permissions = {WRITE_PERMISSION, READ_PERMISSION};

        if(ContextCompat.checkSelfPermission(this.getApplicationContext(),WRITE_PERMISSION)== PackageManager.PERMISSION_GRANTED){
            if(ContextCompat.checkSelfPermission(this.getApplicationContext(),READ_PERMISSION)== PackageManager.PERMISSION_GRANTED){
                Log.d(TAG,"checkPermission: BOTH PERMISSIONS GRANTED");
                permission_granted = true;
            }else{
                Log.e(TAG,"checkPermission: PERMISSIONS NOT GRANTED");
                ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
            }
        }
        else{
            Log.e(TAG,"checkPermission: PERMISSIONS NOT GRANTED");
            ActivityCompat.requestPermissions(this, permissions, LOCATION_PERMISSION_REQUEST_CODE);
        }

    }

    /**
     * This is the chooseImage option that creates an image chooser dialog that allows the user to browse through the
     * device gallery to select the image
     */
    private void chooseImage(){
        //checkPermission();
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
                imagePicture.setImageBitmap(bitmap);
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
            progressDialog.setTitle("Your Picture is Being Posted");
            progressDialog.show();

            final StorageReference ref = storageReference.child("picture_post_images/"+ UUID.randomUUID().toString()+"."+getFileTypeExtension(filePath));
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
                        UserPictureUpload imageUpload = new UserPictureUpload(taskResult.toString());

                        final String pictureName = getIntent().getStringExtra("PictureName");
                        final String pictureDescription = getIntent().getStringExtra("PictureDescription");
                        final String locationName = getIntent().getStringExtra("LocationName");
                        final String locationAddress = getIntent().getStringExtra("LocationAddress");
                        //final LatLng locationCoordinates = getIntent().getExtras().getParcelable("LocationCoordinates");
                        final Double latitude = getIntent().getDoubleExtra("Latitude",0.00);
                        final Double longitude = getIntent().getDoubleExtra("longitude",0.00);
                        final String isPrivate = getIntent().getStringExtra("Status");
                        final String imageUrl = imageUpload.getImageUrl();
                        final String userID = mAuth.getCurrentUser().getUid();
                        String isPublic = "public";
                        String isitPrivate = "private";

                        Pictures pictures = new Pictures(
                                pictureName,
                                pictureDescription,
                                locationName,
                                locationAddress,
                                latitude,
                                longitude,
                                imageUrl,
                                userID,
                                isPrivate
                        );


                        if(isPrivate.equals(isPublic)){
                            // This get an instance of the firebase database and uses this instance to post the object to the real time online database
                            FirebaseDatabase.getInstance().getReference("Picture_Post_Public").push()
                                    .setValue(pictures).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // If successfull this will navigate to the get started page
                                        Toast.makeText(AddPictureImageActivity.this, "PICTURE POSTED",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddPictureImageActivity.this, PicturePostedActivity.class));
                                    } else {
                                        // If Fail, it will display and error message as a toast

                                        Toast.makeText(AddPictureImageActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                        }else if (isPrivate.equals(isitPrivate)){
                            // This get an instance of the firebase database and uses this instance to post the object to the real time online database
                            FirebaseDatabase.getInstance().getReference("Picture_Post").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push()
                                    .setValue(pictures).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        // If successfull this will navigate to the get started page
                                        Toast.makeText(AddPictureImageActivity.this, "PICTURE POSTED",Toast.LENGTH_SHORT).show();
                                        startActivity(new Intent(AddPictureImageActivity.this, PicturePostedActivity.class));
                                    } else {
                                        // If Fail, it will display and error message as a toast

                                        Toast.makeText(AddPictureImageActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                    }
                                }
                            });
                        }
                        // This get an instance of the firebase database and uses this instance to post the object to the real time online database
                        FirebaseDatabase.getInstance().getReference("Picture_Post").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).push()
                                .setValue(pictures).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    // If successfull this will navigate to the get started page
                                    Toast.makeText(AddPictureImageActivity.this, "PICTURE POSTED",Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(AddPictureImageActivity.this, PicturePostedActivity.class));
                                } else {
                                    // If Fail, it will display and error message as a toast

                                    Toast.makeText(AddPictureImageActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                                }
                            }
                        });

                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddPictureImageActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }

    }
}
