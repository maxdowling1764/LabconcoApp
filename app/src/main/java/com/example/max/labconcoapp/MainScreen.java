package com.example.max.labconcoapp;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainScreen extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        VacuumView.OnFragmentInteractionListener,
        TempView.OnFragmentInteractionListener,
        TimingView.OnFragmentInteractionListener,
        GraphView.OnFragmentInteractionListener

{
    public static String jsonDump = "";
    public static String ip = "";
    public static String csvUrl = "";



    private static final int REQUEST_EXTERNAL_STORAGE = 1;
    private static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
    };

    public static boolean firstRun = true;
    public static FreezeDryer fd;

    public DownloadDialogFragment dFrag;
    public IPAddressDialogFragment ipdFrag;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);



        if(firstRun)
        {
            new DownloadCSV(this).execute(csvUrl);
            fd = new FreezeDryer("http://"+ip, "freezeDry1", jsonDump);
            firstRun = false;
        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        verifyStoragePermissions(this);

        //System.out.println("onCreate Called");

    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_screen, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            displayIPDialog();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void fetchJson()
    {
        RequestQueue q = Volley.newRequestQueue(this);
        String url = "http://"+ip+"/dump";

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        MainScreen.jsonDump = response;

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                MainScreen.jsonDump = "error";
            }
        });
        q.add(stringRequest);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        fd.updateJson(jsonDump);

        //System.out.println(csvUrl);

        if (id == R.id.nav_vacuum) {
            Fragment vacFrag = VacuumView.newInstance(fd.getVacuum());
            swapViewFragment(vacFrag);

        } else if (id == R.id.nav_temp) {
            Fragment tempFrag = TempView.newInstance(fd.getTemperatureSensors());
            swapViewFragment(tempFrag);

        } else if (id == R.id.nav_timing) {
            Fragment timeFrag = TimingView.newInstance(fd.getTimeCluster());
            swapViewFragment(timeFrag);

        }
        else if (id == R.id.nav_graph) {
            Fragment graphFrag = GraphView.newInstance(fd.getGraphObjectAtIndex(0));
            swapViewFragment(graphFrag);
            System.err.println("ALL AVAILABLE CSVS");
            for(String s : fd.getAvailableCSVs())
            {
                System.err.println(s);
            }
            ((GraphView) graphFrag).updateCSVSpinner(fd.getAvailableCSVs());
            //updateCSVSpinner();
        }


        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void swapViewFragment(Fragment f)
    {

        FragmentManager fragmentManager = getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.content_frame, f).commit();
        //currentViewID = f.getId();
    }

    public void changeSeries(int index)
    {
        GraphView graphFrag = (GraphView)getFragmentManager().findFragmentById(R.id.content_frame);
        graphFrag.changeSeries(fd.getGraphObjectAtIndex(index));
        //getFragmentManager().beginTransaction().detach(graphFrag).attach(graphFrag).commit();
    }

    public void changeCSV(String csv)
    {
        csvUrl = "http://"+ip+"/dir/" + csv;
        System.err.println(csvUrl);
        new DownloadCSV(this).execute(csvUrl);

    }

    public void postExecuteFunction()
    {
        GraphView graphFrag = (GraphView)getFragmentManager().findFragmentById(R.id.content_frame);
        graphFrag.updateValSpinner();
        graphFrag.changeSeries(fd.getGraphObjectAtIndex(0));
    }
    public void updateCSVSpinner()
    {
        GraphView graphFrag = (GraphView)getFragmentManager().findFragmentById(R.id.content_frame);
        graphFrag.updateCSVSpinner(fd.getAvailableCSVs());

    }

    public void displayDialog()
    {
        dFrag = new DownloadDialogFragment();
        dFrag.show(getFragmentManager(), "test");
    }

    public void displayIPDialog()
    {
        ipdFrag = new IPAddressDialogFragment();
        ipdFrag.show(getFragmentManager(), "ipselect");
    }

    public void dismissDialog()
    {
        dFrag.dismiss();
    }
    public void dismissIPDialog()
    {
        fetchJson();
        new DownloadCSV(this).execute(csvUrl);
        fd = new FreezeDryer("http://"+ip, "freezeDry1", jsonDump);
    }

    public static void setIp(String ip1)
    {
        ip = ip1;

        fd.setIp(ip);

        csvUrl = "http://"+ ip +"/dir/nightrun.csv";
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }
    public static void verifyStoragePermissions(Activity activity) {
        // Check if we have write permission
        int permission = ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE);

        if (permission != PackageManager.PERMISSION_GRANTED) {
            // We don't have permission so prompt the user
            ActivityCompat.requestPermissions(
                    activity,
                    PERMISSIONS_STORAGE,
                    REQUEST_EXTERNAL_STORAGE
            );
        }
    }
}

