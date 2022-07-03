package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static java.lang.Integer.parseInt;

public class NutritionTrackerAddFoodActivity extends AppCompatActivity {

    private ImageButton homeButton;
    private ImageButton confirmLogFoodButton;
    private ImageButton cancelLogFoodButton;
    private Spinner spnMeal;
   // private TextView txtMeal;
    private DatePicker datePicker;

    private EditText eFoodName, eCalories;


   // private Food newFood;

    private String foodName, calories, mealTime, date = "";
   // private int calories;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_tracker_add_food);

        eFoodName = (EditText) findViewById(R.id.foodNameEntry);
        eCalories = (EditText) findViewById(R.id.calorieEntry);

        homeButton  = (ImageButton) findViewById(R.id.homeButton);
        confirmLogFoodButton = (ImageButton) findViewById(R.id.confirmLogFoodButton);
        cancelLogFoodButton = (ImageButton) findViewById(R.id.cancelLogFoodButton);

        datePicker = (DatePicker) findViewById(R.id.datePicker);


        spnMeal = (Spinner) findViewById(R.id.mealType);
        List<String> meals = new ArrayList<>();
        meals.add("-Select Mealtime-");
        meals.add("Breakfast");
        meals.add("Lunch");
        meals.add("Dinner");
        meals.add("Dessert");
        meals.add("Snack");
        ArrayAdapter<String> mealAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, meals);
        mealAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnMeal.setAdapter(mealAdapter);


        spnMeal.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedMeal = spnMeal.getSelectedItem().toString();
                if (!selectedMeal.equals("- Select Meal -")) {
                   // newFood.setMealtime(selectedMeal);
                    mealTime = selectedMeal;
                    //                    txtMeal.setText(spnMeal.getSelectedItem().toString());
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

        confirmLogFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                DateFormat format = new SimpleDateFormat("M/dd/yyyy", Locale.ENGLISH);
//                try {
//                    Date formattedDate = format.parse(date);

                if (eFoodName.getText().toString().isEmpty() || eFoodName.getText().toString() == null
                        || eFoodName.getText().toString() == "" || parseInt(eCalories.getText().toString()) <= 0
                        || spnMeal.getSelectedItem().toString().equals("- Select Mealtime -")) {
                        Toast.makeText(getApplicationContext(), "Enter all fields!", Toast.LENGTH_SHORT).show();
                } else {

                    foodName = eFoodName.getText().toString();
                    calories = eCalories.getText().toString();
                    date = datePicker.getMonth() + "/" + datePicker.getDayOfMonth() + "/" + datePicker.getYear();
                        // mealTime = spnMeal.getSelectedItem().toString();
                        //System.out.println("meal " + mealTime);


//                    Bundle bundle = new Bundle();
//                    bundle.putString("foodName", foodName);
//                    bundle.putString("calories", calories);
//                    bundle.putString("date", date);
//                    bundle.putString("mealTime", mealTime);


                        //Toast.makeText(getApplicationContext(), mealTime, Toast.LENGTH_SHORT).show();

                    Intent myIntent = new Intent(v.getContext(), NutritionHubActivity.class);
                    myIntent.putExtra("foodName", foodName);
                    myIntent.putExtra("mealTime", mealTime);
                    myIntent.putExtra("date", date);
                    myIntent.putExtra("calories", calories);

                    startActivityForResult(myIntent, 0);
                        // myIntent.putExtras(bundle);
                        // Intent myIntent = new Intent(v.getContext(), NutritionHubActivity.class);// for now
                        //startActivityForResult(myIntent, 0);
                }

//                } catch (ParseException e) {
//                    e.printStackTrace();
//                }

            }

        });

        cancelLogFoodButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), NutritionHubActivity.class);// for now
                startActivityForResult(myIntent, 0);
            }
        });



    }

}
