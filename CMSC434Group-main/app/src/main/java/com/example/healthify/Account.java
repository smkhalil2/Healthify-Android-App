package com.example.healthify;

import org.json.JSONException;
import org.json.JSONObject;

public class Account {

    // Variables
    private String username, password, dateOfBirth, name, gender, fitnessLvl, email;
    private int weight, height;

    // Constructor
    public Account(String username, String password, String dateOfBirth, String name, String gender, String fitnessLvl, String email, int weight, int height) {
        this.username = username;
        this.password = password;
        this.dateOfBirth = dateOfBirth;
        this.name = name;
        this.gender = gender;
        this.fitnessLvl = fitnessLvl;
        this.email = email;
        this.weight = weight;
        this.height = height;
    }

    // Setters
    public boolean setUsername(String newUsername) {
        if (username.equals(newUsername)) {
            return false;
        } else {
            username = newUsername;
            return true;
        }
    }

    public boolean setPassword(String newPassword) {
        if (password.equals(newPassword)) {
            return false;
        } else {
            password = newPassword;
            return true;
        }
    }

    public boolean setDateOfBirth(String newDateOfBirth) {
        if (dateOfBirth.equals(newDateOfBirth)) {
            return false;
        } else {
            password = newDateOfBirth;
            return true;
        }
    }

    public boolean setName(String newName) {
        if (name.equals(newName)) {
            return false;
        } else {
            name = newName;
            return true;
        }
    }

    public boolean setGender(String newGender) {
        if (gender.equals(newGender)) {
            return false;
        } else {
            gender = newGender;
            return true;
        }
    }

    public boolean setFitnessLvl(String newFitnessLvl) {
        if (fitnessLvl.equals(newFitnessLvl)) {
            return false;
        } else {
            fitnessLvl = newFitnessLvl;
            return true;
        }
    }

    public boolean setWeight(int newWeight) {
        if (weight == newWeight) {
            return false;
        } else {
            weight = newWeight;
            return true;
        }
    }

    public boolean setHeight(int newHeight) {
        if (height == newHeight) {
            return false;
        } else {
            height = newHeight;
            return true;
        }
    }

    public boolean setEmail(String newEmail) {
        if (email.equals(newEmail)) {
            return false;
        } else {
            email = newEmail;
            return true;
        }
    }

    // Getters
    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getName() {
        return name;
    }

    public String getGender() {
        return gender;
    }

    public String getFitnessLvl() {
        return fitnessLvl;
    }

    public int getWeight() {
        return weight;
    }

    public int getHeight() {
        return height;
    }

    public String getEmail() {
        return email;
    }

    public JSONObject getJSON() {
        JSONObject account = new JSONObject();

        try {
            account.put("username", username);
            account.put("password", password);
            account.put("dateOfBirth", dateOfBirth);
            account.put("name", name);
            account.put("gender", gender);
            account.put("fitnessLvl", fitnessLvl);
            account.put("email", email);
            account.put("weight", weight);
            account.put("height", height);

        } catch (JSONException e) {
            return null;
        }

        return account;
    }

    public String getJSONString() {
        return (getJSON() != null) ? getJSON().toString() : "" ;
    }

}