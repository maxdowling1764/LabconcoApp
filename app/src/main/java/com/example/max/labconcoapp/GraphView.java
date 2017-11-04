package com.example.max.labconcoapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.androidplot.xy.LineAndPointFormatter;
import com.androidplot.xy.SimpleXYSeries;
import com.androidplot.xy.XYPlot;
import com.androidplot.xy.XYSeries;

import java.util.ArrayList;
import java.util.Arrays;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link GraphView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link GraphView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class GraphView extends Fragment
{
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private XYPlot plot;
    private ArrayList<Double> gobj;
    public static GraphObj graphObj;
    private OnFragmentInteractionListener mListener;
    private Spinner csvspinner;
    private Spinner valuespinner;

    public GraphView()
    {
        // Required empty public constructor
    }

    // TODO: Rename and change types and number of parameters
    public static GraphView newInstance(GraphObj graphObj2)
    {
        GraphView fragment = new GraphView();
        System.out.println("new Instance of GraphView");
        Bundle args = new Bundle();

        graphObj = graphObj2;
        double[] tempArr = new double[graphObj.getTemps().size()];
        for (int i = 0; i < tempArr.length; i++)
        {
            tempArr[i] = graphObj.getTemps().get(i);
        }
        args.putDoubleArray("tempVals", tempArr);
        args.putString("title", graphObj.getTitle());
        //fragment.gobj = graphObj.getTemps();
        fragment.setArguments(args);

        //fragment.updateCSVSpinner(MainScreen.fd.getAvailableCSVs());
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        if (getArguments() != null)
        {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    public void changeSeries(GraphObj graphObj)
    {
        double[] tempArr = new double[graphObj.getTemps().size()];
        for (int i = 0; i < tempArr.length; i++)
        {
            tempArr[i] = graphObj.getTemps().get(i);
        }

        getArguments().putDoubleArray("tempVals", tempArr);
        getArguments().putString("title", graphObj.getTitle());

        double[] tVals = getArguments().getDoubleArray("tempVals");

        Number[] series1Numbers = new Number[tVals.length];
        for (int i = 0; i < tVals.length; i++)
        {
            //System.out.println(gobj.get(i));
            series1Numbers[i] = tVals[i];
        }


        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers),          // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                "Series1");
        //LineAndPointFormatter series1Format = new LineAndPointFormatter(v.getContext(), R.xml.line_point_formatter_with_labels);

        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.getLinePaint().setStrokeWidth(7.0f);
        series1Format.getLinePaint().setColor(Color.rgb(0, 255, 0));
        series1Format.getFillPaint().setColor(Color.TRANSPARENT);
        series1Format.getVertexPaint().setColor(Color.rgb(0, 255, 0));
        series1Format.getVertexPaint().setStrokeWidth(0.0f);
        // add a new series' to the xyplot:
        plot.clear();
        plot.addSeries(series1, series1Format);
        plot.setTitle(getArguments().getString("title"));

        plot.redraw();
        //fragment.gobj = graphObj.getTemps();

    }

    public void updateCSVSpinner(ArrayList<String> vals)
    {
        if (getContext() != null)
        {
            ArrayAdapter<String> csvAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, vals);
            csvAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            csvspinner.setAdapter(csvAdapter);
        }
    }

    public void updateValSpinner()
    {
        ArrayList<String> vals = new ArrayList<>();
        for (int i = 0; i < MainScreen.fd.getGobjArrSize(); i++)
        {
            String t = MainScreen.fd.getGraphObjectAtIndex(i).getTitle();
            if (t != null)
            {
                vals.add(MainScreen.fd.getGraphObjectAtIndex(i).getTitle());
            } else
            {
                vals.add("Not Available");
            }

            //vals.add(MainScreen.fd.getGraphObjectAtIndex(i).getTitle());
        }
        ArrayAdapter<String> valueAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, vals);
        valueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valuespinner.setAdapter(valueAdapter);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment

        final View v = inflater.inflate(R.layout.fragment_graph_view, container, false);

        plot = v.findViewById(R.id.plot);
        csvspinner = (Spinner) v.findViewById(R.id.csv_spinner);
        valuespinner = (Spinner) v.findViewById(R.id.value_spinner);

        ArrayList<String> vals = new ArrayList<>();
        ArrayList<String> csvs = new ArrayList<>();


        for (int i = 0; i < MainScreen.fd.getGobjArrSize(); i++)
        {
            String t = MainScreen.fd.getGraphObjectAtIndex(i).getTitle();
            if (t != null)
            {
                vals.add(MainScreen.fd.getGraphObjectAtIndex(i).getTitle());
            } else
            {
                vals.add("Not Available");
            }

            //vals.add(MainScreen.fd.getGraphObjectAtIndex(i).getTitle());
        }

        for (int i = 0; i < MainScreen.fd.getAvailableCSVsLength(); i++)
        {
            String t = MainScreen.fd.getCSV(i);
            if (t != null)
            {
                csvs.add(t);
            } else
            {
                csvs.add("Not Available");
            }
        }
        //Possibly use context from v

        ArrayAdapter<String> csvAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, csvs);

        ArrayAdapter<String> valueAdapter = new ArrayAdapter<String>(getContext(), android.R.layout.simple_spinner_dropdown_item, vals);

        csvAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        valueAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        valuespinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                ((MainScreen) v.getContext()).changeSeries(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        csvspinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener()
        {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l)
            {

                ((MainScreen) v.getContext()).changeCSV(csvspinner.getSelectedItem().toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView)
            {

            }
        });

        csvspinner.setAdapter(csvAdapter);
        valuespinner.setAdapter(valueAdapter);

        double[] tVals = getArguments().getDoubleArray("tempVals");

        Number[] series1Numbers = new Number[tVals.length];
        for (int i = 0; i < tVals.length; i++)
        {
            //System.out.println(gobj.get(i));
            series1Numbers[i] = tVals[i];
        }


        XYSeries series1 = new SimpleXYSeries(
                Arrays.asList(series1Numbers),          // SimpleXYSeries takes a List so turn our array into a List
                SimpleXYSeries.ArrayFormat.Y_VALS_ONLY, // Y_VALS_ONLY means use the element index as the x value
                "Series1");
        //LineAndPointFormatter series1Format = new LineAndPointFormatter(v.getContext(), R.xml.line_point_formatter_with_labels);

        LineAndPointFormatter series1Format = new LineAndPointFormatter();
        series1Format.getLinePaint().setStrokeWidth(7.0f);
        series1Format.getLinePaint().setColor(Color.rgb(0, 255, 0));
        series1Format.getFillPaint().setColor(Color.TRANSPARENT);
        series1Format.getVertexPaint().setColor(Color.rgb(0, 255, 0));
        series1Format.getVertexPaint().setStrokeWidth(0.0f);
        // add a new series' to the xyplot:
        plot.addSeries(series1, series1Format);
        plot.setTitle(getArguments().getString("title"));
        plot.redraw();

        return v;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri)
    {
        if (mListener != null)
        {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener)
        {
            mListener = (OnFragmentInteractionListener) context;
        } else
        {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach()
    {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener
    {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
