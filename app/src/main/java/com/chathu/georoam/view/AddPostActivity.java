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

public class AddPostActivity extends AppCompatActivity {

    private Button eventsButton;
    private Button picturesButton;
    private ImageView backButton;


    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_post_activity);

        // This code is for hiding the Action Bar in The Splash Screen Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Adding XML Variables to the Controller in order to manipulate them
        eventsButton = (Button) findViewById(R.id.eventButton);
        picturesButton = (Button) findViewById(R.id.pictureButton);
        backButton = (ImageView) findViewById(R.id.backButton);

        // OnClick Listener for the Back Button
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });

        // OnClick Listener for the Event Button
        eventsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                events();
            }
        });

        // OnClick Listener for the Pictures Button
        picturesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pictures();
            }
        });
    }

    /**
     * This method will take us back to the Dashboard
     */
    private void back(){
        startActivity(new Intent(AddPostActivity.this, DashboardActivity.class));
    }

    /**
     * This method will take us to the Post EventsModel Activity
     */
    private void events(){
        startActivity(new Intent(AddPostActivity.this, AddEventTextActivity.class));
    }

    /**
     * This method will take us to the Post Pictures Activity
     */
    private void pictures(){
        startActivity(new Intent(AddPostActivity.this, AddPictureTextActivity.class));
    }
}
