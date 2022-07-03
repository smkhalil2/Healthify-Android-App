package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/*where goals will be received*/
public class GoalsActivity extends AppCompatActivity {

    private Context context;

    private ImageButton homeButton;
    private ImageButton createGoal_button;
    private ListView allGoalList; /* box where it shows goals */

    private ArrayAdapter<String> upcomingListAdapter;
    private ArrayList<String> listOfAllGoals; /* array where goals will be sent to  */
    private ArrayList<Goal> eachGoal; /*each goal made*/


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_goals);

        context = getApplicationContext();

        homeButton  = (ImageButton) findViewById(R.id.homeButton);
        createGoal_button  = (ImageButton) findViewById(R.id.createGoal_button);
        allGoalList = (ListView) findViewById(R.id.allGoalList);

        eachGoal = new ArrayList<Goal>();
        listOfAllGoals = new ArrayList<String>();
        /**/
        upcomingListAdapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_item, listOfAllGoals) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
                View view = super.getView(position, convertView, parent);
                TextView tv = (TextView) view.findViewById(android.R.id.text1);
                tv.setTextColor(Color.WHITE);
                return view;
            }
        };

        allGoalList.setAdapter(upcomingListAdapter);

        Bundle extras = getIntent().getExtras();

        if (extras != null) {

            Goal newGoal = new Goal (extras.getString("type"), extras.getString("chart"), extras.getString("goalInformation"), extras.getString("date"));
            addUpcomingGoal(newGoal);

        } else {

        }

        initializeUpcomingGoals();

        upcomingListAdapter.notifyDataSetChanged();
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

        createGoal_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), goal_listpage.class);
                startActivityForResult(myIntent, 0);
            }
        });

        /* show new inputs from goal_listpage */
        allGoalList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Goal currentGoal = eachGoal.get(position);
                Intent myIntent = new Intent(view.getContext(), ViewGoalActivity.class);

                myIntent.putExtra("type", currentGoal.getType());
                myIntent.putExtra("chart", currentGoal.getChart());
                myIntent.putExtra("goalInformation", currentGoal.getName());
                myIntent.putExtra("date", currentGoal.getDate());

                startActivityForResult(myIntent, 0);
            }
        });

    }

    public void initializeUpcomingGoals() {

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
                    JSONArray upcomingGoals = new JSONArray(currUser.getString("upcomingGoals"));

                    for (int j = 0; j < upcomingGoals.length(); j++) {
                        JSONObject currUpcomingGoal = new JSONObject(upcomingGoals.getString(j));
                        Goal goal = new Goal (currUpcomingGoal.getString("type"), currUpcomingGoal.getString("chart"), currUpcomingGoal.getString("goalInformation"), currUpcomingGoal.getString("date"));
                        eachGoal.add(goal);
                        listOfAllGoals.add(goal.toString());
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


    public void addUpcomingGoal(Goal newGoal) {
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
                    JSONArray upcomingGoals = new JSONArray(currUser.getString("upcomingGoals"));

                    upcomingGoals.put(newGoal.toJSONString());

                    currUser.put("upcomingGoals", upcomingGoals);

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

    @Override
    public void onBackPressed() {}
}