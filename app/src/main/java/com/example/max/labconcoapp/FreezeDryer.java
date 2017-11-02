package com.example.max.labconcoapp;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

/**
 * Created by max on 9/27/17.
 */

public class FreezeDryer {
    private String url;
    private String name;
    private TemperatureSensors temperatureSensors;
    private TimeCluster tc;
    private Vacuum vacuum;
    private ArrayList<GraphObj> gobjArr;

    public FreezeDryer(String url, String name, String json) {

        this.name = name;
        temperatureSensors = new TemperatureSensors(json);
        tc = new TimeCluster(json);
        vacuum = new Vacuum(json);
        gobjArr = new ArrayList<>();
        gobjArr.add(new GraphObj(9));
        System.out.println("new instance of freezedryer");
        for (int i = 0; i < gobjArr.get(0).getNumTemps(); i++) {
            loadGraphIndex(i);
        }
    }

    public void updateJson(String json)
    {
        temperatureSensors = new TemperatureSensors(json);
        tc = new TimeCluster(json);
        vacuum = new Vacuum(json);
    }

    public TemperatureSensors getTemperatureSensors()
    {
        return temperatureSensors;
    }

    public TimeCluster getTimeCluster()
    {
        return tc;
    }

    public void loadGraphIndex(int index)
    {
        // add 8 to offset the non shelf temps
        gobjArr.add(new GraphObj(index+8));
    }

    public Vacuum getVacuum()
    {
        return vacuum;
    }
    public GraphObj getGraphObjectAtIndex(int index)
    {
        return gobjArr.get(index);
    }

    public ArrayList<GraphObj> getGraphObjectArray()
    {
        return gobjArr;
    }
}
