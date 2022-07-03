package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/*where goals will be written and sent*/
public class goal_listpage extends AppCompatActivity {

    private ImageButton homeButton, imageButton,imageButton2;
    private Button linechartButton, barchartButton;
    private EditText goalEntry;
    private DatePicker goalDatePicker;
    private Button bmiButton, caloriesButton, exerciseButton;
    private boolean bmiButtonPressed, caloriesButtonPressed, exerciseButtonPressed;
    private boolean linechartButtonPressed, barchartButtonPressed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goal_listpage);

        homeButton = (ImageButton) findViewById(R.id.homeButton);
        imageButton = (ImageButton) findViewById(R.id.imageButton);
        imageButton2 = (ImageButton) findViewById(R.id.imageButton2);

        linechartButton = (Button) findViewById(R.id.linechartButton);
        barchartButton = (Button) findViewById(R.id.barchartButton);

        goalEntry = (EditText) findViewById(R.id.goalEntry);
        goalDatePicker = (DatePicker) findViewById(R.id.goalDatePicker);

        bmiButton = (Button) findViewById(R.id.bmiButton);
        caloriesButton = (Button) findViewById(R.id.caloriesButton);
        exerciseButton = (Button) findViewById(R.id.exerciseButton);

        bmiButtonPressed = false;
        caloriesButtonPressed = false;
        exerciseButtonPressed = false;

        linechartButtonPressed = false;
        barchartButtonPressed = false;

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

        imageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type, chart, goalInformation, date;

                goalInformation = goalEntry.getText().toString();

                // Check if description empty
                if (!goalInformation.equals("")) {

                    // Check if goal type selected

                    if (bmiButtonPressed || caloriesButtonPressed || exerciseButtonPressed) {

                        if (bmiButtonPressed) {
                            type = "bmi";
                        } else if (caloriesButtonPressed) {
                            type = "calories";
                        } else {
                            type = "exercise";
                        }

                        // Check if chart type selected
                        if (linechartButtonPressed || barchartButtonPressed) {

                            if (linechartButtonPressed) {
                                chart = "linechart";
                            } else {
                                chart = "barchart";
                            }

                            String month = String.valueOf(goalDatePicker.getMonth() + 1);
                            String day = String.valueOf(goalDatePicker.getDayOfMonth());
                            String year = String.valueOf(goalDatePicker.getYear());

                            date = month + "/" + day + "/" + year;

                            try {
                                DateFormat format = new SimpleDateFormat("M/dd/yyyy", Locale.ENGLISH);
                                Date formattedDate = format.parse(date);

                                if (formattedDate.after(new Date())) {

                                    Intent myIntent = new Intent(v.getContext(), GoalsActivity.class);
                                    myIntent.putExtra("type", type);
                                    myIntent.putExtra("chart", chart);
                                    myIntent.putExtra("goalInformation", goalInformation );
                                    myIntent.putExtra("date", date);
                                    startActivityForResult(myIntent, 0);

                                } else {

                                    Toast toast= Toast.makeText(getApplicationContext(), "Please enter a date in the future", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                                    toast.show();

                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast toast= Toast.makeText(getApplicationContext(), "Please enter a chart type", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                            toast.show();
                        }

                    } else {
                        Toast toast= Toast.makeText(getApplicationContext(), "Please enter a goal type", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                        toast.show();
                    }

                } else {
                    Toast toast= Toast.makeText(getApplicationContext(), "Please enter a goal description", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                    toast.show();
                }


            }
        });

        imageButton2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), GoalsActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        bmiButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bmiButtonPressed) {
                    bmiButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    bmiButtonPressed = false;
                } else {
                    bmiButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    bmiButtonPressed = true;

                    caloriesButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    caloriesButtonPressed = false;

                    exerciseButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    exerciseButtonPressed = false;
                }
            }
        });

        caloriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caloriesButtonPressed) {
                    caloriesButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    caloriesButtonPressed = false;
                } else {
                    bmiButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    bmiButtonPressed = false;

                    caloriesButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    caloriesButtonPressed = true;

                    exerciseButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    exerciseButtonPressed = false;
                }
            }
        });

        exerciseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (exerciseButtonPressed) {
                    exerciseButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    exerciseButtonPressed = false;
                } else {
                    bmiButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    bmiButtonPressed = false;

                    caloriesButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    caloriesButtonPressed = false;

                    exerciseButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    exerciseButtonPressed = true;
                }
            }
        });

        linechartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (linechartButtonPressed) {
                    linechartButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    linechartButtonPressed = false;
                } else {
                    linechartButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    linechartButtonPressed = true;

                    barchartButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    barchartButtonPressed = false;
                }
            }
        });

        barchartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (barchartButtonPressed) {
                    linechartButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    linechartButtonPressed = false;
                } else {
                    linechartButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    linechartButtonPressed = false;

                    barchartButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    barchartButtonPressed = true;
                }
            }
        });

    }
}