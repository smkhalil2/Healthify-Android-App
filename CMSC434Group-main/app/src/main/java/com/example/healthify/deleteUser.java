package com.example.healthify;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class deleteUser extends AppCompatActivity {

    private Button cancelDeleteButton;
    private Button deleteAccountButton;
    private EditText eUsername, ePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_delete_user);

        eUsername = (EditText) (EditText) findViewById(R.id.usernameDeleteEntry);
        ePassword = (EditText) (EditText) findViewById(R.id.passwordDeleteEntry);
        cancelDeleteButton = (Button) findViewById(R.id.cancelDeleteButton);
        deleteAccountButton = (Button) findViewById(R.id.deleteAccountButton);


        initializeOnClickListeners();
    }

    private void initializeOnClickListeners() {

        deleteAccountButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                Intent myIntent = new Intent(v.getContext(), LoginActivity.class);
                startActivityForResult(myIntent, 0);
                Toast toast = Toast.makeText(getApplicationContext(), "Account Deleted", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.TOP|Gravity.CENTER_HORIZONTAL, 0, 75);
                toast.show();
            }
        });

        cancelDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(v.getContext(), SettingsActivity.class);
                startActivityForResult(myIntent, 0);
            }
        });

    }
}