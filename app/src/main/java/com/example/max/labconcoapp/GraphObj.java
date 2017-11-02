package com.example.max.labconcoapp;

import android.os.Environment;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by max on 10/17/17.
 */

public class GraphObj {
    private ArrayList<Double> temps;
    private String title;
    private int numTemps;

    public GraphObj(int index)
    {
        temps = new ArrayList<>();
        try {
            File file = new File(Environment.getExternalStorageDirectory().toString() + "/graph.csv");
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                String[] lineArr = line.split(",");
                System.out.println(line);
                //TODO Change this stupid little thingy that hardcodes the length of the row
                if(lineArr.length > 8)
                {
                    try {
                        if (!lineArr[index].contains("Shelf")) {
                            try {
                                temps.add(Double.parseDouble(lineArr[index]));
                            } catch (NumberFormatException e) {
                                temps.add(0.0);
                            }
                        } else {
                            numTemps = lineArr.length - 8;
                            title = lineArr[index];
                        }
                    }
                    catch (ArrayIndexOutOfBoundsException e)
                    {
                        temps.add(0.0);
                    }
                }

            }
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public double getNumTemps()
    {
        return numTemps;
    }
    public ArrayList<Double> getTemps()
    {
        return temps;
    }
    public String getTitle() {return title; }
}
