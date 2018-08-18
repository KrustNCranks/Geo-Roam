package com.chathu.georoam.view;

import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.chathu.georoam.R;
import com.chathu.georoam.controller.UserLoggedInController;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeScreenActivity extends AppCompatActivity {

    private static final String TAG = "HomeScreenActivity";
    private Button login;
    private Button register;
    private UserLoggedInController isUserLoggedIn = UserLoggedInController.getInstance();
    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen_activity);

        // This code is for hiding the Action Bar in The Splash Screen Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();


        // Adding XML Variables to the Controller in order to manipulate them
        login = (Button)findViewById(R.id.loginButton);
        register = (Button)findViewById(R.id.registerButton);


        // This is a button click event navigating from the splash screen to the login screen
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, LoginActivity.class));
            }
        });

        // This is a button click event navigating from the splash screen to the Register screen
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HomeScreenActivity.this, RegisterActivity.class));
            }
        });


        // This will see if there are any users logged in, if there is, it will open up the Dasboard page directly
        isUserLoggedIn.isLoggedIn(HomeScreenActivity.this);
    }
}
