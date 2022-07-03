package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    // Instance variables to store references to buttons
    private ImageButton settingsButton;
    private Button fitnessButton, goalsButton, nutritionButton;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Define binds to buttons by id
        fitnessButton = (Button) findViewById(R.id.fitnessButton);
        goalsButton = (Button) findViewById(R.id.goalsButton);
        nutritionButton = (Button) findViewById(R.id.nutritionButton);
        settingsButton = (ImageButton) findViewById(R.id.settingsButton);

        initializeOnClickListeners();
    }

    /**
     * Auxiliary method to initialize on click listeners of buttons in the MainActivity
     */
    private void initializeOnClickListeners() {

        fitnessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FitnessActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        goalsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), GoalsActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        nutritionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent myIntent = new Intent(v.getContext(), NutritionHubActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), SettingsActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    @Override
    public void onBackPressed() {}
}