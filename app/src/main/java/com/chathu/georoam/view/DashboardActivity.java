package com.chathu.georoam.view;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.View;
import android.content.Intent;
import android.support.v7.app.ActionBar;

import com.chathu.georoam.R;

public class DashboardActivity extends AppCompatActivity implements View.OnClickListener {

    private CardView map,profile,explore,myPosts,addPost;
    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard_activity);


        // This code is for hiding the Action Bar in The Splash Screen Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Adding XML Variables to the Controller in order to manipulate them
        map = (CardView) findViewById(R.id.mapCard);
        profile = (CardView) findViewById(R.id.profileCard);
        explore = (CardView) findViewById(R.id.exploreCard);
        myPosts = (CardView) findViewById(R.id.myPostsCard);
        addPost = (CardView) findViewById(R.id.addPostCard);

        //Adding the OnClickListeners to the Cards
        map.setOnClickListener(this);
        profile.setOnClickListener(this);
        explore.setOnClickListener(this);
        myPosts.setOnClickListener(this);
        addPost.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        Intent i;

        switch (v.getId()){
            case R.id.mapCard : i = new Intent(this,SearchMapActivity.class);startActivity(i); break;
            case R.id.profileCard : i = new Intent(this,ProfileActivity.class);startActivity(i); break;
            case R.id.exploreCard : i = new Intent(this, ExploreMapActivity.class);startActivity(i);break;
            case R.id.myPostsCard: i = new Intent(this, MyPostsActivity.class);startActivity(i); break;
            case R.id.addPostCard : i = new Intent(this,AddPostActivity.class);startActivity(i); break;
            default: break;
        }
    }
}
