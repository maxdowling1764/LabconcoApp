package com.example.max.labconcoapp;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Set;

/**
 * Created by max on 9/27/17.
 */

public class TemperatureSensors
{
    private HashMap<String, String> sensors;

    public TemperatureSensors(String json)
    {
        JSONObject data;

        sensors = new HashMap<>();
        try
        {
            data = new JSONObject(json);
            Iterator<String> keys = data.keys();
            ArrayList<String> tempKeyList = new ArrayList<>();

            while (keys.hasNext())
            {
                String k = keys.next();
                if (k.toLowerCase().contains("temp"))
                {
                    tempKeyList.add(k);
                }
            }

            for (String k : tempKeyList)
            {
                sensors.put(k, data.getString(k));
            }

        } catch (JSONException e)
        {
            e.printStackTrace();
        }
    }

    public String asString()
    {
        String t = "";
        Set<String> keys = sensors.keySet();
        ArrayList<String> keyList = new ArrayList<>(keys);

        Collections.sort(keyList);

        for (String s : keyList)
        {
            //System.out.println(sensors.get(s));
            t = t + s + ": " + sensors.get(s) + "\u00b0C \n";
        }
        return t;
    }
}
