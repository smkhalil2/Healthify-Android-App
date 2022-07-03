package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

public class username_page extends AppCompatActivity {

    private ImageButton homeButton;
    private Button changeUsernameButtonButton, changeUserCancelButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_username_page);

        homeButton  = (ImageButton) findViewById(R.id.homeButton);

        changeUsernameButtonButton = (Button) findViewById(R.id.changeUsernameButtonButton);

        changeUserCancelButton = (Button) findViewById(R.id. changeUserCancelButton);

        initializeOnClickListeners();
    }

    private void initializeOnClickListeners() {

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });


        //Make pop maybe or show that username was succesfully changed
        changeUsernameButtonButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), SettingsActivity.class);
                startActivityForResult(myIntent, 0);
                Toast.makeText(getApplicationContext(), "Username Updated", Toast.LENGTH_SHORT).show();

            }
        });

        changeUserCancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), SettingsActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }
}
