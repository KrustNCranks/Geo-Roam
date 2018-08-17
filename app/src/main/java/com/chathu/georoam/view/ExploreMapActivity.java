package com.chathu.georoam.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.chathu.georoam.R;

public class ExploreMapActivity extends AppCompatActivity {
    private ImageView cancel;
    private Button myEvents;
    private Button myPictures;

    /**
     * this is the onCreate function for the Maps Activity, once the activity loads this sets up a fragment and starts loading
     * the map
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.explore_map_activity);

        // This code is for hiding the Action Bar in The Splash Screen Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Adding XML Variables to the Controller in order to manipulate them
        cancel = (ImageView) findViewById(R.id.cancel);
        myEvents = (Button) findViewById(R.id.myEventsButton);
        myPictures = (Button) findViewById(R.id.myPicturesButton);

        // OnClick event for the cancel Button takes us back to the Dashboard
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExploreMapActivity.this, DashboardActivity.class));
            }
        });

        // OnClick event to load my events
        myEvents.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExploreMapActivity.this, ExploreEventsMapActivity.class));
            }
        });

        // OnClick even to load myPictures
        myPictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(ExploreMapActivity.this, ExplorePicturesMapActivity.class));
            }
        });
    }
}
