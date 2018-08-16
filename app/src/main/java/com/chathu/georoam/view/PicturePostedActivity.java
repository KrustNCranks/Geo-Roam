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

public class PicturePostedActivity extends AppCompatActivity {

    private Button dashboard;
    private Button myPosts;
    private Button addAgain;
    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_posted_activity);

        // This code is for hiding the Action Bar in The Splash Screen Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Adding XML Variables to the Controller in order to manipulate them
        dashboard = (Button) findViewById(R.id.dashboardButton);
        myPosts = (Button) findViewById(R.id.myPostsButton);
        addAgain = (Button) findViewById(R.id.addAgainPictureButton);

        // Sends the user back to the dashboard after Posting an image
        dashboard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PicturePostedActivity.this, DashboardActivity.class));
            }
        });

        // Sends the user to the My Posts Activity
        myPosts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PicturePostedActivity.this, MyPostsActivity.class));
            }
        });

        // Sends the User back to add another Event if needed
        addAgain.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(PicturePostedActivity.this,AddEventTextActivity.class));
            }
        });
    }
}
