package com.example.self_enrichment_app.view.mood;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.MoodDiaryEntry;
import com.example.self_enrichment_app.viewmodel.MoodDiaryViewModel;
import com.google.android.material.chip.Chip;
import com.google.android.material.chip.ChipGroup;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class MoodNewEntry extends Fragment {
    private ImageButton BTNCancelAddDiaryEntry, BTNSubmitAddDiaryEntry;
    private ChipGroup chipGroup;
    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private EditText ETDiaryEntry;
    private String editingDate;
    private NavController navController;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private MoodDiaryViewModel moodDiaryViewModel;
    private String userId;

    public MoodNewEntry() {
        // Required empty public constructor
    }

    public static MoodNewEntry newInstance(String param1, String param2) {
        MoodNewEntry fragment = new MoodNewEntry();
//        Bundle args = new Bundle();
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        editingDate = bundle.getString("editingDate");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_mood_new_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();
        moodDiaryViewModel = new ViewModelProvider(this).get(MoodDiaryViewModel.class);

        // Cancel adding a new entry
        BTNCancelAddDiaryEntry = view.findViewById(R.id.BTNCancelAddDiaryEntry);
        BTNCancelAddDiaryEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("editingDate", editingDate);
                navController.navigate(R.id.action_destMoodNewEntry_to_destMood, bundle);
//                navController.navigate(R.id.action_destMoodNewEntry_to_destMood);
            }
        });

        // Adding a new entry
        BTNSubmitAddDiaryEntry = view.findViewById(R.id.BTNSubmitAddDiaryEntry);
        BTNSubmitAddDiaryEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting the mood from the radio group
                radioGroup = view.findViewById(R.id.RGAddMood);
                radioButton = (RadioButton) view.findViewById(radioGroup.getCheckedRadioButtonId());
                String mood = radioButton.getText().toString();

                // Getting the reasons from the chip group
                chipGroup = view.findViewById(R.id.CGAddMoodDiary);
                List<Integer> ids = chipGroup.getCheckedChipIds();
                List<String> reasons = new ArrayList<>();
                for (Integer id:ids){
                    Chip chip = chipGroup.findViewById(id);
                    reasons.add(chip.getText().toString());
                }

                // Getting the entry from the edittext
                ETDiaryEntry = view.findViewById(R.id.ETAddDiaryEntry);
                String entryDescription = ETDiaryEntry.getText().toString();

                // Uploading all this data into the database
//                Date currentDate = Calendar.getInstance().getTime();
//                SimpleDateFormat dateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
//                String date = dateFormat.format(currentDate);
                MoodDiaryEntry newEntry = new MoodDiaryEntry(mood, reasons, entryDescription, userId, editingDate);
                moodDiaryViewModel.addNewMoodDiaryEntry(newEntry);

                // Navigating back to the mood diary main page
                Toast.makeText(getContext(), "Added new entry!", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("editingDate", editingDate);
                navController.navigate(R.id.action_destMoodNewEntry_to_destMood, bundle);
            }
        });
    }

}