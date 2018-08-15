package com.chathu.georoam.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.chathu.georoam.R;

public class EventPostedActivity extends AppCompatActivity {
    private Button dashboard;
    private Button myPosts;
    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.event_posted_activity);

        // This code is for hiding the Action Bar in The Splash Screen Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Adding XML Variables to the Controller in order to manipulate them
        dashboard = (Button) findViewById(R.id.dashboardButton);
        myPosts = (Button) findViewById(R.id.myPostsButton);

        // Sends the user back to the dashboard after Posting an image
        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(EventPostedActivity.this, DashboardActivity.class));
            }
        });
    }
}
