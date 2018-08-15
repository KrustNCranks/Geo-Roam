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

public class MyPostsActivity extends AppCompatActivity {
    private Button events;
    private Button pictures;
    private ImageView back;


    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_posts_activity);


        // This code is for hiding the Action Bar in The Splash Screen Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Adding XML Variables to the Controller in order to manipulate them
        events = (Button)findViewById(R.id.eventsButton);
        pictures = (Button)findViewById(R.id.picturesButton);
        back = (ImageView)findViewById(R.id.backButton);

        // onClick event to go back to the dashboard
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MyPostsActivity.this,DashboardActivity.class));
            }
        });

        // onClick event to go to the events activity
        events.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // onClick event to go to the pictures activity
        pictures.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //startActivity(new Intent(MyPostsActivity.this,MyPicturesActivityController.class));
            }
        });
    }
}
