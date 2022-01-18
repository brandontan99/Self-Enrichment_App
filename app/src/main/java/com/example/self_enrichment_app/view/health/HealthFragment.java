package com.example.self_enrichment_app.view.health;

import android.app.DatePickerDialog;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.self_enrichment_app.view.MainActivity;
import com.example.self_enrichment_app.R;
import com.google.firebase.auth.FirebaseAuth;


import java.util.Calendar;
import java.util.Date;

import soup.neumorphism.NeumorphCardView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthFragment extends Fragment {

    private Button BtnAddEntry, BtnCalendar;
    private TextView TVStepsCountNum, TVGoalValue;
    private TextView TVWeightValue, TVHeightValue, TVBMIValue, TVSysValue, TVDiaValue, TVPulseValue;
    private NeumorphCardView NCVBMI, NCVSys, NCVDia, NCVPulse;
    private ProgressBar PBStepsCount;
    private FirebaseAuth mAuth;
    private String userId;
    //private FirebaseAuth mAuth;


    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    //private static final String ARG_PARAM1 = "param1";
    //private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    //private String mParam1;
    //private String mParam2;

    public HealthFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthFragment newInstance(String param1, String param2) {
        HealthFragment fragment = new HealthFragment();
        Bundle args = new Bundle();
        //args.putString(ARG_PARAM1, param1);
        //args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            //mParam1 = getArguments().getString(ARG_PARAM1);
            //mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setToolbarTitle(R.string.title_health);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_health, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();

        // Choose date
        BtnCalendar = view.findViewById(R.id.BtnCalendar);

        final Calendar currentDate = Calendar.getInstance();
        int currentDayMonth = currentDate.get(Calendar.DAY_OF_MONTH);
        int currentMonth = currentDate.get(Calendar.MONTH) + 1;
        int currentYear = currentDate.get(Calendar.YEAR);
        BtnCalendar.setText(currentDayMonth + "/" + currentMonth + "/" + currentYear);
        BtnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog HealthDatePicker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                BtnCalendar.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, currentYear, currentMonth, currentDayMonth);
                HealthDatePicker.show();
            }
        });
        BtnAddEntry = view.findViewById(R.id.BtnAddEntry);
        View.OnClickListener OCLAddEntry = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("date",BtnCalendar.getText().toString());
                Navigation.findNavController(view).navigate(R.id.destHealthEntryFragment, bundle);
            }
        };
        BtnAddEntry.setOnClickListener(OCLAddEntry);

        // Health Entries
        TVWeightValue = view.findViewById(R.id.TVWeightValue);
        TVHeightValue = view.findViewById(R.id.TVHeightValue);
        TVBMIValue = view.findViewById(R.id.TVBMIValue);
        TVSysValue = view.findViewById(R.id.TVSysValue);
        TVDiaValue = view.findViewById(R.id.TVDiaValue);
        TVPulseValue = view.findViewById(R.id.TVPulseValue);
        NCVBMI = view.findViewById(R.id.NCVBMI);
        NCVSys = view.findViewById(R.id.NCVSys);
        NCVDia = view.findViewById(R.id.NCVDia);
        NCVPulse = view.findViewById(R.id.NCVPulse);
        // Value will change to get from Firebase
        double weightvalue = Double.parseDouble(TVWeightValue.getText().toString());
        double heightvalue = Double.parseDouble(TVHeightValue.getText().toString());
        int sysvalue = Integer.parseInt(TVSysValue.getText().toString());
        int diavalue = Integer.parseInt(TVDiaValue.getText().toString());
        int pulsevalue = Integer.parseInt(TVPulseValue.getText().toString());
        double tempBMI = weightvalue/((heightvalue/100)*(heightvalue/100));
        double BMIValue = roundBMI(tempBMI);
        TVBMIValue.setText(Double.toString(BMIValue));
        // BMI
        if (BMIValue > 24.9){ // too high
            if (BMIValue > 29.9){
                NCVBMI.setBackgroundColor(Color.RED);
            }
            else {
                NCVBMI.setBackgroundColor(Color.YELLOW);
            }
        }
        else if (BMIValue < 18.5) { // too low
            if (BMIValue < 16.0) {
                NCVBMI.setBackgroundColor(Color.RED);
            }
            else {
                NCVBMI.setBackgroundColor(Color.YELLOW);
            }
        }
        else {
            NCVBMI.setBackgroundColor(Color.GREEN);
        }

        // Systolic blood pressure
        if (sysvalue > 120){ // too high
            if (sysvalue > 130){
                NCVSys.setBackgroundColor(Color.RED);
            }
            else {
                NCVSys.setBackgroundColor(Color.YELLOW);
            }
        }
        else if (sysvalue < 100) { // too low
            if (sysvalue < 90) {
                NCVSys.setBackgroundColor(Color.RED);
            }
            else {
                NCVSys.setBackgroundColor(Color.YELLOW);
            }
        }
        else {
            NCVSys.setBackgroundColor(Color.GREEN);
        }

        // Diastolic blood pressure
        if (diavalue > 80){ // too high
            if (diavalue > 90){
                NCVDia.setBackgroundColor(Color.RED);
            }
            else {
                NCVDia.setBackgroundColor(Color.YELLOW);
            }
        }
        else if (diavalue < 70) { // too low
            if (diavalue < 60) {
                NCVDia.setBackgroundColor(Color.RED);
            }
            else {
                NCVDia.setBackgroundColor(Color.YELLOW);
            }
        }
        else {
            NCVDia.setBackgroundColor(Color.GREEN);
        }

        // Blood pulse
        if (pulsevalue > 100 || pulsevalue < 60){ // too high and too low
            NCVPulse.setBackgroundColor(Color.RED);
        }
        else {
            NCVPulse.setBackgroundColor(Color.GREEN);
        }

        // Steps count
        TVStepsCountNum = view.findViewById(R.id.TVStepsCountNum);
        TVGoalValue = view.findViewById(R.id.TVGoalValue);
        PBStepsCount = view.findViewById(R.id.PBStepsCount);
        double stepscountnum = Integer.parseInt(TVStepsCountNum.getText().toString());
        double goalvalue = Integer.parseInt(TVGoalValue.getText().toString());
        int countprogress = (int)(stepscountnum/goalvalue)*100;
        PBStepsCount.setProgress(countprogress);


    }



    private double roundBMI (double BMIValue) {
        double tempRoundBMI = Math.round(BMIValue*10);
        BMIValue = tempRoundBMI/10;
        return BMIValue;
    }

}