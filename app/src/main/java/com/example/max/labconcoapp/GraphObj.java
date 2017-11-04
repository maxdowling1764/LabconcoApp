package com.example.max.labconcoapp;

import android.os.Environment;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by max on 10/17/17.
 */

public class GraphObj {
    private ArrayList<Double> temps;
    public static ArrayList<String> linesOfFile;
    private String title;
    private int numTemps;

    public static void loadCSV() {
        System.err.println("AT LINE 21 GraphObj: " + MainScreen.csvUrl);

        try {
            String f = Environment.getExternalStorageDirectory().toString() + "/" + MainScreen.csvUrl.split("/")[4];
            System.err.println("WEOOWEOOWEOO: " + f);
            File file = new File(f);
            FileReader fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                linesOfFile.add(line);

            }
            fileReader.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
        catch(IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    public static void clearMem()
    {
        if(linesOfFile != null)
            linesOfFile.clear();
    }

    public GraphObj(int index) {
        if (linesOfFile == null) {
            linesOfFile = new ArrayList<>();
            loadCSV();
        }

        temps = new ArrayList<>();

        for (String line : linesOfFile) {
            String[] lineArr = line.split(",");
            //TODO Change this stupid little thingy that hardcodes the length of the row
            if (lineArr.length > 8) {
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
                } catch (ArrayIndexOutOfBoundsException e) {
                    temps.add(0.0);
                }
            }

        }

    }

    public double getNumTemps() {
        return numTemps;
    }

    public ArrayList<Double> getTemps() {
        return temps;
    }

    public String getTitle() {
        return title;
    }
}
