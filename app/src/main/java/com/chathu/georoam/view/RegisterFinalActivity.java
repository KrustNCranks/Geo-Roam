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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


import com.chathu.georoam.R;
import com.chathu.georoam.model.ProfilePictureUpload;
import com.chathu.georoam.model.Users;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.util.UUID;

public class RegisterFinalActivity extends AppCompatActivity {

    private static final String TAG = "CustomizeProfile";
    private static final String WRITE_PERMISSION = Manifest.permission.WRITE_EXTERNAL_STORAGE;
    private static final String READ_PERMISSION = Manifest.permission.READ_EXTERNAL_STORAGE;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1234;
    private Boolean permission_granted = false;

    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private DatabaseReference myRef;
    private StorageReference storageReference;
    private FirebaseAuth.AuthStateListener authListen;
    private String userID;

    private EditText address;
    private EditText occupation;
    private Button updateButton;
    private Button skipButton;
    private ImageView profilePicture;

    private static final int GALLERY_INTENT = 2;
    private Uri filePath;

    private final int PICK_IMAGE_REQUEST = 71;

    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_final_activity);

        // This code is for hiding the Action Bar in The Splash Screen Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Adding XML Variables to the Controller in order to manipulate them
        updateButton = (Button) findViewById(R.id.updateButton);
        skipButton = (Button) findViewById(R.id.skipButton);
        address = (EditText) findViewById(R.id.addressTxt);
        occupation = (EditText) findViewById(R.id.occupationTxt);
        profilePicture =(ImageView) findViewById(R.id.profilePictureImage);

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


        profilePicture.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chooseImage();
            }
        });

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });
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
        checkPermission();
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
                profilePicture.setImageBitmap(bitmap);
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
     * This is used to upload the Image into the firebase storage, this will be added to the images folder
     * which will be created after the image has been uploaded
     */
    private void uploadImage(){
        if(filePath != null)
        {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            progressDialog.setTitle("Your Account is Being Created");
            progressDialog.show();

            final StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString()+"."+getFileTypeExtension(filePath));
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
                        String text = "ProfilePicture";
                        ProfilePictureUpload imageUpload = new ProfilePictureUpload(getIntent().getStringExtra("Name")+text,taskResult.toString());
                        updateDetails(imageUpload.getImageName(),imageUpload.getImageUrl());
                    }
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterFinalActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
    }

    /**
     * This will upload the user details to the Firebase Database
     */
    private void updateDetails(String userPictureName, String userPictureURL){
        final String userAddress = address.getText().toString().trim();
        final String userOccupation = occupation.getText().toString().trim();
        final String nameUser = getIntent().getStringExtra("Name");
        final String emailUser = getIntent().getStringExtra("Email");
        final String usernameUser = getIntent().getStringExtra("Username");
        final String passwordUser = getIntent().getStringExtra("Password");
        final String userProfilePictureName = userPictureName;
        final String userProfilePictureURL = userPictureURL;



        Log.d(TAG, "email: "+emailUser+"pass: "+passwordUser);
        mAuth.createUserWithEmailAndPassword(emailUser, passwordUser).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    // Create a Users Object that will hold all information before sending to the database
                    Users user = new Users(
                            nameUser,
                            emailUser,
                            usernameUser,
                            passwordUser,
                            userAddress,
                            userOccupation,
                            userProfilePictureName,
                            userProfilePictureURL
                    );
                    Toast.makeText(RegisterFinalActivity.this, "USER CREATED",Toast.LENGTH_SHORT).show();

                    // This get an instance of the firebase database and uses this instance to post the object to the real time online database
                    FirebaseDatabase.getInstance().getReference("Users").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                            .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            if (task.isSuccessful()) {
                                // If successfull this will navigate to the get started page
                                Log.d(TAG, "profilePicture: "+userProfilePictureURL);
                                Toast.makeText(RegisterFinalActivity.this, "USER CREATED",Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(RegisterFinalActivity.this, GetStartedActivity.class));
                            } else {
                                // If Fail, it will display and error message as a toast

                                Toast.makeText(RegisterFinalActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();

                            }
                        }
                    });

                }else{
                    // If sign in fails, display a message to the user.

                    Log.w(TAG, "createUserWithEmail:failure", task.getException());
                    Toast.makeText(RegisterFinalActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(RegisterFinalActivity.this, HomeScreenActivity.class));
                }
            }
        });

    }
}
