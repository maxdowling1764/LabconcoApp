package com.example.max.labconcoapp;

import android.os.AsyncTask;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by max on 11/3/17.
 */

public class ParseHTMLTask extends AsyncTask<String, String, String>
{
    FreezeDryer fd;

    public ParseHTMLTask(FreezeDryer fd)
    {
        this.fd = fd;
    }

    @Override
    protected String doInBackground(String... strings)
    {
        System.out.println("URL AHHHHHHHH: " + fd.getUrl());
        Document doc = null;
        try
        {
            doc = Jsoup.connect(fd.getUrl()).get();
        } catch (IOException e)
        {
            e.printStackTrace();
        }
        Element fileList = doc.getElementById("filelist");
        Element row = fileList.getElementsByClass("row").first();
        Elements a = row.getElementsByTag("a");
        System.err.print("SIZE: " + a.size());
        for (int i = 0; i < a.size(); i++)
        {
            String s = a.get(i).attr("href").split("/")[2];
            System.err.println(s);
            fd.addAvailableCSV(s);
        }

        MainScreen.fd = fd;
        return null;
    }
}
