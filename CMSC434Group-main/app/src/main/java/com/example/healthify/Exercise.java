package com.example.healthify;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;

public class Exercise {

    private String type, name, date, time, durationHours, durationMinutes;
    private boolean complete;

    public Exercise (String type, String name, String date, String time, String durationHours, String durationMinutes) {
        this.type = type;
        this.name = name;
        this.date = date;
        this.time = time;
        this.durationHours = durationHours;
        this.durationMinutes = durationMinutes;
        this.complete  = false;
    }

    public String getType() {
        return type;
    }

    public String getName() {
        return name;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }

    public String getDurationHours() {
        return durationHours;
    }

    public String getDurationMinutes() {
        return durationMinutes;
    }

    public boolean isComplete() {
        return complete;
    }

    public String getDuration() {
        if (Integer.parseInt(durationHours) == 1) {
            if (Integer.parseInt(durationMinutes) == 1) {
                return durationHours + " hour " + durationMinutes + " minute";
            } else {
                return durationHours + " hour " + durationMinutes + " minutes";
            }
        } else {
            if (Integer.parseInt(durationMinutes) == 1) {
                return durationHours + " hours " + durationMinutes + " minute";
            } else {
                return durationHours + " hours " + durationMinutes + " minutes";
            }
        }
    }

    public String toString() {
        return getDate() + ", " + getTime() + " — " + getName();
    }

    public void setComplete() {
        complete = true;
    }

    public String toJSONString() {

        JSONObject exercise = new JSONObject();

        try {
            exercise.put("type", type);
            exercise.put("name", name);
            exercise.put("date", date);
            exercise.put("time", time);
            exercise.put("durationHours", durationHours);
            exercise.put("durationMinutes", durationMinutes);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return exercise.toString();
    }

    public Exercise clone() {
        return new Exercise(type, name, date, time, durationHours, durationMinutes);
    }

}
