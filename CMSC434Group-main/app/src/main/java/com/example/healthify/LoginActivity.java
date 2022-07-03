package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class LoginActivity extends AppCompatActivity {

    // Variables
    private Button loginButton, newUserButton, forgotButton;
    private EditText ePw, eUn;
    private Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Sets buttons to id values
        loginButton  = (Button) findViewById(R.id.loginButton);
        newUserButton = (Button) findViewById(R.id.newUserButton);
        forgotButton = (Button) findViewById(R.id.forgotButton);

        eUn = (EditText) findViewById(R.id.usernameEntryLn);
        ePw = (EditText) findViewById(R.id.passwordEntryLn);

        context = getApplicationContext();

        initializeOnClickListeners();
    }

    private void initializeOnClickListeners() {

        // Button Event Listeners
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Checks for empty fields
                if (eUn.getText().toString().isEmpty() || ePw.getText().toString().isEmpty()) {
                    Toast toast= Toast.makeText(getApplicationContext(), "Enter username and password!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                    toast.show();
                }

                // Entered fields
                else{

                    //Checks login credentials
                    if(checkUserLogin(eUn.getText().toString(), ePw.getText().toString())){
                        // Set session id
                        Session.username = eUn.getText().toString();

                        // Logs in
                        Intent myIntent = new Intent(v.getContext(), MainActivity.class);

                        Toast toast= Toast.makeText(getApplicationContext(), "Welcome Back!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                        toast.show();

                        startActivityForResult(myIntent, 0);
                    }

                    else{
                        Toast toast= Toast.makeText(getApplicationContext(), "Invalid Login!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                        toast.show();
                    }
                }

            }
        });

        newUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), NewUserActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

        forgotButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), forgot_password.class);
                startActivityForResult(myIntent, 0);
            }
        });
    }

    // Check login credentials

    private boolean checkUserLogin(String un, String pw){
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

                String dbUsername = currUser.getString("username");
                String dbPassword = currUser.getString("password");

                if(dbUsername.equals(un) && dbPassword.equals(pw)){
                    return true;
                }

            }

            fis.close();



        } catch (JSONException e) {
            // Parse error for JSONArray, empty file and recurse
            try {
                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                fos.write("".getBytes());
            } catch (IOException e2) {

            }

            checkUserLogin(un, pw);

        } catch (IOException e) {

            // File not found exception, write file and recurse
            try {
                FileOutputStream fos = context.openFileOutput(fileName, Context.MODE_PRIVATE);
                fos.write("".getBytes());
            } catch (IOException e2) {

            }

            checkUserLogin(un, pw);
        }

        return false;
    }

    @Override
    public void onBackPressed() {}
}