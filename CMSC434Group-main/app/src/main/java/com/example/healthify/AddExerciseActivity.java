package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.InputType;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Button;
import android.widget.TimePicker;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class AddExerciseActivity extends AppCompatActivity {

    private Context context;
    private ImageButton cancelTaskButton, homeButton, createTaskButton, notificationButton;

    private Button aerobicButton, strengthButton, flexibilityButton, balanceButton, otherButton;
    private Button easyButton, mediumButton, hardButton, customButton;

    private boolean aerobicButtonPressed, strengthButtonPressed, flexibilityButtonPressed, balanceButtonPressed, otherButtonPressed;
    private boolean easyButtonPressed, mediumButtonPressed, hardButtonPressed, customButtonPressed, notificationButtonPressed;

    private EditText nameEntry, hoursEntry, minutesEntry;

    private DatePicker datePicker;
    private TimePicker timePicker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_exercise);

        context = getApplicationContext();

        homeButton  = (ImageButton) findViewById(R.id.homeButton);
        createTaskButton = (ImageButton) findViewById(R.id.createTaskButton);
        cancelTaskButton = (ImageButton) findViewById(R.id.cancelTaskButton);
        notificationButton = (ImageButton) findViewById(R.id.notificationButton);

        aerobicButton = (Button) findViewById(R.id.aerobicButton);
        strengthButton = (Button) findViewById(R.id.strengthButton);
        flexibilityButton = (Button) findViewById(R.id.flexibilityButton);
        balanceButton = (Button) findViewById(R.id.balanceButton);
        otherButton = (Button) findViewById(R.id.otherButton);

        easyButton = (Button) findViewById(R.id.easyButton);
        mediumButton = (Button) findViewById(R.id.mediumButton);
        hardButton = (Button) findViewById(R.id.hardButton);
        customButton = (Button) findViewById(R.id.customButton);

        aerobicButtonPressed = false;
        strengthButtonPressed = false;
        flexibilityButtonPressed = false;
        balanceButtonPressed = false;
        otherButtonPressed = false;

        easyButtonPressed = false;
        mediumButtonPressed = false;
        hardButtonPressed = false;
        customButtonPressed = false;
        notificationButtonPressed = false;

        nameEntry = (EditText) findViewById(R.id.nameEntry);
        hoursEntry = (EditText) findViewById(R.id.hoursEntry);
        minutesEntry = (EditText) findViewById(R.id.minutesEntry);

        datePicker = (DatePicker) findViewById(R.id.datePicker);
        timePicker = (TimePicker) findViewById(R.id.timePicker);

        hoursEntry.setFocusable(false);
        minutesEntry.setFocusable(false);

        initializeOnClickListeners();
    }

    @SuppressLint("ClickableViewAccessibility")
    private void initializeOnClickListeners() {

        homeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (notificationButtonPressed) {
                    Toast toast= Toast.makeText(getApplicationContext(), "Notifications off!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                    toast.show();
                    notificationButtonPressed = false;
                } else {
                    Toast toast= Toast.makeText(getApplicationContext(), "Notifications on!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                    toast.show();
                    notificationButtonPressed = true;
                }
            }
        });

        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String type = "", name, date, time, durationHours, durationMinutes;

                if (aerobicButtonPressed) {
                    type = "aerobic";
                } else if (strengthButtonPressed) {
                    type = "strength";
                } else if (flexibilityButtonPressed) {
                    type = "flexibility";
                } else if (balanceButtonPressed) {
                    type = "balance";
                } else if (otherButtonPressed) {
                    type = "other";
                }

                name = nameEntry.getText().toString();

                String month = String.valueOf(datePicker.getMonth() + 1);
                String day = String.valueOf(datePicker.getDayOfMonth());
                String year = String.valueOf(datePicker.getYear());

                date = month + "/" + day + "/" + year;

                Integer hours = timePicker.getCurrentHour();
                String minutes;

                if (timePicker.getCurrentMinute() < 10) {
                    minutes = "0" + String.valueOf(timePicker.getCurrentMinute());
                } else {
                    minutes = String.valueOf(timePicker.getCurrentMinute());
                }

                if (hours == 0) {
                    time = "12" + ":" + minutes + "AM";
                } else if (hours == 12) {
                    time = "12" + ":" + minutes + "PM";
                } else if (hours > 12) {
                    if (hours - 12 < 10) {
                        time = "0" + (hours - 12) + ":" + minutes + "PM";
                    } else {
                        time = (hours - 12) + ":" + minutes + "PM";
                    }
                } else {
                    if (hours < 10) {
                        time = "0" + hours + ":" + minutes + "AM";
                    } else {
                        time = hours + ":" + minutes + "AM";
                    }
                }

                durationHours = hoursEntry.getText().toString();
                durationMinutes = minutesEntry.getText().toString();




                if (easyButtonPressed || mediumButtonPressed || hardButtonPressed || customButtonPressed) {

                    if (aerobicButtonPressed || strengthButtonPressed || flexibilityButtonPressed || balanceButtonPressed || otherButtonPressed) {

                        if (name != null && !name.equals("")) {

                            DateFormat format = new SimpleDateFormat("M/dd/yyyy h:mma", Locale.ENGLISH);

                            try {
                                Date formattedDate = format.parse(date + " " + time);

                                if (!formattedDate.before(new Date())) {

                                    if (durationHours.length() != 0 && durationMinutes.length() != 0) {

                                        if (Integer.parseInt(durationHours) > 0 || Integer.parseInt(durationMinutes) > 0) {
                                            Intent myIntent = new Intent(v.getContext(), FitnessActivity.class);
                                            myIntent.putExtra("type", type);
                                            myIntent.putExtra("name", name);
                                            myIntent.putExtra("date", date);
                                            myIntent.putExtra("time", time);
                                            myIntent.putExtra("durationHours", durationHours);
                                            myIntent.putExtra("durationMinutes", durationMinutes);

                                            myIntent.putExtra("task", "upcoming");
                                            startActivityForResult(myIntent, 0);
                                        } else {

                                            Toast toast= Toast.makeText(getApplicationContext(), "Please enter a positive duration", Toast.LENGTH_SHORT);
                                            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                                            toast.show();

                                        }

                                    } else {

                                        Toast toast= Toast.makeText(getApplicationContext(), "Please enter a duration", Toast.LENGTH_SHORT);
                                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                                        toast.show();

                                    }

                                } else {

                                    Toast toast= Toast.makeText(getApplicationContext(), "Please enter a date in the future", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                                    toast.show();

                                }

                            } catch (ParseException e) {
                                e.printStackTrace();
                            }

                        } else {
                            Toast toast= Toast.makeText(getApplicationContext(), "Please enter an exercise name", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                            toast.show();
                        }

                    } else {
                        Toast toast= Toast.makeText(getApplicationContext(), "Please select an exercise type", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                        toast.show();
                    }

                } else {
                    Toast toast= Toast.makeText(getApplicationContext(), "Please select a difficulty", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                    toast.show();
                }

            }
        });

        cancelTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), FitnessActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        aerobicButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (aerobicButtonPressed) {
                    aerobicButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    aerobicButtonPressed = false;

                } else {
                    aerobicButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    aerobicButtonPressed = true;

                    strengthButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    strengthButtonPressed = false;

                    flexibilityButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    flexibilityButtonPressed = false;

                    balanceButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    balanceButtonPressed = false;

                    otherButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    otherButtonPressed = false;
                }
            }
        });

        strengthButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (strengthButtonPressed) {
                    strengthButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    strengthButtonPressed = false;
                } else {
                    aerobicButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    aerobicButtonPressed = false;

                    strengthButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    strengthButtonPressed = true;

                    flexibilityButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    flexibilityButtonPressed = false;

                    balanceButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    balanceButtonPressed = false;

                    otherButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    otherButtonPressed = false;
                }
            }
        });

        flexibilityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (flexibilityButtonPressed) {
                    flexibilityButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    flexibilityButtonPressed = false;
                } else {
                    aerobicButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    aerobicButtonPressed = false;

                    strengthButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    strengthButtonPressed = false;

                    flexibilityButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    flexibilityButtonPressed = true;

                    balanceButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    balanceButtonPressed = false;

                    otherButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    otherButtonPressed = false;
                }
            }
        });

        balanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (balanceButtonPressed) {
                    balanceButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    balanceButtonPressed = false;
                } else {
                    aerobicButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    aerobicButtonPressed = false;

                    strengthButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    strengthButtonPressed = false;

                    flexibilityButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    flexibilityButtonPressed = false;

                    balanceButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    balanceButtonPressed = true;

                    otherButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    otherButtonPressed = false;
                }
            }
        });

        otherButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (otherButtonPressed) {
                    otherButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    otherButtonPressed = false;
                } else {
                    aerobicButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    aerobicButtonPressed = false;

                    strengthButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    strengthButtonPressed = false;

                    flexibilityButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    flexibilityButtonPressed = false;

                    balanceButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    balanceButtonPressed = false;

                    otherButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    otherButtonPressed = true;
                }
            }
        });

        easyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (easyButtonPressed) {
                    easyButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    easyButtonPressed = false;
                } else {
                    easyButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    easyButtonPressed = true;

                    mediumButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    mediumButtonPressed = false;

                    hardButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    hardButtonPressed = false;

                    customButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    customButtonPressed = false;

                    hoursEntry.setFocusable(false);
                    minutesEntry.setFocusable(false);

                    hoursEntry.setText("0");
                    minutesEntry.setText("30");
                }
            }
        });

        mediumButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediumButtonPressed) {
                    mediumButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    mediumButtonPressed = false;
                } else {
                    easyButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    easyButtonPressed = false;

                    mediumButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    mediumButtonPressed = true;

                    hardButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    hardButtonPressed = false;

                    customButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    customButtonPressed = false;

                    hoursEntry.setFocusable(false);
                    minutesEntry.setFocusable(false);

                    hoursEntry.setText("1");
                    minutesEntry.setText("0");
                }
            }
        });

        hardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (hardButtonPressed) {
                    hardButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    hardButtonPressed = false;
                } else {
                    easyButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    easyButtonPressed = false;

                    mediumButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    mediumButtonPressed = false;

                    hardButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    hardButtonPressed = true;

                    customButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    customButtonPressed = false;

                    hoursEntry.setFocusable(false);
                    minutesEntry.setFocusable(false);

                    hoursEntry.setText("1");
                    minutesEntry.setText("30");
                }
            }
        });

        customButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (customButtonPressed) {
                    customButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    customButtonPressed = false;

                    hoursEntry.setFocusable(false);
                    minutesEntry.setFocusable(false);

                } else {
                    easyButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    easyButtonPressed = false;

                    mediumButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    mediumButtonPressed = false;

                    hardButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    hardButtonPressed = false;

                    customButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    customButtonPressed = true;

                    hoursEntry.setFocusableInTouchMode(true);
                    minutesEntry.setFocusableInTouchMode(true);

                }

                hoursEntry.setText("0");
                minutesEntry.setText("0");
            }
        });

    }
}