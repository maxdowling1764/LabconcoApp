package com.example.max.labconcoapp;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Environment;
import android.util.Log;

import com.example.max.labconcoapp.MainScreen;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;

class DownloadCSV extends AsyncTask<String, String, String> {

    /**
     * Before starting background thread
     * */

    MainScreen a;
    public DownloadCSV(MainScreen a)
    {
        this.a = a;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        System.err.println("PreExecute");
        a.displayDialog();
    }

    /**
     * Downloading file in background thread
     * */
    @Override
    protected String doInBackground(String... f_url) {
        int count;
        System.err.println("DOWNLOADING.......");
        try {
            String root = Environment.getExternalStorageDirectory().toString();

            URL url = new URL(f_url[0]);



                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lengthOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file

                //System.out.println("DDDD");
                OutputStream output = new FileOutputStream(root + "/" + url.getFile().split("/")[2]);
                //System.out.println("EEEE");
                byte data[] = new byte[2048];

                long total = 0;
                while ((count = input.read(data)) != -1) {
                    total += count;

                    // writing data to file
                    //System.out.println(count);
                    output.write(data, 0, count);

                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();


        } catch (Exception e) {
            Log.e("Error: ", e.getMessage());
        }


        return null;
    }



    /**
     * After completing background task
     * **/
    @Override
    protected void onPostExecute(String file_url) {
        System.err.println("Dismissed Dialog");
        if((GraphView)a.getFragmentManager().findFragmentById(R.id.content_frame) != null) {
            MainScreen.fd = new FreezeDryer("http://" + MainScreen.ip, "freezeDry1", MainScreen.jsonDump);
            MainScreen.fd.fetchCSVList();
            a.postExecuteFunction();
        }
        a.dismissDialog();
    }

}