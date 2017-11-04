package com.example.max.labconcoapp;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

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
    private ArrayList<String> availableCSVs;

    public FreezeDryer(String url, String name, String json) {
        GraphObj.clearMem();
        GraphObj.loadCSV();
        this.name = name;
        this.url = url;
        temperatureSensors = new TemperatureSensors(json);
        tc = new TimeCluster(json);
        vacuum = new Vacuum(json);
        gobjArr = new ArrayList<>();
        availableCSVs = new ArrayList<>();

        gobjArr.add(new GraphObj(8));
        //System.out.println("new instance of freezedryer");
        for (int i = 1; i < gobjArr.get(0).getNumTemps(); i++) {
            loadGraphIndex(i);
        }
        //System.err.println("SIZE: " + gobjArr.size());
    }

    public void fetchCSVList()
    {
//        System.out.println("URL AHHHHHHHH: " + url);
//        Document doc = Jsoup.connect(url).get();
//        Element fileList = doc.getElementById("filelist");
//        Element row = fileList.getElementsByClass("row").first();
//        Elements a = row.getElementsByTag("a");
//        System.err.print("SIZE: " + a.size());
//        for(int i = 0; i < a.size(); i++)
//        {
//            String s = a.get(i).attr("href").split("/")[2];
//            System.err.println(s);
//            availableCSVs.add(s);
//        }

        new ParseHTMLTask(this).execute();

    }

    public String getUrl()
    {
        return url;
    }

    public ArrayList<String> getAvailableCSVs()
    {
        return availableCSVs;
    }

    public int getGobjArrSize()
    {
        return gobjArr.size();
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

    public String getCSV(int index)
    {
        return availableCSVs.get(index);
    }

    public void addAvailableCSV(String s)
    {
        availableCSVs.add(s);
    }

    public int getAvailableCSVsLength()
    {
        return availableCSVs.size();
    }

    public void setIp(String ip)
    {
        this.url = "http://"+ip;
    }
}
