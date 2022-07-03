package com.example.healthify;

import org.json.JSONException;
import org.json.JSONObject;

public class Food {

    // Variables
    private String foodName, mealTime, date, calories;



    // Constructor
    public Food(String foodName, String mealTime, String date, String calories) {
       this.foodName = foodName;
       this.mealTime = mealTime;
       this.calories = calories;
       this.date = date;

    }

    // Setters
    public boolean setName(String newName) {
        if (foodName.equals(newName)) {
            return false;
        } else {
            foodName = newName;
            return true;
        }
    }

    public boolean setCalories(String newCalories) {
        if (calories.equals(newCalories)) {
            return false;
        } else {
            calories = newCalories;
            return true;
        }
    }

    public boolean setMealtime(String newMealtime) {
        if (newMealtime.equals(newMealtime)) {
            return false;
        } else {
            mealTime = newMealtime;
            return true;
        }
    }

    public boolean setDate(String newDate) {
        if (date.equals(newDate)) {
            return false;
        } else {
            date = newDate;
            return true;
        }
    }


// Getters
    public String getFoodName() {
    return foodName;
}

    public String getMealTime() {
        return mealTime;
    }

    public String getDate() {
        return date;
    }

    public String getCalories() {
        return calories;
    }

    public JSONObject getJSON() {
        JSONObject food = new JSONObject();

        try {
            food.put("foodName", foodName);
            food.put("mealTime", mealTime);
            food.put("date", date);
            food.put("calories", calories);

        } catch (JSONException e) {
            return null;
        }

        return food;
    }

    @Override
    public String toString() {
        if (foodName != null && mealTime != null && calories != null && date != null) {
            return date + "— " + foodName + " at " + mealTime + ", " + calories + " Cals";
        } else {
            return "";
        }
    }

    public String toJSONString() {

        JSONObject food = new JSONObject();

        try {
            food.put("foodName", foodName);
            food.put("mealTime", mealTime);
            food.put("date", date);
            food.put("calories", calories);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return food.toString();
    }





}