package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class forgot_password extends AppCompatActivity {
    private ImageButton backButton;
    private Button changePasswordButton1;
    private EditText emailEntryFtPw;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        // Sets by ID
        backButton  = (ImageButton) findViewById(R.id.backButton);
        changePasswordButton1 = (Button) findViewById(R.id.changePasswordButton1);
        emailEntryFtPw = (EditText) findViewById(R.id.emailEntryFtPw);
        context = getApplicationContext();

        initializeOnClickListeners();
    }


    private void initializeOnClickListeners() {
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });


        changePasswordButton1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checks for empty fields
                if (emailEntryFtPw.getText().toString().isEmpty()) {
                    Toast toast= Toast.makeText(getApplicationContext(), "Enter an email!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                    toast.show();
                }

                // Entered fields
                else{

                    //Checks login credentials
                    if(checkEmail(emailEntryFtPw.getText().toString())){
                        Toast toast= Toast.makeText(getApplicationContext(), "Instruction to change password emailed!", Toast.LENGTH_LONG);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                        toast.show();
                        // Changes Password
                        Intent myIntent = new Intent(v.getContext(), LoginActivity.class);
                        startActivityForResult(myIntent, 0);
                    }

                    else{
                        Toast toast= Toast.makeText(getApplicationContext(), "Invalid Email!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                        toast.show();
                    }
                }
            }
        });
    }

    // Checks for valid email

    private boolean checkEmail(String emEntry){
        // Variables
        FileInputStream fis;
        String fileName = "storage.json";
        JSONArray users = new JSONArray();

        try {
            fis = context.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fis);
            BufferedReader bufferedReader = new BufferedReader(isr);
            String line;

            if ((line = bufferedReader.readLine()) != null) {
                users = new JSONArray(line);
            }

            for (int i = 0; i < users.length(); i++) {
                JSONObject currUser = new JSONObject(users.getString(i));
                String dbEmail = currUser.getString("email");

                if(dbEmail.equals(emEntry)){
                    return true;
                }
            }

            fis.close();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return false;
    }

}