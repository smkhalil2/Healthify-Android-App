package com.example.healthify;

import org.json.JSONException;
import org.json.JSONObject;

public class Goal {

    private String type, chart, date, goalInformation;

    public Goal (String type, String chart, String date, String goalInformation) {
        this.type = type;
        this.chart = chart;
        this.date = date;
        this.goalInformation = goalInformation;
    }

    public String getType() {return type; }

    public String getChart() {return chart; }

    public String getName() {
        return goalInformation;
    }

    public String getDate() {
        return date;
    }

    public String toString() {
        return date + " — " + goalInformation;
    }

    public String toJSONString() {

        JSONObject exercise = new JSONObject();

        try {
            exercise.put("type", type);
            exercise.put("chart", chart);
            exercise.put("goalInformation", goalInformation);
            exercise.put("date", date);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        return exercise.toString();
    }



}
