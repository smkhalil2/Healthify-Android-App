package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

public class SettingsActivity extends AppCompatActivity {

    private ImageButton homeButton;
    private Button changePasswordButton, LogOutButton;
    private Button changeUsernameButton, deleteAccountButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        homeButton  = (ImageButton) findViewById(R.id.homeButton);
        changePasswordButton  = (Button) findViewById(R.id.changePasswordButton);
        changeUsernameButton  = (Button) findViewById(R.id.changeUsernameButton);
        LogOutButton = (Button) findViewById(R.id.LogOutButton);
        deleteAccountButton = (Button) findViewById(R.id.deleteAccountButton);
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

        changePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), change_password.class);
                startActivityForResult(myIntent, 0);
            }
        });

        changeUsernameButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), username_page.class);
                startActivityForResult(myIntent, 0);
            }
        });

        LogOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), LoginActivity.class);

                Session.username = "";

                startActivityForResult(myIntent, 0);
            }
        });

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), deleteUser.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }
}