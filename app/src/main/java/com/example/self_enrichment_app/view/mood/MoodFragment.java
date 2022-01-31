package com.example.self_enrichment_app.view.mood;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.NavDeepLinkBuilder;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.self_enrichment_app.data.model.MoodDiaryEntry;
import com.example.self_enrichment_app.view.MainActivity;
import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.view.general.LoginActivity;
import com.example.self_enrichment_app.viewmodel.MoodDiaryViewModel;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.common.net.InternetDomainName;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.protobuf.Value;

import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MoodFragment extends Fragment {
    public static boolean overweight;
    private Calendar myCalendar = Calendar.getInstance();
    private Calendar notificationCalendar;
    private String dateFormat ="dd MMM yyyy";
    private TextView TVDate, TVDiaryEntry;
    private ImageView IVMood;
    private ChipGroup CGMoodDiary;
    private Chip CHIPWork, CHIPFriends, CHIPFamily, CHIPHealth, CHIPFinance, CHIPLove;
    private Button BTNAddDiaryEntry;
    private ImageButton BTNEditDiaryEntry, BTNMoodHappy, BTNMoodSad, BTNMoodAngry, BTNMoodTired;
    private BarChart barChart;
    private NavController navController;
    private FirebaseFirestore firestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private MoodDiaryViewModel moodDiaryViewModel;
    private MoodDiaryEntry displayedEntry;
    private String userId;
    private String currentDate, selectedDate;
    private SimpleDateFormat simpleDateFormat;
    private static boolean notificationCreated;


    public MoodFragment() {
        // Required empty public constructor
    }

    public static MoodFragment newInstance(String param1, String param2) {
        MoodFragment fragment = new MoodFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Date today = Calendar.getInstance().getTime();
        simpleDateFormat = new SimpleDateFormat(this.dateFormat, Locale.getDefault());
        currentDate = simpleDateFormat.format(today);
        Bundle bundle = getArguments();
        if (bundle != null){
            selectedDate = bundle.getString("editingDate");
            Log.d("Steven", "onCreate: " + selectedDate);
        }

        // Creating the notification
        // Notification will check every 9pm
        createNotificationChannel();

        notificationCalendar = Calendar.getInstance();
        notificationCalendar.set(Calendar.HOUR_OF_DAY, 21);
        notificationCalendar.set(Calendar.MINUTE, 0);
        notificationCalendar.set(Calendar.SECOND, 0);

        if (Calendar.getInstance().after(notificationCalendar)){
            notificationCalendar.add(Calendar.DAY_OF_MONTH, 1);
            notificationCreated = false;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setToolbarTitle(R.string.title_mood);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mood, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();
        moodDiaryViewModel = new ViewModelProvider(this).get(MoodDiaryViewModel.class);

        IVMood = view.findViewById(R.id.IVMood);
        CGMoodDiary = view.findViewById(R.id.CGMoodDiary);
        CHIPWork = view.findViewById(R.id.CHIPWork);
        CHIPFriends = view.findViewById(R.id.CHIPFriends);
        CHIPFamily = view.findViewById(R.id.CHIPFamily);
        CHIPHealth = view.findViewById(R.id.CHIPHealth);
        CHIPFinance = view.findViewById(R.id.CHIPFinance);
        CHIPLove = view.findViewById(R.id.CHIPLove);
        TVDiaryEntry = view.findViewById(R.id.TVDiaryEntry);
        TVDate= view.findViewById(R.id.TVDate);
        barChart = view.findViewById(R.id.BCMoodChart);

        // Adding a new entry
        BTNAddDiaryEntry = view.findViewById(R.id.BTNAddDiaryEntry);
        BTNAddDiaryEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editingDate = TVDate.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("editingDate", editingDate);
                navController.navigate(R.id.action_destMood_to_destMoodNewEntry, bundle);
            }
        });

        // Editing an old entry
        BTNEditDiaryEntry = view.findViewById(R.id.BTNEditDiaryEntry);
        BTNEditDiaryEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String editingDate = TVDate.getText().toString();
                Bundle bundle = new Bundle();
                bundle.putString("editingDate", editingDate);
                bundle.putParcelable("displayedEntry", displayedEntry);
                navController.navigate(R.id.action_destMood_to_destMoodEditEntry, bundle);
            }
        });

        // Getting and Setting the initial date
        try {
            if (selectedDate != null){
                Date date = simpleDateFormat.parse(selectedDate);
                myCalendar.setTime(date);
            }
            else{
                Date date = simpleDateFormat.parse(currentDate);
                myCalendar.setTime(date);
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        updateLabel();

        // Creating the calendar picker
        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int day) {
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH,month);
                myCalendar.set(Calendar.DAY_OF_MONTH,day);
                updateLabel();
            }
        };

        // Setting the calendar picker into the TVDate
        TVDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatePickerDialog DPD = new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH));
                DPD.getDatePicker().setMaxDate(System.currentTimeMillis());
                DPD.show();
            }
        });

        // Setting the graph
        updateGraph("happy");

        BTNMoodHappy = view.findViewById(R.id.BTNMoodHappy);
        BTNMoodHappy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                selectMood("happy");
                updateGraph("happy");
            }
        });

        BTNMoodSad = view.findViewById(R.id.BTNMoodSad);
        BTNMoodSad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGraph("sad");
            }
        });

        BTNMoodAngry = view.findViewById(R.id.BTNMoodAngry);
        BTNMoodAngry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGraph("angry");
            }
        });

        BTNMoodTired = view.findViewById(R.id.BTNMoodTired);
        BTNMoodTired.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateGraph("tired");
            }
        });
    }

//    private void selectMood(String mood) {
//        if (mood.equalsIgnoreCase("happy")){
//
//        } else if (mood.equalsIgnoreCase("happy")) {
//
//        } else if (mood.equalsIgnoreCase("happy"))
//    }

    public void updateLabel(){
        String entryDate = simpleDateFormat.format(myCalendar.getTime());
        TVDate.setText(entryDate);
        DocumentReference docRef = firestore.collection("MoodDiaryEntries").document(userId+entryDate);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()){
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()){
                        MoodDiaryEntry diaryEntry = document.toObject(MoodDiaryEntry.class);
                        displayedEntry = diaryEntry;
                        String mood = diaryEntry.getMood();
                        Log.d("Steven", "Mood: " + mood);
                        if (mood.equalsIgnoreCase("happy"))
                            IVMood.setImageResource(R.drawable.ic_mood_happy);
                        else if (mood.equalsIgnoreCase("sad"))
                            IVMood.setImageResource(R.drawable.ic_mood_sad);
                        else if (mood.equalsIgnoreCase("angry"))
                            IVMood.setImageResource(R.drawable.ic_mood_angry);
                        else if (mood.equalsIgnoreCase("tired"))
                            IVMood.setImageResource(R.drawable.ic_mood_tired);

                        List<String> reasons = diaryEntry.getReasons();
                        Log.d("Steven", "Reasons: " + reasons.toString());
                        CGMoodDiary.clearCheck();
                        if (reasons.contains("Work"))
                            CHIPWork.setChecked(true);
                        if (reasons.contains("Friends"))
                            CHIPFriends.setChecked(true);
                        if (reasons.contains("Family"))
                            CHIPFamily.setChecked(true);
                        if (reasons.contains("Health"))
                            CHIPHealth.setChecked(true);
                        if (reasons.contains("Finance"))
                            CHIPFinance.setChecked(true);
                        if (reasons.contains("Love"))
                            CHIPLove.setChecked(true);

                        TVDiaryEntry.setText(diaryEntry.getEntryDescription());
                        Log.d("Steven", "Entry data: " + diaryEntry.toString());

                        // Checking if the current day contains an entry and disabling the
                        // add new entry button if yes
                        BTNAddDiaryEntry.setEnabled(false);
                        BTNAddDiaryEntry.setVisibility(View.GONE);
                    } else {
                        displayedEntry = null;
                        IVMood.setImageResource(R.drawable.ic_mood_sad);
                        CGMoodDiary.clearCheck();
                        TVDiaryEntry.setText("No entry for this date. Add entry?");
                        BTNAddDiaryEntry.setEnabled(true);
                        BTNAddDiaryEntry.setVisibility(View.VISIBLE);
                        Log.d("Steven", "No such document");
                    }
                } else{
                    displayedEntry = null;
                    IVMood.setBackgroundResource(R.drawable.ic_mood_happy);
                    CGMoodDiary.clearCheck();
                    TVDiaryEntry.setText("No entry for this date. Add entry?");
                    Log.d("Steven", "get failed with ", task.getException());
                }
            }
        });

    }

    public void updateGraph(String mood){
        firestore.collection("MoodDiaryEntries").whereEqualTo("createdBy", userId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()){
                    // Getting all the entries
                    ArrayList<MoodDiaryEntry> allEntries = new ArrayList<>();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        allEntries.add(document.toObject(MoodDiaryEntry.class));
                    }
                    MoodStatistics statistics = new MoodStatistics(allEntries);

                    // Creating a notification if the user's been sad for a while
                    if ((statistics.getTotalPositives() < statistics.getTotalEntries()/2) && notificationCreated == false){
                        Intent intent = new Intent(getContext(), MoodAlarmReceiver.class);
                        PendingIntent pendingIntent = PendingIntent.getBroadcast(getContext(),
                                0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

                        AlarmManager alarmManager = (AlarmManager) getActivity()
                                .getSystemService(Context.ALARM_SERVICE);
                        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                                notificationCalendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY
                                , pendingIntent);
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                            alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, notificationCalendar.getTimeInMillis(), pendingIntent);
                        }
                        notificationCreated = true;
                        Log.d("Steven", "New notification created");
                    } else {
                        if (notificationCreated == true)
                            Log.d("Steven","Notification has been created");
                        else
                            Log.d("Steven","User is not depressed, no notification needed");
                    }
                    // Creating the dataset
                    List<BarEntry> entries = new ArrayList<>();
                    if (mood.equalsIgnoreCase("happy")){
                        entries = statistics.getHappyList();
                    } else if (mood.equalsIgnoreCase("sad")){
                        entries = statistics.getSadList();
                    } else if (mood.equalsIgnoreCase("angry")){
                        entries = statistics.getAngryList();
                    } else if (mood.equalsIgnoreCase("tired")){
                        entries = statistics.getTiredList();
                    }
                    BarDataSet set = new BarDataSet(entries, "BarDataSet");
                    int workColor = getResources().getColor(R.color.work);
                    int friendColor = getResources().getColor(R.color.friend);
                    int familyColor = getResources().getColor(R.color.family);
                    int healthColor = getResources().getColor(R.color.health);
                    int financeColor = getResources().getColor(R.color.finance);
                    int loveColor = getResources().getColor(R.color.love);
                    set.setColors(workColor, friendColor, familyColor, healthColor, financeColor, loveColor);
                    set.setValueTextSize(15f);
                    ValueFormatter valueFormatter = new ValueFormatter() {
                        private DecimalFormat decimalFormat = new DecimalFormat("#");
                        @Override
                        public String getBarLabel(BarEntry barEntry) {
                            return decimalFormat.format(barEntry.getY());
                        }
                    };
                    set.setValueFormatter(valueFormatter);
                    BarData data = new BarData(set);
                    data.setBarWidth(0.8f);

                    // Creating the chart
                    barChart.setDrawBarShadow(false);
                    barChart.setDrawValueAboveBar(true);
                    barChart.getDescription().setEnabled(false);
                    barChart.setPinchZoom(true);
                    barChart.setDrawGridBackground(false);
                    barChart.getAxisRight().setEnabled(false);
                    barChart.getLegend().setEnabled(false);
                    barChart.setExtraBottomOffset(10f);


                    // the labels that should be drawn on the XAxis
                    String[] reasons = new String[] {"Work", "Friends", "Family", "Health", "Finance", "Love"};
                    XAxis xAxis = barChart.getXAxis();
                    xAxis.setDrawGridLines(false);
                    xAxis.setDrawAxisLine(false);
                    xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                    xAxis.setGranularity(1f);
                    xAxis.setValueFormatter( new IndexAxisValueFormatter(reasons));
                    xAxis.setTextSize(15f);


                    YAxis yAxis = barChart.getAxisLeft();
                    yAxis.setDrawGridLines(false);
                    yAxis.setEnabled(false);

                    barChart.setData(data);
                    barChart.setFitBars(true);
                    barChart.invalidate();

                } else {
                    Log.d("Steven", "Error getting documents: ", task.getException());
                }
            }
        });
    }

    public void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = "MoodDiaryNotificationChannel";
            String description = "Mental health notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("MoodDiary", name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getContext().getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        } else {
            Log.d("Steven", "Android SDK too old, doesn't require notification channel");
        }
    }
}