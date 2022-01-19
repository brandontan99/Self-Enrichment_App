package com.example.self_enrichment_app.view.health;

import android.app.DatePickerDialog;
import android.content.Context;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.self_enrichment_app.data.model.HealthEntry;
import com.example.self_enrichment_app.data.model.MainGoals;
import com.example.self_enrichment_app.view.MainActivity;
import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.view.goals.MainGoalsAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.Calendar;
import java.util.Date;
import java.util.Random;

import soup.neumorphism.NeumorphCardView;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthFragment extends Fragment implements SensorEventListener {

    private Button BtnAddEntry, BtnCalendar;
    private TextView TVStepsCountNum, TVGoalValue;
    private TextView TVWeightValue, TVHeightValue, TVBMIValue, TVSysValue, TVDiaValue, TVPulseValue;
    private NeumorphCardView NCVBMI, NCVSys, NCVDia, NCVPulse;
    private ProgressBar PBStepsCount;
    private FirebaseAuth mAuth;
    private String userId;
    private SensorManager sensorManager;
    private Sensor stepsCounter;
    private boolean hasStepsCounter;
    private int stepsCount;
    private String todayDate;


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
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();
        sensorManager = (SensorManager)getActivity().getSystemService(Context.SENSOR_SERVICE);
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepsCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            Toast.makeText(getActivity(),"Has sensor detected",Toast.LENGTH_SHORT).show();
            hasStepsCounter = true;
        }
        else{
            // no steps counter , plan come out with toast message
            Toast.makeText(getActivity(),"No sensor detected",Toast.LENGTH_SHORT).show();
            hasStepsCounter = false;
        }

        // Choose date
        BtnCalendar = view.findViewById(R.id.BtnCalendar);
        final Calendar currentDate = Calendar.getInstance();
        int currentDayMonth = currentDate.get(Calendar.DAY_OF_MONTH);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentYear = currentDate.get(Calendar.YEAR);
        todayDate = currentDayMonth + "/" + (currentMonth + 1) + "/" + currentYear;
        BtnCalendar.setText(currentDayMonth + "/" + (currentMonth + 1) + "/" + currentYear);
        searchEntry(BtnCalendar.getText().toString());
        BtnCalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog HealthDatePicker = new DatePickerDialog(getActivity(),
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                BtnCalendar.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                                searchEntry(BtnCalendar.getText().toString());
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

        // Health entries find view
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
        // Steps count find view
        TVStepsCountNum = view.findViewById(R.id.TVStepsCountNum);
        TVGoalValue = view.findViewById(R.id.TVGoalValue);
        PBStepsCount = view.findViewById(R.id.PBStepsCount);
        /*
        //realtime
        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange documentChange : documentSnapshots.getDocumentChanges())
                {
                    String   isAttendance =  documentChange.getDocument().getData().get("weight").toString();
                    String  isCalender   =  documentChange.getDocument().getData().get("height").toString();
                    String isEnablelocation = documentChange.getDocument().getData().get("pulse").toString();
                    Log.d("TEST",isAttendance + "=====");
                }
            }
        */
    }



    private double roundBMI (double BMIValue) {
        double tempRoundBMI = Math.round(BMIValue*10);
        BMIValue = tempRoundBMI/10;
        return BMIValue;
    }

    private void searchEntry(String newDate) {
        // Value will change to get from Firebase
        Query query = FirebaseFirestore.getInstance().collection("HealthEntries").whereEqualTo("userId",userId).whereEqualTo("date",newDate);
        // not realtime

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // dont know how to put empty entry
                        if (document.exists()) {
                            Log.d("TAG", "Document Id: " + document.getId() + "==" + document.getData());

                            TVWeightValue.setText(document.get("weight").toString());
                            TVHeightValue.setText(document.get("height").toString());
                            TVSysValue.setText(document.get("sys").toString());
                            TVDiaValue.setText(document.get("dia").toString());
                            TVPulseValue.setText(document.get("pulse").toString());
                            TVGoalValue.setText(document.get("steps_goal").toString());
                            TVStepsCountNum.setText(document.get("steps_count").toString());
                            double weightvalue = Double.parseDouble(TVWeightValue.getText().toString());
                            double heightvalue = Double.parseDouble(TVHeightValue.getText().toString());
                            double tempBMI = weightvalue/((heightvalue/100)*(heightvalue/100));
                            double BMIValue = roundBMI(tempBMI);
                            int sysvalue = Integer.parseInt(document.get("sys").toString());
                            int diavalue = Integer.parseInt(document.get("dia").toString());
                            int pulsevalue = Integer.parseInt(document.get("pulse").toString());
                            double stepscountnum = Integer.parseInt(document.get("steps_count").toString());
                            double goalvalue = Integer.parseInt(document.get("steps_goal").toString());
                            int countprogress = (int)((stepscountnum/goalvalue)*100);
                            if (countprogress >= 100) {
                                countprogress = 100;
                                //PBStepsCount.setDrawingCacheBackgroundColor(Color.GREEN);
                            }
                            Log.d("PROGRESS","PERCENTAGE" + countprogress);
                            PBStepsCount.setProgress(countprogress);

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
                        }
                        else{
                            Log.d("er", "nmo daetge");
                        }


                    }
                }
            }
        });
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event.sensor == stepsCounter){
            //event.values[0] = generateStepsCount();
            Toast.makeText(getActivity(),event.values[0] + " steps",Toast.LENGTH_SHORT).show();
            stepsCount = (int) event.values[0];
            //event.values[0] = 0;
            Log.d("Steps","Num of steps: " + stepsCount);
            Toast.makeText(getActivity(),"Saving data",Toast.LENGTH_SHORT).show();
            changeStepsCount(todayDate,stepsCount);
            Toast.makeText(getActivity(),"Saved",Toast.LENGTH_SHORT).show();
        }
    }

    // https://www.youtube.com/watch?v=NnvJylicKvE&ab_channel=SarthiTechnology

    private void changeStepsCount(String todayDate, int stepsCount) {
        // Value will change to get from Firebase
        Query query = FirebaseFirestore.getInstance().collection("HealthEntries").whereEqualTo("userId",userId).whereEqualTo("date",todayDate);
        // not realtime

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {

                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        // dont know how to put empty entry
                        if (document.exists()) {
                            Log.d("TAG", "Document Id: " + document.getId() + "==" + document.getData());
                            int newStepsCount = stepsCount + (int) document.get("steps_count");
                            TVStepsCountNum.setText(newStepsCount);
                            double stepscountnum = newStepsCount;
                            double goalvalue = Integer.parseInt(document.get("steps_goal").toString());
                            int countprogress = (int)((stepscountnum/goalvalue)*100);
                            if (countprogress >= 100) {
                                countprogress = 100;
                                //PBStepsCount.setDrawingCacheBackgroundColor(Color.GREEN);
                            }
                            Log.d("PROGRESS","PERCENTAGE" + countprogress);
                            PBStepsCount.setProgress(countprogress);
                        }
                        else{
                            //Log.d("er", "nmo daetge");
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onResume() {
        super.onResume();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            sensorManager.registerListener(this, stepsCounter, SensorManager.SENSOR_DELAY_UI);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)!=null){
            sensorManager.unregisterListener(this, stepsCounter);
        }
    }
    // https://www.youtube.com/watch?v=WSx2a99kPY4&t=140s&ab_channel=CodePalace
    public float generateStepsCount(){
        float minVal = 1.0f;
        float maxVal = 9999.0f;

        Random rand = new Random();

        return rand.nextFloat() * (maxVal - minVal) + minVal;
    }
}