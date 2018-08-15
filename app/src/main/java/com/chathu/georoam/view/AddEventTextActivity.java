package com.chathu.georoam.view;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.chathu.georoam.R;

public class AddEventTextActivity extends AppCompatActivity {

    private Button next;
    private ImageView back;
    private EditText eventName;
    private EditText eventDescription;
    private EditText eventStartDate;
    private EditText eventEndDate;
    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_event_text_activity);

        // This code is for hiding the Action Bar in The Splash Screen Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Adding XML Variables to the Controller in order to manipulate them
        next = (Button) findViewById(R.id.nextButton);
        back = (ImageView) findViewById(R.id.backButton);
        eventName = (EditText) findViewById(R.id.eventName);
        eventDescription = (EditText) findViewById(R.id.eventDescription);
        eventStartDate = (EditText) findViewById(R.id.eventStartDate);
        eventEndDate = (EditText) findViewById(R.id.eventEndDate);

        // onClick Event for the next Button
        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                next();
            }
        });

        // onClick Event to go back to the Post Activity
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                back();
            }
        });
    }

    /**
     * This will take all the text from the edit text fields and will send it to the next activity before uploading to the
     * database
     */
    private void next(){
        Intent intent = new Intent( AddEventTextActivity.this, AddEventMapActivity.class );
        intent.putExtra ( "EventName", eventName.getText().toString().trim() );
        intent.putExtra ( "EventDescription", eventDescription.getText().toString().trim() );
        intent.putExtra("EventStartDate",eventStartDate.getText().toString().trim());
        intent.putExtra("EventEndDate",eventEndDate.getText().toString().trim());
        startActivity(intent);
    }

    /**
     * This method will take us back to the Post Activity
     */
    private void back(){
        startActivity(new Intent(AddEventTextActivity.this, AddPostActivity.class));
    }
}
