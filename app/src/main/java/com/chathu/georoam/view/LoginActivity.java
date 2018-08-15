package com.chathu.georoam.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.chathu.georoam.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity{

    private static final String TAG = "LOGIN User";
    private ImageView back;
    private EditText email;
    private EditText password;
    private TextView register;
    private Button loginButton;
    private ProgressBar progressBar;

    // Used for Firebase Authentication
    private FirebaseAuth mAuth;


    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        // This code is for hiding the Action Bar in The Splash Screen Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Adding XML Variables to the Controller in order to manipulate them
        email = (EditText) findViewById(R.id.emailTxt);
        password = (EditText) findViewById(R.id.passTxt);
        loginButton = (Button) findViewById(R.id.loginButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        // Hide The Progress Par onCreate
        progressBar.setVisibility(View.GONE);

        // Access the Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        // Calling the Function to go Back once the X button is clicked
        back();

        // Open up the Register Page
        register();

        // Logs the user in and opens up the dashboard , the username and password and taken and parsed as input parameters to the function
        // below
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login(email.getText().toString(), password.getText().toString());
            }
        });
    }


    /**
     * This onStart method checks to see if the user is signed in or not, basically checks for a non null value
     */
    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
    }


    /**
     * This onClick Listener takes us back to the Splash Screen
     */
    public void back(){
        // Adding XML Variables to the Controller in order to manipulate them
        back = (ImageView)findViewById(R.id.backButton);

        // The onClick Listener Code
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, HomeScreenActivity.class));
            }
        });
    }

    /**
     * This onClick Listener will take us to the Register Page
     */
    public void register(){
        // Adding XML Variables to the Controller in order to manipulate them
        register = (TextView)findViewById(R.id.register);

        // The onClick Listener Code
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });
    }


    /**
     * This will check for user authentication and uses an onClick listener to the Dashboard page, it takes input parameters
     * from the ones sent above
     */
    public void login(String email, String password){
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        progressBar.setVisibility(View.GONE);
                        if (task.isSuccessful()) {
                            // Sign in success, navigates to the dashboard
                            Log.d(TAG, "signInWithEmail:success");
                            startActivity(new Intent(LoginActivity.this, DashboardActivity.class));
                        } else {
                            progressBar.setVisibility(View.GONE);
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toast.makeText(LoginActivity.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                });


    }
}
