package com.chathu.georoam.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.graphics.Bitmap;
import android.view.View;
import android.util.Log;

import com.chathu.georoam.R;
import com.chathu.georoam.model.Users;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Map;


public class ProfileActivity extends AppCompatActivity {

    private static final String TAG = "PROFILE";
    private ImageView logout;
    private FirebaseAuth mAuth;
    private FirebaseDatabase db;
    private FirebaseAuth.AuthStateListener authListen;
    private DatabaseReference myRef;
    private String userID;

    private TextView nameText;
    private TextView emailText;
    private TextView usernameText;
    private TextView addressText;
    private TextView occupationText;
    private ImageView profilePicture;

    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_activity);

        // This code is for hiding the Action Bar in The Splash Screen Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Access the Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        // Get userID
        FirebaseUser user = mAuth.getCurrentUser();
        userID = user.getUid();

        // Initiliaze Database
        db = FirebaseDatabase.getInstance();
        myRef = db.getReference().child("Users").child(userID);

        // Adding XML Variables to the Controller in order to manipulate them
        logout = (ImageView)findViewById(R.id.logoutButton);
        nameText = (TextView) findViewById(R.id.nameTxt);
        emailText = (TextView) findViewById(R.id.emailTxt);
        usernameText = (TextView) findViewById(R.id.usernameTxt);
        addressText = (TextView) findViewById(R.id.addressTxt);
        occupationText = (TextView) findViewById(R.id.occupationTxt);
        profilePicture = (ImageView) findViewById(R.id.profilePictureImage);

        // This is the Logout Functionality
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
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


        /**
         *
         * This is the code that is used to update the data on the UI
         */
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Users user = dataSnapshot.getValue(Users.class);
                showData(user);
                System.out.println(user);
                // Map<String,Object> map = (Map<String, Object>)dataSnapshot.getValue();
                //String name = dataSnapshot.getValue(String.class);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                throw databaseError.toException();
            }
        });
    }

    /**
     *
     * This method fetches data from the database dynamically depending on the logged in user
     * using the getters from the models directory and updates the UI
     *
     */
    private void showData(Users users) {

        // Sets the text fields with the information
        nameText.setText(users.getPersonName().toUpperCase());
        usernameText.setText(users.getUsername());
        emailText.setText(users.getEmail());
        addressText.setText(users.getAddress());
        occupationText.setText(users.getOccupation());

        // This resizes and fits into the Profile Picture ImageView
        Picasso.get().load(users.getProfilePictureURL()).resize(400,357).centerCrop().into(profilePicture);
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
}
