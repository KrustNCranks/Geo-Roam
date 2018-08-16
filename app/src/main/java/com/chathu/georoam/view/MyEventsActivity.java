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
import com.chathu.georoam.model.EventsModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MyEventsActivity extends AppCompatActivity {

    private static final String TAG = "MY EVENTS";
    private RecyclerView mRecyclerView;
    private EventImageAdapter mAdapter;

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener authListen;
    private DatabaseReference myRef;
    private List<EventsModel> mEvents;
    private String userID;

    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_events_recycler_view);

        // Adding XML Variables to the Controller in order to manipulate them
        mRecyclerView = findViewById(R.id.recycler_view_events);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        mEvents = new ArrayList<>();


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

        myRef = FirebaseDatabase.getInstance().getReference("Event_Post").child(userID);

        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                EventsModel eventsModel = dataSnapshot.getValue(EventsModel.class);
                mEvents.add(eventsModel);
                mAdapter = new EventImageAdapter(MyEventsActivity.this,mEvents);
                mRecyclerView.setAdapter(mAdapter);
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
        /*myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EventsModel events = dataSnapshot.getValue(EventsModel.class);
                mEvents.add(events);

                mAdapter = new EventImageAdapter(MyEventsActivity.this,mEvents);
                mRecyclerView.setAdapter(mAdapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(MyEventsActivity.this,databaseError.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }); */
    }
}
