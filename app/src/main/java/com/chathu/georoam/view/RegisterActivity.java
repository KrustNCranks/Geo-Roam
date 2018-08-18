package com.chathu.georoam.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.chathu.georoam.R;
import com.chathu.georoam.controller.ValidationController;
import com.google.firebase.auth.FirebaseAuth;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "Register Users";
    private EditText name;
    private EditText email;
    private EditText username;
    private EditText password;
    private EditText password_confirm;
    private Button register;
    private ImageView back;
    private ProgressBar progressBar;


    // Used for Firebase Authentication
    private FirebaseAuth mAuth;

    // Create a new instance of the Validation controller
    private ValidationController validator = ValidationController.getInstance();

    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register_activity);

        // This code is for hiding the Action Bar in The Splash Screen Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Adding XML Variables to the Controller in order to manipulate them
        name = (EditText) findViewById(R.id.nameTxt);
        email = (EditText) findViewById(R.id.emailTxt);
        username = (EditText) findViewById(R.id.userTxt);
        password = (EditText) findViewById(R.id.passTxt);
        password_confirm = (EditText) findViewById(R.id.passTxt2);
        register = (Button) findViewById(R.id.registerButton);
        back = (ImageView) findViewById(R.id.backButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        // Hide The Progress Par onCreate
        progressBar.setVisibility(View.GONE);

        // Access the Firebase Auth instance
        mAuth = FirebaseAuth.getInstance();

        // This is the register button, when you press the register button, a new user will be created.
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createAccount();
            }
        });

        // This is the back button, when you press the big X, you will be sent back to the home screen
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(RegisterActivity.this, HomeScreenActivity.class));
            }
        });
    }

    /**
     * This is the createAccount method, this is where all the magic happens when creeating an account
     * since firebase allows user creation with only Email and Password, we create a new Users object using the Users model in the models
     * directory and then pass it along as a object to the RealTime Database, note: DO NOT USE FIRESTORE UNTIL IT COMES OUT OF BETA
     */
    private void createAccount(){
        if(!validator.isEmpty(name.getText().toString())){
            if((!validator.isEmpty(email.getText().toString())) && (validator.isEmail(email.getText().toString()))){
                if(!validator.isEmpty(username.getText().toString())){
                    if(!validator.isEmpty(password.getText().toString())){
                        if(validator.confirm(password.getText().toString(),password_confirm.getText().toString())){
                            Intent intent = new Intent ( RegisterActivity.this, RegisterFinalActivity.class );
                            intent.putExtra ( "Name", name.getText().toString().trim() );
                            intent.putExtra ( "Email", email.getText().toString().trim() );
                            intent.putExtra ( "Username", username.getText().toString().trim() );
                            intent.putExtra ( "Password", password.getText().toString().trim() );
                            startActivity(intent);
                        }else{
                            Toast.makeText(RegisterActivity.this,"Passwords do no match",Toast.LENGTH_SHORT).show();
                        }
                    }else{
                        Toast.makeText(RegisterActivity.this,"Please Enter a PASSWORD",Toast.LENGTH_SHORT).show();
                    }

                }else{
                    Toast.makeText(RegisterActivity.this,"Please Enter a USERNAME",Toast.LENGTH_SHORT).show();
                }

            }else{
                Toast.makeText(RegisterActivity.this,"Please Enter a Valid EMAIL",Toast.LENGTH_SHORT).show();
            }

        }else
        {
            Toast.makeText(RegisterActivity.this,"Name Field Cannot Be Empty",Toast.LENGTH_SHORT).show();
        }


        Log.d(TAG,  "createAccount(): name: "+name.getText().toString());
        Log.d(TAG, "email: "+email.getText().toString());
        Log.d(TAG, "username: "+username.getText().toString());
        Log.d(TAG,"password: "+password.getText().toString());

    }
}
