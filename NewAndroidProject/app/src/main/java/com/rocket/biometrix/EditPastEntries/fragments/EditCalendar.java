package com.rocket.biometrix.EditPastEntries.fragments;

import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CalendarView;
import android.widget.Toast;

import com.rocket.biometrix.Common.StringDateTimeConverter;
import com.rocket.biometrix.Database.LocalStorageAccess;
import com.rocket.biometrix.Database.LocalStorageAccessExercise;
import com.rocket.biometrix.R;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link EditCalendar.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link EditCalendar#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EditCalendar extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public EditCalendar() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EditCalendar.
     */
    // TODO: Rename and change types and number of parameters
    public static EditCalendar newInstance(String param1, String param2) {
        EditCalendar fragment = new EditCalendar();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
//        return inflater.inflate(R.layout.fragment_exercise_calendar, container, false);
        View view = inflater.inflate(R.layout.fragment_exercise_calendar, container, false);

        //Making reference to calendar view from this fragments view generated by the layout above
        CalendarView cal = (CalendarView) view.findViewById(R.id.ex_CV);

        cal.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {

            @Override
            public void onSelectedDayChange(CalendarView view, int year, int month,
                                            int dayOfMonth) {
                //Months are off by one for some reason, I blame bernie sanders. Or PST pre-sets
                if (month == 12){month = 1;} else {month++;}

                //snipe the day
                String[] dateSelected = { Integer.toString(year),Integer.toString(month),Integer.toString(dayOfMonth)};
                String dateSelectedFormatted = StringDateTimeConverter.convertCalDateString(dateSelected);

                //LocalStorageAccessExercise dbEx = new LocalStorageAccessExercise(getActivity());

                //Pass selectByDate() cursor to fill ListView
                Cursor exercise = LocalStorageAccessExercise.selectByDate(dateSelectedFormatted);


                //getActivity() for the context.
                Toast.makeText(getActivity(), dateSelectedFormatted, Toast.LENGTH_LONG).show();



            }

        });

        return view;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    //When parent activity OnCreate is finished, can access UI elements
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
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
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
