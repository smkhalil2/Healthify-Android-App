package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
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


public class NewUserActivity extends AppCompatActivity {

    // Variables
    private Context context;
    private Button continueProcessButton;
    private ImageButton backButtonToLogin;
    private EditText eUsername, ePassword, eConfirmation, eEmail;
    private DatePicker dob;

    private String username, password, confirmation, email, dateofbirth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_user);

        // Assigns variables
        continueProcessButton = (Button) findViewById(R.id.continueProcessButton);
        backButtonToLogin = (ImageButton) findViewById(R.id.backButtonToLogin);
        eUsername = (EditText) findViewById(R.id.usernameEntry);
        ePassword = (EditText) findViewById(R.id.newPasswordEntry);
        eConfirmation = (EditText) findViewById(R.id.passwordConfirmEntry);
        eEmail = (EditText) findViewById(R.id.emailEntry);
        dob = (DatePicker)findViewById(R.id.datePickerNewUser);
        context = getApplicationContext();

        initializeOnClickListeners();
    }

    private void initializeOnClickListeners() {


        // Assigns activity for buttons
        continueProcessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Checks for empty fields
                if (eUsername.getText().toString().isEmpty() || ePassword.getText().toString().isEmpty() || eConfirmation.getText().toString().isEmpty() || eEmail.getText().toString().isEmpty()) {
                    Toast toast= Toast.makeText(getApplicationContext(), "Enter all fields!", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                    toast.show();
                }

                // No Empty Fields
                else{
                    // Valid username
                    if(true){

                        //valid password
                        if(pwCheck(ePassword.getText().toString())){

                            if (emailCheck(eEmail.getText().toString())){
                                // Checks if password matches confirmation
                                if(ePassword.getText().toString().equals(eConfirmation.getText().toString())){

                                    // Covnerts inputs to strings to be entered into JSON
                                    username = eUsername.getText().toString();
                                    password = ePassword.getText().toString();
                                    email = eEmail.getText().toString();
                                    dateofbirth = dob.getMonth() + "-" + dob.getDayOfMonth() + "-" + dob.getYear();

                                    // Transfer data
                                    Bundle bundle = new Bundle();
                                    bundle.putString("username", username);
                                    bundle.putString("pw", password);
                                    bundle.putString("email", email);
                                    bundle.putString("dob", dateofbirth);
                                    Intent myIntent = new Intent(v.getContext(), new_user_final.class);
                                    myIntent.putExtras(bundle);
                                    startActivityForResult(myIntent, 0);
                                }

                                // passwords do not match
                                else{
                                    Toast toast= Toast.makeText(getApplicationContext(), "Passwords do not match!", Toast.LENGTH_SHORT);
                                    toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                                    toast.show();
                                }
                            }

                            // Invalid email
                            else{
                                Toast toast= Toast.makeText(getApplicationContext(), "Please enter valid email (must have @website.domain)!", Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                                toast.show();
                            }


                        }

                        //Invalid Password
                        else {
                            Toast toast= Toast.makeText(getApplicationContext(), "Password is invalid!", Toast.LENGTH_SHORT);
                            toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                            toast.show();
                        }
                    }

                    // Invalid Username
                    else{
                        Toast toast= Toast.makeText(getApplicationContext(), "Username is taken!", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                        toast.show();
                    }
                }

            }
        });

        backButtonToLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

    }

    // Checks email
    private boolean emailCheck(String mail){
        if(mail.contains("@") == false){
            return false;
        }

        if((mail.substring(mail.lastIndexOf("@") + 1).contains(".")) == false){
            return false;
        }


        return true;
    }


    // Checks if a password is valid
    private boolean pwCheck(String pw){
        // Checks password length
        if (pw.length() < 8){
            return false;
        }

        // Checks for number and cases
        boolean num = false, lcase = false, ucase = false;

        // Loops through password
        char[] charArray = pw.toCharArray();
        for(int count = 0; count < charArray.length; count++){

            //Checks for upper case
            if(Character.isUpperCase(charArray[count])){
                ucase = true;
            }

            //Checks for lower case
            if(Character.isLowerCase(charArray[count])){
                lcase = true;
            }
        }

        //Checks for numbers
        if(pw.matches(".*\\d.*")){
            num = true;
        }

        if(num == false || ucase == false || lcase == false){
            return false;
        }

        return true;
    }

    // Checks if a username is taken within the database
    private boolean checkUser(String uname){
        // Variables
        JSONObject account;
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

                if(dbUsername.equals(uname)){
                    return false;
                }
            }

            fis.close();

        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        return true;
    }


}