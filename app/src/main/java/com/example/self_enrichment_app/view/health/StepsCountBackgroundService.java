package com.example.self_enrichment_app.view.health;

import android.app.Activity;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.IBinder;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.self_enrichment_app.data.model.HealthEntry;
import com.example.self_enrichment_app.view.MainActivity;
import com.example.self_enrichment_app.viewmodel.HealthEntriesViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
//public class StepsCountBackgroundService {
public class StepsCountBackgroundService extends Service implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor stepsCounter;
    private int previousTotalStepsCount = 0;
    private int totalStepsCount = 0;
    private HealthEntriesViewModel healthEntriesViewModel = new HealthEntriesViewModel();
    private FirebaseAuth mAuth;
    private String userId;
    HealthFragment healthFragment = new HealthFragment();
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    public StepsCountBackgroundService() {
    }
/*
    MyCallback myCallback = null;
    public StepsCountBackgroundService(MyCallback callback){
        this.myCallback = callback;
    }
*/


    @Override
    public void onCreate() {
        super.onCreate();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();
        sensorManager = (SensorManager) getSystemService(this.SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepsCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
            Toast.makeText(this,"Has sensor detected",Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(this,"No sensor detected",Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if (stepsCounter != null){
            sensorManager.registerListener(this, stepsCounter, SensorManager.SENSOR_DELAY_UI);
            Toast.makeText(this,"start sensor...",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "no sensor to start...", Toast.LENGTH_SHORT).show();
        }



        return START_STICKY;
    }
/*
    @Override
    public void onResume() {
        super.onResume();
        if (stepsCounter != null){
            sensorManager.registerListener(this, stepsCounter, SensorManager.SENSOR_DELAY_UI);
            Toast.makeText(this,"start sensor...",Toast.LENGTH_SHORT).show();
        }
        else {
            Toast.makeText(this, "no sensor to start...", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        // sensorManager.unregisterListener(this);
        sensorManager.unregisterListener(this, stepsCounter);
        Toast.makeText(this,"pause sensor.",Toast.LENGTH_SHORT).show();
    }
*/
    @Override
    public void onDestroy() {
        super.onDestroy();
        // sensorManager.unregisterListener(this);
        sensorManager.unregisterListener(this, stepsCounter);
        Toast.makeText(this,"stop sensor.",Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        if (event != null) {
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
            Toast.makeText(this, "Current steps " + currentStepsCount, Toast.LENGTH_SHORT).show();
            Toast.makeText(this, "Saving data", Toast.LENGTH_SHORT).show();
            HealthFragment healthFragment = new HealthFragment();
            healthFragment.changeStepsCount(todayDate(),currentStepsCount);

            //Log.d()
            /*
            if (myCallback != null) {

                myCallback.changeStepsCount(todayDate(), currentStepsCount);
            }*/

            Toast.makeText(this, "Total " + totalStepsCount + "Previous " + previousTotalStepsCount, Toast.LENGTH_SHORT).show();
        }
    }

    public String todayDate(){
        final Calendar today = Calendar.getInstance();
        today.add(Calendar.DATE, 0);
        DateFormat dateFormat = new SimpleDateFormat("dd/M/yyyy");
        String todayDate = dateFormat.format(today.getTime());
        return todayDate;
    }
    private void saveStepsCount(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("healthpref", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putInt("steps", previousTotalStepsCount);
        editor.apply();
    }

    private void loadStepsCount(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("healthpref", Context.MODE_PRIVATE);
        int prevTotalSteps = sharedPreferences.getInt("steps", 0);
        previousTotalStepsCount = prevTotalSteps;
    }

/*
    private void changeStepsCount(String todayDate, int currentStepsCount) {
        //Button BtnCalendar = (Button) get.findViewById(R.id);
        // Value will change to get from Firebase
        Query query = FirebaseFirestore.getInstance().collection("HealthEntries").whereEqualTo("userId",userId).whereEqualTo("date",todayDate);
        // not realtime

        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getDocuments().isEmpty()){
                        Log.d("PROGRESS", "rrr:");
                        HealthEntry newHealthEntry = new HealthEntry(healthFragment.todayDate(), userId, -1, -1, -1,-1,-1,0, 0);
                        healthEntriesViewModel.addHealthEntry(newHealthEntry);
                        //previousTotalStepsCount = totalStepsCount;
                        //saveStepsCount();
                        Toast.makeText(healthFragment.getActivity(),"Now steps " + currentStepsCount,Toast.LENGTH_SHORT).show();
                        //Toast.makeText(getActivity(),"Totalsss " + totalStepsCount + "Previous " + previousTotalStepsCount,Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),"New data",Toast.LENGTH_SHORT).show();
                        //TVStepsCountNum.setText(currentStepsCount);
                        // 100% by default

                        String date = healthFragment.BtnCalendar.getText().toString();
                        if (date.equals(healthFragment.todayDate())){
                            healthFragment.setEmptyText(0);
                            //healthFragment.PBStepsCount.setProgress(100);
                        }
                    }
                    else {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("TAG", "Document Id: " + document.getId() + "==" + document.getData());
                            //int newStepsCount = stepsCount + (int) document.get("steps_count");
                            int oldStepsCount = Integer.parseInt(document.get("steps_count").toString());
                            //tempoldcount = oldStepsCount;
                            int currentCount = currentStepsCount + oldStepsCount;
                            //Toast.makeText(healthFragment.getActivity(),currentStepsCount + " + " + oldStepsCount + " = " + currentCount,Toast.LENGTH_SHORT).show();
                            //Toast.makeText(getActivity(),"Totalsss " + totalStepsCount + "Previous " + previousTotalStepsCount,Toast.LENGTH_SHORT).show();
                            healthEntriesViewModel.updateStepsCount(document.getId(), currentCount);
                            //int newStepsCount = stepsCount;
                            //healthFragment
                            String date = getText().toString();
                            if (date.equals(healthFragment.todayDate())){
                                //healthFragment.TVStepsCountNum.setText(Integer.toString(currentCount));
                                double stepscountnum = currentCount;
                                double goalvalue = Integer.parseInt(document.get("steps_goal").toString());
                                if (goalvalue > 0) {
                                    int countprogress = (int) ((stepscountnum / goalvalue) * 100);
                                    if (countprogress >= 100) {
                                        countprogress = 100;
                                        //healthFragment.TVStepsAlertMsg.setVisibility(View.INVISIBLE);
                                        //healthFragment.TVStepsAlertExMark.setVisibility(View.INVISIBLE);
                                        //PBStepsCount.setDrawingCacheBackgroundColor(Color.GREEN);
                                    }
                                    else{
                                        //healthFragment.TVStepsAlertMsg.setVisibility(View.VISIBLE);
                                        //healthFragment.TVStepsAlertExMark.setVisibility(View.VISIBLE);
                                    }
                                    Log.d("PROGRESS", "PERCENTAGE" + countprogress);
                                    //healthFragment.PBStepsCount.setProgress(countprogress);
                                }
                                else {
                                    //healthFragment.TVStepsAlertMsg.setVisibility(View.INVISIBLE);
                                    //healthFragment.TVStepsAlertExMark.setVisibility(View.INVISIBLE);
                                    //healthFragment.PBStepsCount.setProgress(100);
                                }
                            }
                        }
                    }
                }
            }
        });
    }
*/
/*
    public interface MyCallback {
    // Declaration of the template function for the interface
        public void changeStepsCount(String todayDate, int currentStepsCount);
    }*/
}
