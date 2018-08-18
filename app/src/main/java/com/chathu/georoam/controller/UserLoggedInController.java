package com.chathu.georoam.controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;

import com.chathu.georoam.view.DashboardActivity;
import com.chathu.georoam.view.HomeScreenActivity;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import static android.support.constraint.Constraints.TAG;

public class UserLoggedInController {

    private static UserLoggedInController myInstance = new UserLoggedInController();

    public static UserLoggedInController getInstance(){
        if(myInstance == null){
            return new UserLoggedInController();
        }
        return myInstance;
    }

    /**
     * This will check if a user is logged in, if the user is logged in, it will open up directly to the dashboard page
     * @param page
     */
    public void isLoggedIn(Activity page){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            // User is signed in
            Intent i = new Intent(page, DashboardActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            page.startActivity(i);
        } else {
            // User is signed out
            Log.d(TAG, "onAuthStateChanged:signed_out");
        }
    }
}
