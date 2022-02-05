package com.example.self_enrichment_app.view.health;

import android.Manifest;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
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
import com.example.self_enrichment_app.view.MainActivity;
import com.example.self_enrichment_app.R;
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
import soup.neumorphism.NeumorphCardView;

public class HealthFragment extends Fragment implements SensorEventListener {
    public static boolean[][] healthStatus = {{false,false,false},{false,false,false},{false,false,false}};
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
    // notification

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
        }


        sensorManager = (SensorManager)getActivity().getSystemService(getActivity().SENSOR_SERVICE);

        if (sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER) != null) {
            stepsCounter = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER);
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
        PBStepsCount.setProgress(0);

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
        createNotificationChannel();
        // Choose date
        BtnCalendar = view.findViewById(R.id.BtnCalendar);
        final Calendar currentDate = Calendar.getInstance();
        int currentDayMonth = currentDate.get(Calendar.DAY_OF_MONTH);
        int currentMonth = currentDate.get(Calendar.MONTH);
        int currentYear = currentDate.get(Calendar.YEAR);
        todayDate = currentDayMonth + "/" + (currentMonth + 1) + "/" + currentYear;
        Log.d("TEXT","Today:" + todayDate);
        todayDate = todayDate();
        Log.d("TEXTNEW","Today:" + todayDate);
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
                                PBStepsCount.setProgress(0);
                                searchEntry(BtnCalendar.getText().toString());
                            }
                        }, currentYear, currentMonth, currentDayMonth);
                HealthDatePicker.show();
            }
        });
        checkHealthNotification(new HealthNotificationCallback() {
            @Override
            public void HealthNotificationCallback(boolean[][] healthStatus) {
                Log.d("TEXT","Today:" + healthStatus[0][0] + healthStatus[0][1] + healthStatus[0][2]);
                Log.d("TEXT","Yesterday:" + healthStatus[1][0] + healthStatus[1][1] + healthStatus[1][2]);
                Log.d("TEXT","Dayb4yesterday:" + healthStatus[2][0] + healthStatus[2][1] + healthStatus[2][2]);
                sendHealthNotification(healthStatus);
            }
        });

        checkYesterdayStepsCount();

    }

    private double roundBMI (double BMIValue) {
        double tempRoundBMI = Math.round(BMIValue*10);
        BMIValue = tempRoundBMI/10;
        return BMIValue;
    }

    public void searchEntry(String newDate) {
        // Value will change to get from Firebase
        PBStepsCount.setProgress(0);
        Query query = FirebaseFirestore.getInstance().collection("HealthEntries").whereEqualTo("userId",userId).whereEqualTo("date",newDate);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getDocuments().isEmpty()){
                        Log.d("TAG", "No data");
                        setEmptyText(0);
                        TVStepsAlertMsg.setVisibility(View.INVISIBLE);
                        TVStepsAlertExMark.setVisibility(View.INVISIBLE);
                        PBStepsCount.setProgress(0);
                        NCVBMI.setBackgroundColor(Color.WHITE);
                        NCVSys.setBackgroundColor(Color.WHITE);
                        NCVDia.setBackgroundColor(Color.WHITE);
                        NCVPulse.setBackgroundColor(Color.WHITE);
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

                                    Log.d("PROGRESS", "PERCENTAGE" + countprogress);
                                    if (countprogress >= 100) {
                                        TVStepsAlertMsg.setVisibility(View.INVISIBLE);
                                        TVStepsAlertExMark.setVisibility(View.INVISIBLE);
                                        PBStepsCount.setProgress(100);
                                    }
                                    else if (countprogress == 0){
                                        PBStepsCount.setProgress(0);
                                        TVStepsAlertMsg.setVisibility(View.VISIBLE);
                                        TVStepsAlertExMark.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        PBStepsCount.setProgress(countprogress);
                                        TVStepsAlertMsg.setVisibility(View.VISIBLE);
                                        TVStepsAlertExMark.setVisibility(View.VISIBLE);
                                    }
                                }
                                else {
                                    TVStepsAlertMsg.setVisibility(View.INVISIBLE);
                                    TVStepsAlertExMark.setVisibility(View.INVISIBLE);
                                    PBStepsCount.setProgress(0);
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
                                PBStepsCount.setProgress(0);
                                NCVBMI.setBackgroundColor(Color.WHITE);
                                NCVSys.setBackgroundColor(Color.WHITE);
                                NCVDia.setBackgroundColor(Color.WHITE);
                                NCVPulse.setBackgroundColor(Color.WHITE);
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
        }
        else {
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        sensorManager.unregisterListener(this, stepsCounter);
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
            changeStepsCount(todayDate, currentStepsCount);
        }
    }

    private void changeStepsCount(String todayDate, int currentStepsCount) {
        // Value will change to get from Firebase
        Query query = FirebaseFirestore.getInstance().collection("HealthEntries").whereEqualTo("userId",userId).whereEqualTo("date",todayDate);
        query.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getDocuments().isEmpty()){
                        HealthEntry newHealthEntry = new HealthEntry(todayDate, userId, -1, -1, -1,-1,-1,0, 0);
                        healthEntriesViewModel.addHealthEntry(newHealthEntry);
                        String date = BtnCalendar.getText().toString();
                        if (date.equals(todayDate())){
                            setEmptyText(0);
                            PBStepsCount.setProgress(0);
                        }
                    }
                    else {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("TAG", "Document Id: " + document.getId() + "==" + document.getData());
                            int oldStepsCount = Integer.parseInt(document.get("steps_count").toString());
                            int currentCount = currentStepsCount + oldStepsCount;
                            healthEntriesViewModel.updateStepsCount(document.getId(), currentCount);
                            String date = BtnCalendar.getText().toString();
                            if (date.equals(todayDate())){
                                TVStepsCountNum.setText(Integer.toString(currentCount));
                                double stepscountnum = currentCount;
                                double goalvalue = Integer.parseInt(document.get("steps_goal").toString());
                                if (goalvalue > 0) {
                                    int countprogress = (int) ((stepscountnum / goalvalue) * 100);
                                    if (countprogress >= 100) {
                                        Log.d("PROGRESS", "PERCENTAGE" + countprogress);
                                        PBStepsCount.setProgress(100);
                                        TVStepsAlertMsg.setVisibility(View.INVISIBLE);
                                        TVStepsAlertExMark.setVisibility(View.INVISIBLE);
                                    }
                                    else if (countprogress == 0){
                                        PBStepsCount.setProgress(0);
                                        TVStepsAlertMsg.setVisibility(View.VISIBLE);
                                        TVStepsAlertExMark.setVisibility(View.VISIBLE);
                                    }
                                    else{
                                        Log.d("PROGRESS", "PERCENTAGE" + countprogress);
                                        PBStepsCount.setProgress(countprogress);
                                        TVStepsAlertMsg.setVisibility(View.VISIBLE);
                                        TVStepsAlertExMark.setVisibility(View.VISIBLE);
                                    }

                                }
                                else {
                                    TVStepsAlertMsg.setVisibility(View.INVISIBLE);
                                    TVStepsAlertExMark.setVisibility(View.INVISIBLE);
                                    PBStepsCount.setProgress(0);
                                }
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

    private void checkHealthNotification(HealthNotificationCallback HealthNotificationCallback) {
        Query todayQuery = FirebaseFirestore.getInstance().collection("HealthEntries")
                .whereEqualTo("userId",userId).whereEqualTo("date",todayDate());
        todayQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> todayTask) {
                if (todayTask.isSuccessful()) {
                    if (!todayTask.getResult().getDocuments().isEmpty()){
                        for (QueryDocumentSnapshot todayDocument : todayTask.getResult()) {
                            if (Integer.parseInt(todayDocument.get("weight").toString()) > -1){
                                healthStatus(todayDocument, healthStatus, 0);
                            }
                            Query yesterdayQuery = FirebaseFirestore.getInstance().collection("HealthEntries")
                                    .whereEqualTo("userId",userId).whereEqualTo("date",yesterdayDate());
                            yesterdayQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<QuerySnapshot> yesterdayTask) {
                                    if (yesterdayTask.isSuccessful()) {
                                        if (!yesterdayTask.getResult().getDocuments().isEmpty()){
                                            for (QueryDocumentSnapshot yesterdayDocument : yesterdayTask.getResult()) {
                                                if (Integer.parseInt(yesterdayDocument.get("weight").toString()) > -1){
                                                    healthStatus(yesterdayDocument, healthStatus, 1);
                                                    Query dayBeforeYesterdayQuery = FirebaseFirestore.getInstance().collection("HealthEntries")
                                                            .whereEqualTo("userId",userId).whereEqualTo("date",dayBeforeYesterdayDate());
                                                    dayBeforeYesterdayQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                                        @Override
                                                        public void onComplete(@NonNull Task<QuerySnapshot> dayBeforeYesterdayTask) {
                                                            if (dayBeforeYesterdayTask.isSuccessful()) {
                                                                if (!dayBeforeYesterdayTask.getResult().getDocuments().isEmpty()){
                                                                    for (QueryDocumentSnapshot dayBeforeYesterdayDocument : dayBeforeYesterdayTask.getResult()) {
                                                                        if (Integer.parseInt(dayBeforeYesterdayDocument.get("weight").toString()) > -1){
                                                                            healthStatus(dayBeforeYesterdayDocument, healthStatus, 2);
                                                                            HealthNotificationCallback.HealthNotificationCallback(healthStatus);
                                                                        }
                                                                    }
                                                                }
                                                            }
                                                        }
                                                    });
                                                }
                                            }
                                        }
                                    }
                                }
                            });
                        }
                    }
                }
            }
        });
    }

    private void healthStatus(QueryDocumentSnapshot queryDocumentSnapshot, boolean[][] healthStatus, int row_index){
        double weight = Integer.parseInt(queryDocumentSnapshot.get("weight").toString());
        double height = Integer.parseInt(queryDocumentSnapshot.get("height").toString());
        double BMI = roundBMI(weight/((height/100)*(height/100)));
        int sys = Integer.parseInt(queryDocumentSnapshot.get("sys").toString());
        int dia = Integer.parseInt(queryDocumentSnapshot.get("dia").toString());
        int pulse = Integer.parseInt(queryDocumentSnapshot.get("pulse").toString());
        // put alert for very critical value (RED INDICATOR)
        if (BMI > 29.9 || BMI < 16.0) {
            healthStatus[row_index][0] = true;
        }
        if (sys > 130 || sys < 90) {
            healthStatus[row_index][1] = true;
        }
        if (dia > 90 || dia < 60) {
            healthStatus[row_index][2] = true;
        }
        if (pulse > 100 || pulse < 60){ // too high and too low
            healthStatus[row_index][2] = true;
        }
    }

    private void sendHealthNotification(boolean[][] healthStatus){
        if (healthStatus[0][0] && healthStatus[1][0] && healthStatus[2][0]) {
            healthBMINotification();
        }

        if (healthStatus[0][1] && healthStatus[1][1] && healthStatus[2][1]) {
            healthBloodPressureNotification();
        }

        if (healthStatus[0][2] && healthStatus[1][2] && healthStatus[2][2]) {
            healthBloodPulseNotification();
        }
    }

    private void checkYesterdayStepsCount() {
        Query yesterdayQuery = FirebaseFirestore.getInstance().collection("HealthEntries")
                .whereEqualTo("userId",userId).whereEqualTo("date",yesterdayDate());
        yesterdayQuery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> yesterdayTask) {
                if (yesterdayTask.isSuccessful()) {
                    if (!yesterdayTask.getResult().getDocuments().isEmpty()){
                        for (QueryDocumentSnapshot yesterdayDocument : yesterdayTask.getResult()) {
                            double yesterdayCount = Integer.parseInt(yesterdayDocument.get("steps_count").toString());
                            double yesterdayGoalValue = Integer.parseInt(yesterdayDocument.get("steps_goal").toString());
                            if (yesterdayGoalValue > 0) {
                                int countprogress = (int) ((yesterdayCount / yesterdayGoalValue) * 100);
                                if (countprogress < 100) {
                                    healthstepsNotification();
                                }
                            }
                        }
                    }
                }
            }
        });
    }

    private String todayDate(){
        final Calendar today = Calendar.getInstance();
        today.add(Calendar.DATE, 0);
        DateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
        String todayDate = dateFormat.format(today.getTime());
        return todayDate;
    }

    private String yesterdayDate(){
        final Calendar yesterday = Calendar.getInstance();
        yesterday.add(Calendar.DATE, -1);
        DateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
        String yesterdayDate = dateFormat.format(yesterday.getTime());
        return yesterdayDate;
    }

    private String dayBeforeYesterdayDate(){
        final Calendar daybeforeyesterday = Calendar.getInstance();
        daybeforeyesterday.add(Calendar.DATE, -3);
        DateFormat dateFormat = new SimpleDateFormat("d/M/yyyy");
        String daybeforeyesterdayDate = dateFormat.format(daybeforeyesterday.getTime());
        return daybeforeyesterdayDate;
    }

    private void createNotificationChannel(){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            CharSequence name = "HealthChannel";
            String desc = "This is notification channel for health and fitness page";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("healthnotification", name, importance);
            channel.setDescription(desc);
            NotificationManager notificationManager = getActivity().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

    private void healthstepsNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "healthnotification")
                .setSmallIcon(R.drawable.ic_health)
                .setContentTitle("You have not reach your yesterday steps goal")
                .setContentText("Try work harder and clear today's goal.")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
        notificationManagerCompat.notify(1, builder.build());
    }

    private void healthBMINotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "healthnotification")
                .setSmallIcon(R.drawable.ic_health)
                .setContentTitle("Your BMI for these three days are not within normal range")
                .setContentText("Have a better lifestyle to revert your BMI to normal range")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
        notificationManagerCompat.notify(2, builder.build());
    }

    private void healthBloodPressureNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "healthnotification")
                .setSmallIcon(R.drawable.ic_health)
                .setContentTitle("Your blood pressure for these three days are quite abnormal")
                .setContentText("Please being alert as this is dangerous for your health")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
        notificationManagerCompat.notify(3, builder.build());
    }

    private void healthBloodPulseNotification(){
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getActivity(), "healthnotification")
                .setSmallIcon(R.drawable.ic_health)
                .setContentTitle("Your blood pulse for these three days are quite abnormal")
                .setContentText("Please being alert as this is dangerous for your health")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setAutoCancel(true);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(getActivity());
        notificationManagerCompat.notify(4, builder.build());
    }

    public interface HealthNotificationCallback {
        void HealthNotificationCallback(boolean[][] healthStatus);
    }
}
