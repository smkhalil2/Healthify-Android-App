package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
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
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class FitnessActivity extends AppCompatActivity {

    private Context context;

    private ImageButton homeButton,  createTaskButton;
    private ListView upcomingList, completedList;

    private ArrayAdapter<String> upcomingListAdapter;
    private ArrayList<String> upcomingListItems;
    private ArrayList<Exercise> upcomingExerciseList;

    private ArrayAdapter<String> completedListAdapter;
    private ArrayList<String> completedListItems;
    private ArrayList<Exercise> completedExerciseList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fitness);

        homeButton  = (ImageButton) findViewById(R.id.homeButton);
        createTaskButton  = (ImageButton) findViewById(R.id.createTaskButton);
        upcomingList = (ListView) findViewById(R.id.upcomingList);
        completedList  = (ListView) findViewById(R.id.completedList);
        context = getApplicationContext();

        upcomingListItems = new ArrayList<String>();
        upcomingExerciseList = new ArrayList<Exercise>();

        completedListItems = new ArrayList<String>();
        completedExerciseList = new ArrayList<Exercise>();

        upcomingListAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, upcomingListItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                return view;
            }
        };

        completedListAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, completedListItems) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                return view;
            }
        };

        upcomingList.setAdapter(upcomingListAdapter);
        completedList.setAdapter(completedListAdapter);

        initializeUpcomingExercises();
        initializeCompletedExercises();

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            if (extras.getString("task").equals("completed")) {
                Exercise completedExercise = new Exercise (extras.getString("type"), extras.getString("name"), extras.getString("date"), extras.getString("time"), extras.getString("durationHours"), extras.getString("durationMinutes"));
                removeUpcomingExercise(completedExercise);
                addCompletedExercise(completedExercise);

                Toast toast= Toast.makeText(getApplicationContext(), "Exercise Completed!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                toast.show();
            } else {
                Exercise newExercise = new Exercise (extras.getString("type"), extras.getString("name"), extras.getString("date"), extras.getString("time"), extras.getString("durationHours"), extras.getString("durationMinutes"));
                addUpcomingExercise(newExercise);

                Toast toast= Toast.makeText(getApplicationContext(), "Exercise Created!", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                toast.show();
            }

        } else {

        }

        sortUpcomingExercises();
        sortCompletedExercises();

        upcomingListAdapter.notifyDataSetChanged();
        completedListAdapter.notifyDataSetChanged();

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

        createTaskButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), AddExerciseActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        upcomingList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Exercise currentExercise = upcomingExerciseList.get(position);

                Intent myIntent = new Intent(view.getContext(), ViewExerciseActivity.class);

                myIntent.putExtra("type", currentExercise.getType());
                myIntent.putExtra("name", currentExercise.getName());
                myIntent.putExtra("date", currentExercise.getDate());
                myIntent.putExtra("time", currentExercise.getTime());
                myIntent.putExtra("durationHours", currentExercise.getDurationHours());
                myIntent.putExtra("durationMinutes", currentExercise.getDurationMinutes());

                myIntent.putExtra("task", "upcoming");

                startActivityForResult(myIntent, 0);
            }
        });

        completedList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                Exercise currentExercise = completedExerciseList.get(position);

                Intent myIntent = new Intent(view.getContext(), ViewExerciseActivity.class);

                myIntent.putExtra("type", currentExercise.getType());
                myIntent.putExtra("name", currentExercise.getName());
                myIntent.putExtra("date", currentExercise.getDate());
                myIntent.putExtra("time", currentExercise.getTime());
                myIntent.putExtra("durationHours", currentExercise.getDurationHours());
                myIntent.putExtra("durationMinutes", currentExercise.getDurationMinutes());

                myIntent.putExtra("task", "completed");

                startActivityForResult(myIntent, 0);
            }
        });
    }

    public void initializeUpcomingExercises() {

        try {
            String fileName = "storage.json";
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            JSONArray users = new JSONArray();
            String line;

            if ((line = bufferedReader.readLine()) != null) {
                users = new JSONArray(line);
            }

            for (int i = 0; i < users.length(); i++) {
                JSONObject currUser = new JSONObject(users.getString(i));
                String username = currUser.getString("username");

                if (username.equals(Session.username)) {
                    JSONArray upcomingExercises = new JSONArray(currUser.getString("upcomingExercises"));

                    for (int j = 0; j < upcomingExercises.length(); j++) {
                        JSONObject currUpcomingExercise = new JSONObject(upcomingExercises.getString(j));
                        Exercise exercise = new Exercise (currUpcomingExercise.getString("type"), currUpcomingExercise.getString("name"), currUpcomingExercise.getString("date"), currUpcomingExercise.getString("time"), currUpcomingExercise.getString("durationHours"),  currUpcomingExercise.getString("durationMinutes"));
                        upcomingExerciseList.add(exercise);
                        upcomingListItems.add(exercise.toString());
                    }

                }
            }

            fis.close();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void addUpcomingExercise(Exercise newExercise) {
        try {
            String fileName = "storage.json";
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            JSONArray users = new JSONArray();
            JSONArray updatedUsers = new JSONArray();
            String line;

            if ((line = bufferedReader.readLine()) != null) {
                users = new JSONArray(line);
            }

            for (int i = 0; i < users.length(); i++) {
                JSONObject currUser = new JSONObject(users.getString(i));
                String username = currUser.getString("username");

                if (username.equals(Session.username)) {
                    JSONArray upcomingExercises = new JSONArray(currUser.getString("upcomingExercises"));

                    upcomingExercises.put(newExercise.toJSONString());

                    upcomingExerciseList.add(newExercise);
                    upcomingListItems.add(newExercise.toString());

                    currUser.put("upcomingExercises", upcomingExercises);

                    updatedUsers.put(currUser.toString());
                } else {
                    updatedUsers.put(currUser.toString());
                }
            }

            fis.close();

            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);

            fos.write(updatedUsers.toString().getBytes());

            fos.close();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void removeUpcomingExercise(Exercise completedExercise) {
        try {
            String fileName = "storage.json";
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            JSONArray users = new JSONArray();
            JSONArray updatedUsers = new JSONArray();
            String line;

            if ((line = bufferedReader.readLine()) != null) {
                users = new JSONArray(line);
            }

            for (int i = 0; i < users.length(); i++) {
                JSONObject currUser = new JSONObject(users.getString(i));
                String username = currUser.getString("username");

                if (username.equals(Session.username)) {
                    JSONArray upcomingExercises = new JSONArray(currUser.getString("upcomingExercises"));
                    JSONArray updatedUpcomingExercise = new JSONArray();

                    for (int j = 0; j < upcomingExercises.length(); j++) {
                        JSONObject currUpcomingExercise = new JSONObject(upcomingExercises.getString(j));

                        if (currUpcomingExercise.getString("type").equals(completedExercise.getType())
                                && currUpcomingExercise.getString("name").equals(completedExercise.getName())
                                && currUpcomingExercise.getString("date").equals(completedExercise.getDate())
                                && currUpcomingExercise.getString("time").equals(completedExercise.getTime())
                                && currUpcomingExercise.getString("durationHours").equals(completedExercise.getDurationHours())
                                && currUpcomingExercise.getString("durationMinutes").equals(completedExercise.getDurationMinutes())) {


                        } else {
                            updatedUpcomingExercise.put(currUpcomingExercise.toString());
                        }

                    }

                    currUser.remove("upcomingExercises");
                    currUser.put("upcomingExercises", updatedUpcomingExercise);

                    for (int j = 0; j < upcomingExerciseList.size(); j++) {
                        Exercise currExercise = upcomingExerciseList.get(j);


                        if (currExercise.getType().equals(completedExercise.getType())
                                && currExercise.getName().equals(completedExercise.getName())
                                && currExercise.getDate().equals(completedExercise.getDate())
                                && currExercise.getTime().equals(completedExercise.getTime())
                                && currExercise.getDurationHours().equals(completedExercise.getDurationHours())
                                && currExercise.getDurationMinutes().equals(completedExercise.getDurationMinutes())) {
                            upcomingExerciseList.remove(j);
                            upcomingListItems.remove(j);
                            j--;
                        }
                    }

                    updatedUsers.put(currUser.toString());
                } else {
                    updatedUsers.put(currUser.toString());
                }
            }

            fis.close();

            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);

            fos.write(updatedUsers.toString().getBytes());

            fos.close();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void initializeCompletedExercises() {
        try {
            String fileName = "storage.json";
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            JSONArray users = new JSONArray();
            String line;

            if ((line = bufferedReader.readLine()) != null) {
                users = new JSONArray(line);
            }

            for (int i = 0; i < users.length(); i++) {
                JSONObject currUser = new JSONObject(users.getString(i));
                String username = currUser.getString("username");

                if (username.equals(Session.username)) {
                    JSONArray completedExercises = new JSONArray(currUser.getString("completedExercises"));

                    for (int j = 0; j < completedExercises.length(); j++) {
                        JSONObject currCompletedExercise = new JSONObject(completedExercises.getString(j));
                        Exercise exercise = new Exercise (currCompletedExercise.getString("type"), currCompletedExercise.getString("name"), currCompletedExercise.getString("date"), currCompletedExercise.getString("time"), currCompletedExercise.getString("durationHours"),  currCompletedExercise.getString("durationMinutes"));
                        completedExerciseList.add(exercise);
                        completedListItems.add(exercise.toString());
                    }

                }
            }

            fis.close();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public void addCompletedExercise(Exercise completedExercise) {
        try {
            String fileName = "storage.json";
            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            JSONArray users = new JSONArray();
            JSONArray updatedUsers = new JSONArray();
            String line;

            if ((line = bufferedReader.readLine()) != null) {
                users = new JSONArray(line);
            }

            for (int i = 0; i < users.length(); i++) {
                JSONObject currUser = new JSONObject(users.getString(i));
                String username = currUser.getString("username");

                if (username.equals(Session.username)) {
                    JSONArray completedExercises = new JSONArray(currUser.getString("completedExercises"));

                    completedExercises.put(completedExercise.toJSONString());

                    completedExerciseList.add(completedExercise);
                    completedListItems.add(completedExercise.toString());

                    currUser.put("completedExercises", completedExercises);

                    updatedUsers.put(currUser.toString());
                } else {
                    updatedUsers.put(currUser.toString());
                }
            }

            fis.close();

            FileOutputStream fos = context.openFileOutput(fileName,Context.MODE_PRIVATE);

            fos.write(updatedUsers.toString().getBytes());

            fos.close();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sortUpcomingExercises() {
        DateFormat format = new SimpleDateFormat("M/dd/yyyy h:mma", Locale.ENGLISH);

        ArrayList<Exercise> upcomingExerciseListCopy = new ArrayList<Exercise>();
        ArrayList<String> upcomingListItemsCopy = new ArrayList<String>();

        try {
            while (!upcomingExerciseList.isEmpty()) {
                Date currDateMin = format.parse(upcomingExerciseList.get(0).getDate() + " " + upcomingExerciseList.get(0).getTime());
                int minIndex = 0;


                for (int i = 1; i < upcomingExerciseList.size(); i++) {
                    Date nextDate = format.parse(upcomingExerciseList.get(i).getDate() + " " + upcomingExerciseList.get(i).getTime());


                    if (nextDate.before(currDateMin)) {
                        currDateMin = nextDate;
                        minIndex = i;
                    }
                }

                upcomingExerciseListCopy.add(upcomingExerciseList.remove(minIndex));
                upcomingListItemsCopy.add(upcomingListItems.remove(minIndex));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < upcomingExerciseListCopy.size(); i++) {
            upcomingExerciseList.add(upcomingExerciseListCopy.get(i).clone());
            upcomingListItems.add(new String(upcomingListItemsCopy.get(i)));
        }
    }

    public void sortCompletedExercises() {
        DateFormat format = new SimpleDateFormat("M/dd/yyyy h:mma", Locale.ENGLISH);

        ArrayList<Exercise> completedExerciseListCopy = new ArrayList<Exercise>();
        ArrayList<String> completedListItemsCopy = new ArrayList<String>();

        try {
            while (!completedExerciseList.isEmpty()) {

                Date currDateMin = format.parse(completedExerciseList.get(0).getDate() + " " + completedExerciseList.get(0).getTime());
                int minIndex = 0;


                for (int i = 1; i < completedExerciseList.size(); i++) {
                    Date nextDate = format.parse(completedExerciseList.get(i).getDate() + " " + completedExerciseList.get(i).getTime());


                    if (nextDate.before(currDateMin)) {
                        currDateMin = nextDate;
                        minIndex = i;
                    }
                }

                completedExerciseListCopy.add(completedExerciseList.remove(minIndex));
                completedListItemsCopy.add(completedListItems.remove(minIndex));
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        for (int i = 0; i < completedExerciseListCopy.size(); i++) {
            completedExerciseList.add(completedExerciseListCopy.get(i).clone());
            completedListItems.add(new String(completedListItemsCopy.get(i)));
        }
    }

    @Override
    public void onBackPressed() {}
}