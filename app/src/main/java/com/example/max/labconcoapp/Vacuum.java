package com.example.max.labconcoapp;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by max on 9/27/17.
 */

public class Vacuum
{
    private String state;

    public Vacuum(String json)
    {
        JSONObject data;
        try
        {
            data = new JSONObject(json);
            this.state = data.getString("vacuumLevel");
        } catch (JSONException e)
        {
            e.printStackTrace();
            this.state = "None";
        }

    }

    public String displayState()
    {
        return "Vacuum: \n" + state;
    }

    public String displayVal()
    {
        return "0.0kPa";
    }
}
