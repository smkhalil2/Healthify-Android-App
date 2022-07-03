package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Locale;

public class ViewExerciseActivity extends AppCompatActivity {

    private ImageButton homeButton, backButton;
    private Button caloriesButton, heartRateButton, stepsButton;
    private Button startPauseButton, restartButton, stopButton;
    private ImageView imageView;
    private TextView nameLabel, dateLabel, durationLabel, selectLabel, timeLabel;
    private boolean caloriesButtonPressed, heartRateButtonPressed, stepsButtonPressed;
    private boolean startPauseButtonPressed, durationReached;
    private boolean stopwatchRunning;
    private Exercise newExercise;

    private int seconds, durationBound;

    private Handler stopwatch;
    private Runnable stopwatchRunnable;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_exercise);

        homeButton = (ImageButton) findViewById(R.id.homeButton);
        backButton = (ImageButton) findViewById(R.id.backButton);

        caloriesButton = (Button) findViewById(R.id.caloriesButton);
        heartRateButton = (Button) findViewById(R.id.heartRateButton);
        stepsButton = (Button) findViewById(R.id.stepsButton);

        startPauseButton = (Button) findViewById(R.id.startPauseButton);
        restartButton = (Button) findViewById(R.id.restartButton);
        stopButton = (Button) findViewById(R.id.stopButton);

        timeLabel = (TextView) findViewById(R.id.timeLabel);
        selectLabel = (TextView) findViewById(R.id.selectLabel);

        caloriesButtonPressed = false;
        heartRateButtonPressed = false;
        stepsButtonPressed = false;

        startPauseButtonPressed = false;
        stopwatchRunning = false;
        durationReached = false;

        seconds = 0;

        startPauseButton.setText("Start");
        startPauseButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.play35x35);

        imageView = (ImageView) findViewById(R.id.taskImageView);

        stopwatch = new Handler();

        nameLabel = (TextView) findViewById(R.id.nameLabel);
        dateLabel = (TextView) findViewById(R.id.dateLabel);
        durationLabel = (TextView) findViewById(R.id.durationLabel);
        timeLabel = (TextView) findViewById(R.id.timeLabel);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            newExercise = new Exercise (extras.getString("type"), extras.getString("name"), extras.getString("date"), extras.getString("time"), extras.getString("durationHours"), extras.getString("durationMinutes"));

            nameLabel.setText(newExercise.getName());
            dateLabel.setText(newExercise.getDate() + ", " + newExercise.getTime());
            durationLabel.setText("Duration: " + newExercise.getDuration());

            durationBound = Integer.parseInt(newExercise.getDurationHours()) * 3600 + Integer.parseInt(newExercise.getDurationMinutes()) * 60;

            String exerciseType = newExercise.getType();

            if (exerciseType.equals("aerobic")) {
                imageView.setImageResource(R.drawable.ic_aerobic_white);
            } else if (exerciseType.equals("strength")) {
                imageView.setImageResource(R.drawable.ic_strength_red);
            } else if (exerciseType.equals("flexibility")) {
                imageView.setImageResource(R.drawable.ic_stretch_orange);
            } else if (exerciseType.equals("balance")) {
                imageView.setImageResource(R.drawable.ic_balance_yellow);
            } else if (exerciseType.equals("other")) {
                imageView.setImageResource(R.drawable.ic_other_pink);
            }

            if (extras.getString("task").equals("upcoming")) {
                timeLabel.setText("00:00:00");

                selectLabel.setText("Select what to track:");

                caloriesButton.setVisibility(View.VISIBLE);
                heartRateButton.setVisibility(View.VISIBLE);
                stepsButton.setVisibility(View.VISIBLE);

                startPauseButton.setVisibility(View.VISIBLE);
                restartButton.setVisibility(View.VISIBLE);
                stopButton.setVisibility(View.VISIBLE);

            } else {

                if (newExercise.getDurationHours().length() == 1) {

                    if (newExercise.getDurationMinutes().length() == 1) {
                        timeLabel.setText("0" + newExercise.getDurationHours() + ":0" + newExercise.getDurationMinutes() + ":00");
                    } else {
                        timeLabel.setText("0" + newExercise.getDurationHours() + ":" + newExercise.getDurationMinutes() + ":00");
                    }

                } else {

                    if (newExercise.getDurationMinutes().length() == 1) {
                        timeLabel.setText(newExercise.getDurationHours() + ":0" + newExercise.getDurationMinutes() + ":00");
                    } else {
                        timeLabel.setText(newExercise.getDurationHours() + ":" + newExercise.getDurationMinutes() + ":00");
                    }

                }

                selectLabel.setText("Task completed!");

                caloriesButton.setVisibility(View.GONE);
                heartRateButton.setVisibility(View.GONE);
                stepsButton.setVisibility(View.GONE);

                startPauseButton.setVisibility(View.GONE);
                restartButton.setVisibility(View.GONE);
                stopButton.setVisibility(View.GONE);
            }
        } else {

            // No data has been passed in to view
        }

        stopwatchRunnable = new Runnable() {
            @Override
            public void run() {
                if (stopwatchRunning) {
                    if (durationReached) {
                        seconds++;

                        int hours = seconds / 3600;
                        int minutes = (seconds % 3600) / 60;
                        int secs = seconds % 60;

                        String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);

                        timeLabel.setText(time);

                        stopwatch.postDelayed(stopwatchRunnable, 1000);
                    } else {
                        if (seconds == durationBound) {
                            startPauseButton.setText("Resume");
                            startPauseButtonPressed = false;
                            stopwatchRunning = false;
                            startPauseButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.play35x35);
                            durationReached = true;

                            Toast toast= Toast.makeText(getApplicationContext(), "Times up!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                            toast.show();
                        } else {
                            seconds++;

                            int hours = seconds / 3600;
                            int minutes = (seconds % 3600) / 60;
                            int secs = seconds % 60;

                            String time = String.format(Locale.getDefault(), "%02d:%02d:%02d", hours, minutes, secs);

                            timeLabel.setText(time);

                            stopwatch.postDelayed(stopwatchRunnable, 1000);
                        }
                    }

                } else {

                    stopwatch.removeCallbacks(stopwatchRunnable);
                }
            }
        };

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
                Intent myIntent = new Intent(v.getContext(), FitnessActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });


        caloriesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (caloriesButtonPressed) {
                    caloriesButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    caloriesButtonPressed = false;
                } else {
                    caloriesButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    caloriesButtonPressed = true;
                }
            }
        });

        heartRateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (heartRateButtonPressed) {
                    heartRateButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    heartRateButtonPressed = false;
                } else {
                    heartRateButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    heartRateButtonPressed = true;
                }
            }
        });

        stepsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (stepsButtonPressed) {
                    stepsButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    stepsButtonPressed = false;
                } else {
                    stepsButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    stepsButtonPressed = true;
                }
            }
        });

        startPauseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (startPauseButtonPressed) {
                    startPauseButton.setText("Resume");
                    //startPauseButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.unpressed, null));
                    startPauseButtonPressed = false;
                    stopwatchRunning = false;
                    startPauseButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.play35x35);

                } else {
                    startPauseButton.setText("Pause");
                    //startPauseButton.setBackground(ResourcesCompat.getDrawable(getResources(), R.drawable.pressed, null));
                    startPauseButtonPressed = true;
                    stopwatchRunning = true;
                    startPauseButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.pause35x35);

                    startStopwatch();
                }
            }
        });

        restartButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startPauseButton.setText("Start");
                startPauseButtonPressed = false;
                stopwatchRunning = false;
                durationReached = false;
                startPauseButton.setCompoundDrawablesWithIntrinsicBounds(0, 0, 0, R.drawable.play35x35);

                seconds = 0;
                timeLabel.setText("00:00:00");
            }
        });

        stopButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stopwatchRunning = false;
                durationReached = false;

                // Send newExercise to FitnessActivity to complete exercise
                newExercise.setComplete();

                Intent myIntent = new Intent(v.getContext(), FitnessActivity.class);

                myIntent.putExtra("type", newExercise.getType());
                myIntent.putExtra("name", newExercise.getName());
                myIntent.putExtra("date", newExercise.getDate());
                myIntent.putExtra("time", newExercise.getTime());
                myIntent.putExtra("durationHours", newExercise.getDurationHours());
                myIntent.putExtra("durationMinutes", newExercise.getDurationMinutes());

                myIntent.putExtra("task", "completed");
                startActivityForResult(myIntent, 0);

            }
        });

    }

    public void startStopwatch() {
        stopwatch.postDelayed(stopwatchRunnable, 1000);
    }

}