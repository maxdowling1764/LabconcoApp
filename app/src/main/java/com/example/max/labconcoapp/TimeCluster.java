package com.example.max.labconcoapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by max on 9/27/17.
 */

public class TimeCluster {
    private String date;
    private String time;
    //private String timeRemaining;
    private int programStep;

    public TimeCluster(String json)
    {
        JSONObject data;
        try {
            data = new JSONObject(json);
            String t = data.getString("time");
            t = t.substring(9,11) + ":" + t.substring(11,13);
            this.time = t;

            String d = data.getString("date");
            d = d.substring(4,6) + "-" + d.substring(6,8) + "-" + d.substring(0,4);
            this.date = d;

            String i = data.getString("programStep");
            this.programStep = Integer.parseInt(i);

        } catch (JSONException e) {
            this.date = "00-00-00";
            this.time = "00:00";
            this.programStep = -1;
            e.printStackTrace();
        }
    }

    public String asString()
    {
        return "Time: " + getTime() + "\nDate: " + getDate() + "\nStep: " + getProgramStep();
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getProgramStep()
    {
        return programStep;
    }

    public void setProgramStep(int programStep)
    {
        this.programStep = programStep;
    }
}
