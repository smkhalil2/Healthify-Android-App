package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

public class NutritionDatabaseActivity extends AppCompatActivity {

    private Context context;

    private ImageButton backButton, homeButton, searchButton;
    private AutoCompleteTextView searchEntry;

    private TextView foundLabel, foodLabel;

    private ArrayAdapter<String> databaseListAdapter;
    private ArrayList<String> databaseListItems;

    private PieChart nutritionPieChart;
    private PieData nutritionalData;

    private ArrayList<String> foods;
    private HashMap<String, String[]> xValues;
    private HashMap<String, float[]> yValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nutrition_database);

        context = getApplicationContext();
        backButton = (ImageButton) findViewById(R.id.backButton);
        homeButton  = (ImageButton) findViewById(R.id.homeButton);
        searchButton  = (ImageButton) findViewById(R.id.searchButton);
        nutritionPieChart = (PieChart) findViewById(R.id.nutritionPieChart);
        searchEntry = (AutoCompleteTextView) findViewById(R.id.searchEntry);
        foundLabel = (TextView) findViewById(R.id.foundLabel);
        foodLabel = (TextView) findViewById(R.id.foodLabel);

        databaseListItems = new ArrayList<String>();
        foods = new ArrayList<String>();
        xValues = new HashMap<String, String[]>();
        yValues = new HashMap<String, float[]>();

        databaseListAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, databaseListItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                return view;
            }
        };

        searchEntry.setAdapter(databaseListAdapter);

        nutritionPieChart.setVisibility(View.GONE);
        foundLabel.setVisibility(View.GONE);
        foodLabel.setVisibility(View.GONE);

        initializeDatabase();

        databaseListAdapter.notifyDataSetChanged();

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

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), NutritionHubActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onClick(View v) {
                String foodName = searchEntry.getText().toString();

                if (foods.contains(foodName)) {
                    // Food found

                    foundLabel.setVisibility(View.VISIBLE);
                    foodLabel.setVisibility(View.VISIBLE);

                    foundLabel.setText(foodName + " found!");
                    foodLabel.setText("Nutritional Facts of " + foodName);

                    updatePieChart(foodName, xValues.get(foodName), yValues.get(foodName));

                    nutritionPieChart.setVisibility(View.VISIBLE);
                } else {
                    // Food not found
                    foundLabel.setVisibility(View.VISIBLE);
                    foundLabel.setText(foodName + " not found!");

                    foodLabel.setVisibility(View.GONE);
                    nutritionPieChart.setVisibility(View.GONE);

                }

            }
        });
    }

    private void updatePieChart(String foodName, String[] xData, float[] yData) {
        ArrayList<PieEntry> entries = new ArrayList<PieEntry>();

        if (foods.contains(foodName)) {
            // Food found in database

            for (int i = 0; i < xData.length; i++) {
                entries.add(new PieEntry(yData[i], xData[i]));
            }

            PieDataSet set = new PieDataSet(entries, "(grams)");
            set.setColors(ColorTemplate.MATERIAL_COLORS);
            nutritionalData = new PieData(set);

            nutritionPieChart.setRotationEnabled(true);
            nutritionPieChart.setData(nutritionalData);
            nutritionPieChart.setNoDataText("Food item not found in database.");
            nutritionPieChart.getDescription().setText("");
            nutritionPieChart.getLegend().setTextColor(Color.WHITE);

        } else {

            // Food not found in database

        }


    }

    private void initializeDatabase() {

        String fileName = "database.txt";

        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(getAssets().open(fileName)));

            String line;
            while ((line = br.readLine()) != null) {

                try {
                    JSONObject food = new JSONObject(line);
                    String foodName = food.getString("name");
                    JSONArray xDataJSON = new JSONArray(food.getString("xData"));
                    JSONArray yDataJSON = new JSONArray(food.getString("yData"));

                    String[] xData =  new String[xDataJSON.length()];
                    float[] yData = new float[yDataJSON.length()];

                    for (int i = 0; i < xDataJSON.length(); i++) {
                        xData[i] = xDataJSON.getString(i);
                        yData[i] = (float) yDataJSON.getDouble(i);
                    }

                    databaseListItems.add(foodName);
                    foods.add(foodName);
                    xValues.put(foodName, xData);
                    yValues.put(foodName, yData);

                } catch (JSONException e) {
                    e.printStackTrace();

                }
            }

            br.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Helper function to get data, use top 4 nutritional facts
     * @param name
     * @param xData
     * @param yData
     */
    private void printJSON(String name, String[] xData, float[] yData) {
        try {

            JSONObject food = new JSONObject();

            JSONArray xDataJSON = new JSONArray();
            JSONArray yDataJSON = new JSONArray();

            for (String x : xData) {
                xDataJSON.put(x);
            }

            for (float y : yData) {
                yDataJSON.put(y);
            }

            food.put("name", name);
            food.put("xData", xDataJSON);
            food.put("yData", yDataJSON);

            System.out.println(food.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}