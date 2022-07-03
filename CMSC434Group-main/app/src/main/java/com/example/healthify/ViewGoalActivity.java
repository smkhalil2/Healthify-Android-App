package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;

import java.util.ArrayList;

public class ViewGoalActivity extends AppCompatActivity {

    private ImageButton homeButton, backButton;
    private ImageButton snapchatButton, facebookButton, instagramButton;
    private TextView xAxisLabel, dateLabel, nameLabel, descriptionLabel;

    private LineChart goalLineChart;
    private LineData goalLineData;

    private BarChart goalBarChart;
    private BarData goalBarData;

    private Goal newGoal;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_goal);

        homeButton = (ImageButton) findViewById(R.id.homeButton);
        backButton = (ImageButton) findViewById(R.id.backButton);

        goalLineChart = (LineChart) findViewById(R.id.goalLineChart);
        goalBarChart = (BarChart) findViewById(R.id.goalBarChart);

        xAxisLabel = (TextView) findViewById(R.id.xAxis);
        dateLabel = (TextView) findViewById(R.id.dateLabel);
        nameLabel = (TextView) findViewById(R.id.nameLabel);
        descriptionLabel = (TextView) findViewById(R.id.descriptionLabel);

        snapchatButton = (ImageButton) findViewById(R.id.snapchatButton);
        facebookButton = (ImageButton) findViewById(R.id.facebookButton);
        instagramButton = (ImageButton) findViewById(R.id.instagramButton);

        goalLineChart.setVisibility(View.GONE);
        goalBarChart.setVisibility(View.GONE);
        xAxisLabel.setText("");


        Bundle extras = getIntent().getExtras();

        if (extras != null) {
            newGoal = new Goal(extras.getString("type"), extras.getString("chart"), extras.getString("goalInformation"), extras.getString("date"));

            descriptionLabel.setText(newGoal.getName());
            dateLabel.setText(newGoal.getDate());

            if (newGoal.getType().equals("bmi")) {

                nameLabel.setText("BMI Tracker");

                if (newGoal.getChart().equals("linechart")) {
                    initializeLineChart(getBMILineChartData());
                } else {
                    initializeBarChart(getBMIBarChartData());
                }

                xAxisLabel.setText("Day");

            } else if (newGoal.getType().equals("calories")) {

                nameLabel.setText("Calorie Tracker");

                if (newGoal.getChart().equals("linechart")) {
                    initializeLineChart(getCaloriesLineChartData());
                } else {
                    initializeBarChart(getCaloriesBarChartData());
                }

                xAxisLabel.setText("Day");

            } else {
                // Exercise

                nameLabel.setText("Exercise Tracker");

                if (newGoal.getChart().equals("linechart")) {
                    initializeLineChart(getExerciseLineChartData());
                } else {
                    initializeBarChart(getExerciseBarChartData());
                }

                xAxisLabel.setText("Day");
            }

        } else {

        }

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
                Intent myIntent = new Intent(v.getContext(), GoalsActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        snapchatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast= Toast.makeText(getApplicationContext(), "Shared to your Snapchat!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                toast.show();
            }
        });

        instagramButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast= Toast.makeText(getApplicationContext(), "Shared to your Instagram!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                toast.show();
            }
        });

        facebookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast= Toast.makeText(getApplicationContext(), "Shared to your Facebook!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                toast.show();
            }
        });

    }

    private void initializeLineChart(ArrayList<Entry> dataValues) {
        LineDataSet lineDataSet = new LineDataSet(dataValues, "");
        lineDataSet.setColor(Color.RED);
        lineDataSet.setValueTextColor(Color.WHITE);

        goalLineData = new LineData(lineDataSet);

        goalLineChart.setData(goalLineData);
        goalLineChart.getLegend().setTextColor(Color.WHITE);
        goalLineChart.getLegend().setEnabled(false);

        goalLineChart.setNoDataText("No goal data");
        goalLineChart.getDescription().setText("");

        goalLineChart.getXAxis().setTextColor(Color.WHITE);
        goalLineChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        goalLineChart.getAxisLeft().setTextColor(Color.WHITE);
        goalLineChart.getAxisRight().setEnabled(false);
        goalLineChart.setVisibility(View.VISIBLE);
        xAxisLabel.setText("Day");
    }

    private void initializeBarChart(ArrayList<BarEntry> dataValues) {
        BarDataSet barDataSet = new BarDataSet(dataValues, "");
        barDataSet.setColor(Color.RED);
        barDataSet.setValueTextColor(Color.WHITE);

        goalBarData = new BarData(barDataSet);

        goalBarChart.setData(goalBarData);

        goalBarChart.getLegend().setTextColor(Color.WHITE);
        goalBarChart.getLegend().setEnabled(false);

        goalBarChart.setNoDataText("No goal data");
        goalBarChart.getDescription().setText("");

        goalBarChart.getXAxis().setTextColor(Color.WHITE);
        goalBarChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        goalBarChart.getAxisLeft().setTextColor(Color.WHITE);
        goalBarChart.getAxisRight().setEnabled(false);

        goalBarChart.setVisibility(View.VISIBLE);
        xAxisLabel.setText("Day");
    }

    private ArrayList<Entry> getBMILineChartData() {
        ArrayList<Entry> dataValues = new ArrayList<Entry>();
        dataValues.add(new Entry(0, 27));
        dataValues.add(new Entry(1, 27));
        dataValues.add(new Entry(2, 26));
        dataValues.add(new Entry(3, 26));
        dataValues.add(new Entry(4, 25));
        dataValues.add(new Entry(5, 25));
        dataValues.add(new Entry(6, 24));

        return dataValues;
    }

    private ArrayList<Entry> getCaloriesLineChartData() {
        ArrayList<Entry> dataValues = new ArrayList<Entry>();
        dataValues.add(new Entry(0, 4000));
        dataValues.add(new Entry(1, 3920));
        dataValues.add(new Entry(2, 3840));
        dataValues.add(new Entry(3, 3505));
        dataValues.add(new Entry(4, 3102));
        dataValues.add(new Entry(5, 2905));
        dataValues.add(new Entry(6, 2850));

        return dataValues;
    }

    private ArrayList<BarEntry> getBMIBarChartData() {
        ArrayList<BarEntry> dataValues = new ArrayList<BarEntry>();
        dataValues.add(new BarEntry(0, 27));
        dataValues.add(new BarEntry(1, 27));
        dataValues.add(new BarEntry(2, 26));
        dataValues.add(new BarEntry(3, 26));
        dataValues.add(new BarEntry(4, 25));
        dataValues.add(new BarEntry(5, 25));
        dataValues.add(new BarEntry(6, 24));

        return dataValues;
    }

    private ArrayList<BarEntry> getCaloriesBarChartData() {
        ArrayList<BarEntry> dataValues = new ArrayList<BarEntry>();
        dataValues.add(new BarEntry(0, 4000));
        dataValues.add(new BarEntry(1, 3920));
        dataValues.add(new BarEntry(2, 3840));
        dataValues.add(new BarEntry(3, 3505));
        dataValues.add(new BarEntry(4, 3102));
        dataValues.add(new BarEntry(5, 2905));
        dataValues.add(new BarEntry(6, 2850));

        return dataValues;
    }

    private ArrayList<Entry> getExerciseLineChartData() {
        ArrayList<Entry> dataValues = new ArrayList<Entry>();
        dataValues.add(new Entry(0, 10));
        dataValues.add(new Entry(1, 15));
        dataValues.add(new Entry(2, 20));
        dataValues.add(new Entry(3, 20));
        dataValues.add(new Entry(4, 25));
        dataValues.add(new Entry(5, 30));
        dataValues.add(new Entry(6, 40));

        return dataValues;
    }

    private ArrayList<BarEntry> getExerciseBarChartData() {
        ArrayList<BarEntry> dataValues = new ArrayList<BarEntry>();
        dataValues.add(new BarEntry(0, 10));
        dataValues.add(new BarEntry(1, 15));
        dataValues.add(new BarEntry(2, 20));
        dataValues.add(new BarEntry(3, 20));
        dataValues.add(new BarEntry(4, 25));
        dataValues.add(new BarEntry(5, 30));
        dataValues.add(new BarEntry(6, 40));

        return dataValues;
    }
}