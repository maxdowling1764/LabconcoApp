package com.example.max.labconcoapp;

import android.content.Context;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link VacuumView.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link VacuumView#newInstance} factory method to
 * create an instance of this fragment.
 */
public class VacuumView extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private OnFragmentInteractionListener mListener;

    public VacuumView() {
        // Required empty public constructor
    }

    public static VacuumView newInstance(Vacuum vac) {
        VacuumView fragment = new VacuumView();
        Bundle args = new Bundle();
        args.putString("vac", vac.displayState());
        args.putString("num", vac.displayVal());
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vView = inflater.inflate(R.layout.fragment_vacuum_view, container, false);
        TextView tView = (TextView) vView.findViewById(R.id.vactext);
        TextView vnumView = (TextView) vView.findViewById(R.id.vacnum);
        String vacState = getArguments().getString("vac");
        tView.setText(vacState);

        if(vacState.contains("High"))
        {
            System.out.println("REDDDD");
            tView.setTextColor(Color.parseColor("#f44242"));
        }

        vnumView.setText(getArguments().getString("num"));
        return vView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
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
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
