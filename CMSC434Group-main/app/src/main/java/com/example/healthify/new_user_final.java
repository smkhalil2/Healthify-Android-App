package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
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
import java.util.ArrayList;
import java.util.List;

import javax.security.auth.login.LoginException;

public class new_user_final extends AppCompatActivity {

    // Variables
    private Context context;
    private Button addUserButton;
    private Button cancelAddUserButton;
    private ImageButton backButtonToFst;
    private Spinner spnGender;
    private TextView txtGender;
    private Spinner spnFitness;
    private TextView txtFitness;
    private EditText nameEntry;
    private EditText weightEntry;
    private EditText heightEntryFt;
    private EditText heightEntryIn;
    private String genderVal,fitnessVal,uname,uweight,uheightft,uheightin;

    // Variables from previous pages
    String username = "";
    String password = "";
    String email = "";
    String dateofbirth = "";



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user_final);

        // Inputs
        nameEntry = (EditText) findViewById(R.id.nameEntry);
        weightEntry = (EditText) findViewById(R.id.weightEntry);
        heightEntryFt = (EditText) findViewById(R.id.heightEntryFt);
        heightEntryIn = (EditText) findViewById(R.id.heightEntryIn);
        context = getApplicationContext();

        // Buttons
        addUserButton = (Button) findViewById(R.id.addUserButton);
        cancelAddUserButton = (Button) findViewById(R.id.cancelAddUserButton);
        backButtonToFst = (ImageButton) findViewById(R.id.backButtonToFst);

        // Gender Spinners
        spnGender = (Spinner) findViewById(R.id.genderType);
        List<String> genderLst = new ArrayList<>();
        genderLst.add("-Select Gender-");
        genderLst.add("Male");
        genderLst.add("Female");
        genderLst.add("Other");

        ArrayAdapter<String> genderAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, genderLst);
        genderAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnGender.setAdapter(genderAdapter);

        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedGender = spnGender.getSelectedItem().toString();
                /*
                if (!selectedGender.equals("-Select Gender-")) {
                    txtGender.setText(spnGender.getSelectedItem().toString());
                }
                 */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        // Fitness Spinners
        spnFitness = (Spinner) findViewById(R.id.fitnessType);
        List<String> fitnessLst = new ArrayList<>();
        fitnessLst.add("-Select Fitness Level-");
        fitnessLst.add("Out Of Shape");
        fitnessLst.add("Average");
        fitnessLst.add("Fit");

        ArrayAdapter<String> fitnessAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, fitnessLst);
        fitnessAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnFitness.setAdapter(fitnessAdapter);

        spnGender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String selectedFitness = spnFitness.getSelectedItem().toString();
                /*
                if (!selectedFitness.equals("-Select Fitness Level-")) {
                    txtFitness.setText(spnFitness.getSelectedItem().toString());
                }
                 */
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        initializeOnClickListeners();
    }

    private void initializeOnClickListeners() {

        addUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checks for empty fields
                if (nameEntry.getText().toString().isEmpty() || weightEntry.getText().toString().isEmpty() || heightEntryFt.getText().toString().isEmpty() ||
                        heightEntryIn.getText().toString().isEmpty() || ((spnGender.getSelectedItem().toString()).equals("-Select Gender-")) ||
                        ((spnFitness.getSelectedItem().toString()).equals("-Select Fitness Level-"))) {

                    Toast toast= Toast.makeText(getApplicationContext(), "Enter all fields!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                    toast.show();

                }

                // No Empty Fields
                else{

                    // Checks for invalid measurements
                    double lbWt = Double.parseDouble(weightEntry.getText().toString());
                    double ftHt = Double.parseDouble(heightEntryFt.getText().toString());
                    double inHt = Double.parseDouble(heightEntryIn.getText().toString());

                    // Valid measurements (greater than 0)
                    if(lbWt > 0 && ftHt > 0 && inHt >= 0){
                        //Checks inches
                        if(inHt <= 12){
                            // Total Height in Inches
                            double totalHt = (12 * ftHt) + inHt;

                            // Creates user
                            Bundle bundle = getIntent().getExtras();
                            if(bundle != null) {
                                username = bundle.getString("username");
                                password = bundle.getString("pw");
                                email = bundle.getString("email");
                                dateofbirth = bundle.getString("dob");
                            }

                            genderVal = spnGender.getSelectedItem().toString();
                            fitnessVal = spnFitness.getSelectedItem().toString();
                            uname = nameEntry.getText().toString();

                            // User creation success
                            if(addNewUser(username, password, dateofbirth, uname,genderVal,fitnessVal,email,lbWt,totalHt)){
                                // Sets session id
                                Session.username = username;

                                // Sends to main page
                                Toast toast= Toast.makeText(getApplicationContext(), "Welcome to Healthify!", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                                toast.show();

                                Intent myIntent = new Intent(v.getContext(), MainActivity.class);
                                startActivityForResult(myIntent, 0);
                            }

                            // User not created
                            else{
                                Toast toast= Toast.makeText(getApplicationContext(), "Error in creating user contact dev team!", Toast.LENGTH_SHORT);
                                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                                toast.show();
                            }

                        }

                        //Invalid Inches
                        else{
                            Toast toast= Toast.makeText(getApplicationContext(), "Enter valid measurements (Inches)!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                            toast.show();
                        }

                    }

                    // Invalid measurements
                    else{
                        Toast toast= Toast.makeText(getApplicationContext(), "Enter valid measurements!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                        toast.show();
                    }

                }

            }
        });

        cancelAddUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        backButtonToFst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), NewUserActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

    }

    // Adds new user to the database
    private boolean addNewUser(String username, String password, String dateOfBirth, String name, String gender, String fitnessLvl, String email, double weight, double height){
        // Example: JSON File Write, Assets stored into storage.json, a local item

        String fileName = "storage.json";

        try {
            JSONArray users = new JSONArray();

            FileInputStream fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);

            String line;

            if ((line = bufferedReader.readLine()) != null) {
                users = new JSONArray(line);
            }

            fis.close();

            JSONObject account = new JSONObject();
            account.put("username", username);
            account.put("password", password);
            account.put("dateOfBirth", dateOfBirth);
            account.put("name", name);
            account.put("gender", gender);
            account.put("fitnessLvl", fitnessLvl);
            account.put("email", email);
            account.put("weight", weight);
            account.put("height", height);

            JSONArray upcomingExercises = new JSONArray();
            JSONArray completedExercises = new JSONArray();

            JSONArray upcomingGoals = new JSONArray();

            account.put("upcomingExercises", upcomingExercises);
            account.put("completedExercises", completedExercises);

            account.put("upcomingGoals", upcomingGoals);

            users.put(account.toString());

            FileOutputStream fos = context.openFileOutput( fileName, Context.MODE_PRIVATE);

            fos.write(users.toString().getBytes());


            fos.close();

        } catch (JSONException e) {

            // Parse error for JSONArray, empty file and recurse
            try {
                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                fos.write("".getBytes());
            } catch (IOException e2) {

            }

            addNewUser(username, password, dateOfBirth, name, gender, fitnessLvl, email, weight, height);


        } catch (IOException e) {
            // File not found exception, write file and recurse
            try {
                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                fos.write("".getBytes());
            } catch (IOException e2) {

            }

            addNewUser(username, password, dateOfBirth, name, gender, fitnessLvl, email, weight, height);

        }

        return true;
    }

} 