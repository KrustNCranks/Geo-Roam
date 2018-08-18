package com.chathu.georoam.view;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.chathu.georoam.R;
import com.chathu.georoam.controller.EventImageAdapter;
import com.chathu.georoam.controller.PictureImageAdapter;
import com.chathu.georoam.model.Pictures;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class MyPicturesActivity extends AppCompatActivity implements PictureImageAdapter.OnItemClickListener {

    private static final String TAG = "MY PICTURES";
    private RecyclerView mRecyclerView;
    private PictureImageAdapter mAdapter;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListen;
    private DatabaseReference myRef;
    private List<Pictures> mPictures;
    private String userID;
    private FirebaseStorage myStorage;

    private ChildEventListener mDBListener;

    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_pictures_recycler_view);

        // Adding XML Variables to the Controller in order to manipulate them
        mRecyclerView = findViewById(R.id.recycler_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mPictures = new ArrayList<>();
        mAdapter = new PictureImageAdapter(MyPicturesActivity.this,mPictures);
        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(MyPicturesActivity.this);

        // Access the Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        // Setting the Firebase References
        myRef = FirebaseDatabase.getInstance().getReference("Picture_Post");

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

        // This gets the Firebase Storage reference
        myStorage = FirebaseStorage.getInstance();

        // This gets the Firebase Database reference
        myRef = FirebaseDatabase.getInstance().getReference("Picture_Post").child(userID);


        mDBListener= myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Pictures pictures = dataSnapshot.getValue(Pictures.class);
                pictures.setKey(dataSnapshot.getKey());
                mPictures.add(pictures);
                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
                /*Pictures pictures = dataSnapshot.getValue(Pictures.class);
                pictures.setKey(dataSnapshot.getKey());
                mPictures.add(pictures);
                mAdapter = new PictureImageAdapter(MyPicturesActivity.this,mPictures);
                mRecyclerView.setAdapter(mAdapter);
                mAdapter.setOnItemClickListener(MyPicturesActivity.this);
                mAdapter.notifyDataSetChanged(); */
            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    @Override
    public void onViewImageClick(int position) {
        Toast.makeText(this, "Normal click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void OnItemClick(int position) {
        Toast.makeText(this, "Whatever click at position: " + position, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onDeleteImageClick(int position) {
        Pictures selectedItem = mPictures.get(position);
        final String selectedKey = selectedItem.getKey();

        StorageReference imageRef = myStorage.getReferenceFromUrl(selectedItem.getPictureURL());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                myRef.child(selectedKey).removeValue();
                Toast.makeText(this, "Item Deleted" + , Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        myRef.removeEventListener(mDBListener);
    }
}
