package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class change_password extends AppCompatActivity {

    private ImageButton homeButton;
    private Button changePasswordButton;
    private Button cancelChangePasswordButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);
        homeButton = (ImageButton) findViewById(R.id.homeButton);

        changePasswordButton = (Button) findViewById(R.id.changePasswordButton);
        cancelChangePasswordButton = (Button) findViewById(R.id.cancelChangePasswordButton);
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
                Intent myIntent = new Intent(v.getContext(), SettingsActivity.class);
                startActivityForResult(myIntent, 0);
                Toast.makeText(getApplicationContext(), "Password Updated", Toast.LENGTH_SHORT).show();

            }
        });


        cancelChangePasswordButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), SettingsActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }
}