package com.example.self_enrichment_app.view.health;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;

import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
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
import com.example.self_enrichment_app.viewmodel.HealthEntriesViewModel;
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

public class HealthFragment extends Fragment implements SensorEventListener {

    private Button BtnAddEntry, BtnCalendar;
    private TextView TVStepsCountNum, TVGoalValue, TVStepsAlertExMark, TVStepsAlertMsg;
    private TextView TVWeightValue, TVHeightValue, TVBMIValue, TVSysValue, TVDiaValue, TVPulseValue;
    private NeumorphCardView NCVBMI, NCVSys, NCVDia, NCVPulse;
    private ProgressBar PBStepsCount;
    private FirebaseAuth mAuth;
    private String userId;
    private SensorManager sensorManager;
    private Sensor stepsCounter;
    private HealthEntriesViewModel healthEntriesViewModel;
    private int previousTotalStepsCount = 0;
    private int totalStepsCount = 0;
    private String todayDate;
    private int tempoldcount = 0;

    public HealthFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setToolbarTitle(R.string.title_health);
        getActivity().getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_GRANTED) {
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{Manifest.permission.ACTIVITY_RECOGNITION}, 99);
        }
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACTIVITY_RECOGNITION) == PackageManager.PERMISSION_DENIED) {
            Toast.makeText(getActivity(),"Please allow the motion and fitness permission.",Toast.LENGTH_SHORT).show();
        }
        sensorManager = (SensorManager)getActivity().getSystemService(getActivity().SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepsCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            Toast.makeText(getActivity(),"Has sensor detected",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity(),"No sensor detected",Toast.LENGTH_SHORT).show();
        }
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_health, container, false);
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();
        healthEntriesViewModel = new HealthEntriesViewModel();
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
        TVStepsAlertExMark = view.findViewById(R.id.TVStepsAlertExMark);
        TVStepsAlertMsg = view.findViewById(R.id.TVStepsAlertMsg);
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
                    if (task.getResult().getDocuments().isEmpty()){
                        Log.d("TAG", "No data");
                        //HealthEntry newHealthEntry = new HealthEntry(todayDate, userId, -1, -1, -1,-1,-1,0, 0);
                        //healthEntriesViewModel.addHealthEntry(newHealthEntry);
                        setEmptyText(0);
                        Toast.makeText(getActivity(),"Empty data",Toast.LENGTH_SHORT).show();
                        TVStepsAlertMsg.setVisibility(View.INVISIBLE);
                        TVStepsAlertExMark.setVisibility(View.INVISIBLE);
                        PBStepsCount.setProgress(100);
                    }
                    else{
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("TAG", "Document Id: " + document.getId() + "==" + document.getData());
                            if (Integer.parseInt(document.get("weight").toString()) > -1) {
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

                                if (goalvalue > 0) {
                                    int countprogress = (int) ((stepscountnum / goalvalue) * 100);
                                    if (countprogress >= 100) {
                                        countprogress = 100;
                                        TVStepsAlertMsg.setVisibility(View.INVISIBLE);
                                        TVStepsAlertExMark.setVisibility(View.INVISIBLE);
                                        //PBStepsCount.setDrawingCacheBackgroundColor(Color.GREEN);
                                    }
                                    else{
                                        TVStepsAlertMsg.setVisibility(View.VISIBLE);
                                        TVStepsAlertExMark.setVisibility(View.VISIBLE);
                                    }
                                    Log.d("PROGRESS", "PERCENTAGE" + countprogress);
                                    PBStepsCount.setProgress(countprogress);
                                }
                                else {
                                    TVStepsAlertMsg.setVisibility(View.INVISIBLE);
                                    TVStepsAlertExMark.setVisibility(View.INVISIBLE);
                                    PBStepsCount.setProgress(100);
                                }
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
                            else {
                                int stepsCount = Integer.parseInt(document.get("steps_count").toString());
                                setEmptyText(stepsCount);
                                PBStepsCount.setProgress(100);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (stepsCounter != null){
            sensorManager.registerListener(this, stepsCounter, SensorManager.SENSOR_DELAY_UI);
            Toast.makeText(getActivity(),"start sensor...",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(getActivity(), "no sensor to start...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // sensorManager.unregisterListener(this);
        sensorManager.unregisterListener(this, stepsCounter);
        Toast.makeText(getActivity(),"pause sensor.",Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        if (event != null){
            totalStepsCount = (int) event.values[0];
            loadStepsCount();
            if (previousTotalStepsCount == 0 && totalStepsCount != 0) {
                previousTotalStepsCount = totalStepsCount;
                saveStepsCount();
            }
            if (previousTotalStepsCount > totalStepsCount) {
                previousTotalStepsCount = totalStepsCount;
                saveStepsCount();
            }
            int currentStepsCount = totalStepsCount - previousTotalStepsCount;
            previousTotalStepsCount = totalStepsCount;
            saveStepsCount();
            Toast.makeText(getActivity(),"Current steps " + currentStepsCount,Toast.LENGTH_SHORT).show();
            //previousStepsCount = (int) event.values[0];
            //TVStepsCountNum.setText(Integer.toString(stepsCount));
            Toast.makeText(getActivity(),"Saving data",Toast.LENGTH_SHORT).show();
            changeStepsCount(todayDate, currentStepsCount);
            Toast.makeText(getActivity(),"Total " + totalStepsCount + "Previous " + previousTotalStepsCount,Toast.LENGTH_SHORT).show();
        }
    }

    private void changeStepsCount(String todayDate, int currentStepsCount) {
        // Value will change to get from Firebase
        Query query = FirebaseFirestore.getInstance().collection("HealthEntries").whereEqualTo("userId",userId).whereEqualTo("date",todayDate);
        // not realtime
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getDocuments().isEmpty()){
                        HealthEntry newHealthEntry = new HealthEntry(todayDate, userId, -1, -1, -1,-1,-1,0, 0);
                        healthEntriesViewModel.addHealthEntry(newHealthEntry);
                        //previousTotalStepsCount = totalStepsCount;
                        //saveStepsCount();
                        setEmptyText(0);
                        Toast.makeText(getActivity(),"Now steps " + currentStepsCount,Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),"Totalsss " + totalStepsCount + "Previous " + previousTotalStepsCount,Toast.LENGTH_SHORT).show();
                        Toast.makeText(getActivity(),"New data",Toast.LENGTH_SHORT).show();
                        //TVStepsCountNum.setText(currentStepsCount);
                        // 100% by default
                        PBStepsCount.setProgress(100);
                    }
                    else {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("TAG", "Document Id: " + document.getId() + "==" + document.getData());
                            //int newStepsCount = stepsCount + (int) document.get("steps_count");
                            int oldStepsCount = Integer.parseInt(document.get("steps_count").toString());
                            //tempoldcount = oldStepsCount;
                            int currentCount = currentStepsCount + oldStepsCount;
                            Toast.makeText(getActivity(),currentStepsCount + " + " + oldStepsCount + " = " + currentCount,Toast.LENGTH_SHORT).show();
                            Toast.makeText(getActivity(),"Totalsss " + totalStepsCount + "Previous " + previousTotalStepsCount,Toast.LENGTH_SHORT).show();
                            healthEntriesViewModel.updateStepsCount(document.getId(), currentCount);
                            //int newStepsCount = stepsCount;
                            TVStepsCountNum.setText(Integer.toString(currentCount));
                            double stepscountnum = currentCount;
                            double goalvalue = Integer.parseInt(document.get("steps_goal").toString());
                            if (goalvalue > 0) {
                                int countprogress = (int) ((stepscountnum / goalvalue) * 100);
                                if (countprogress >= 100) {
                                    countprogress = 100;
                                    TVStepsAlertMsg.setVisibility(View.INVISIBLE);
                                    TVStepsAlertExMark.setVisibility(View.INVISIBLE);
                                    //PBStepsCount.setDrawingCacheBackgroundColor(Color.GREEN);
                                }
                                else{
                                    TVStepsAlertMsg.setVisibility(View.VISIBLE);
                                    TVStepsAlertExMark.setVisibility(View.VISIBLE);
                                }
                                Log.d("PROGRESS", "PERCENTAGE" + countprogress);
                                PBStepsCount.setProgress(countprogress);
                            }
                            else {
                                TVStepsAlertMsg.setVisibility(View.INVISIBLE);
                                TVStepsAlertExMark.setVisibility(View.INVISIBLE);
                                PBStepsCount.setProgress(100);
                            }
                        }
                    }
                }
            }
        });
    }

    private void setEmptyText(int stepsCount) {
        TVWeightValue.setText("-");
        TVHeightValue.setText("-");
        TVBMIValue.setText("-");
        TVSysValue.setText("-");
        TVDiaValue.setText("-");
        TVPulseValue.setText("-");
        TVStepsCountNum.setText(Integer.toString(stepsCount));
        TVGoalValue.setText("0");
    }

    private void saveStepsCount(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("healthpref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("steps", previousTotalStepsCount);
        editor.apply();
    }

    private void loadStepsCount(){
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("healthpref", Context.MODE_PRIVATE);
        int prevTotalSteps = sharedPreferences.getInt("steps", 0);
        previousTotalStepsCount = prevTotalSteps;
    }
}

// Reference:
// https://www.youtube.com/watch?v=WSx2a99kPY4&t=140s&ab_channel=CodePalace
// https://www.youtube.com/watch?v=NnvJylicKvE&ab_channel=SarthiTechnology
// https://stackoverflow.com/questions/59954588/how-to-implement-step-counter-in-android-studio-if-pedometer-sensor-not-availabl
// https://www.youtube.com/watch?v=YsHHXg1vbcc&ab_channel=CodinginFlow