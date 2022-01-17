package com.example.self_enrichment_app.view.mood;

import android.app.DatePickerDialog;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.self_enrichment_app.view.MainActivity;
import com.example.self_enrichment_app.R;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class MoodFragment extends Fragment {
    private Calendar myCalendar = Calendar.getInstance();
    private String dateFormat ="dd MMM yyyy";
    private TextView TVDate;
    private Button BTNAddDiaryEntry;
    private ImageButton BTNEditDiaryEntry;
    private NavController navController;


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


        // Adding a new entry
        BTNAddDiaryEntry = view.findViewById(R.id.BTNAddDiaryEntry);
        BTNAddDiaryEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_destMood_to_destMoodNewEntry);
            }
        });

        // Editing a new entry
        BTNEditDiaryEntry = view.findViewById(R.id.BTNEditDiaryEntry);
        BTNEditDiaryEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_destMood_to_destMoodEditEntry);
            }
        });

        // Getting and Setting the initial date
        TVDate= view.findViewById(R.id.TVDate);
        Date currentDate = Calendar.getInstance().getTime();
        SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat, Locale.getDefault());
        TVDate.setText(dateFormat.format(currentDate));

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
                new DatePickerDialog(getContext(), date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });
    }

    public void updateLabel(){
        SimpleDateFormat dateFormat = new SimpleDateFormat(this.dateFormat, Locale.getDefault());
        TVDate.setText(dateFormat.format(myCalendar.getTime()));
    }
}