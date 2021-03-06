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
import android.widget.Switch;
import android.widget.Toast;

import com.chathu.georoam.R;
import com.chathu.georoam.controller.ValidationController;

public class AddPictureTextActivity extends AppCompatActivity {

    private EditText name;
    private EditText description;
    private Button next;
    private ImageView back;
    private Switch privateSwitch;
    private String isPrivate = null;
    private ValidationController validator = ValidationController.getInstance();

    /**
     * This is the onCreate , when the activity runs, all the code runs in this
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_picture_text_activity);

        // This code is for hiding the Action Bar in The Splash Screen Activity
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        // Adding XML Variables to the Controller in order to manipulate them
        next = (Button) findViewById(R.id.nextButton);
        back = (ImageView) findViewById(R.id.backButton);
        name = (EditText) findViewById(R.id.pictureName);
        description = (EditText) findViewById(R.id.pictureDescription);
        privateSwitch = (Switch)findViewById(R.id.switch2);

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
        if(!validator.isEmpty(name.getText().toString()))
        {
            if (!validator.isEmpty(description.getText().toString())){
                if(privateSwitch.isChecked()){
                    isPrivate = "private";
                }
                else{
                    isPrivate = "public";
                }
                Intent intent = new Intent ( AddPictureTextActivity.this, AddPictureMapActivity.class );
                intent.putExtra ( "PictureName", name.getText().toString().trim() );
                intent.putExtra ( "PictureDescription", description.getText().toString().trim() );
                intent.putExtra("Status",isPrivate);
                startActivity(intent);
            }else {
                Toast.makeText(AddPictureTextActivity.this,"Please Describe Your Image",Toast.LENGTH_SHORT).show();
            }

        }else {
            Toast.makeText(AddPictureTextActivity.this,"Please Name Your Image",Toast.LENGTH_SHORT).show();
        }

    }

    /**
     * This method will take us back to the Post Activity
     */
    private void back(){
        startActivity(new Intent(AddPictureTextActivity.this, AddPostActivity.class));
    }
}
