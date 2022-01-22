package com.example.self_enrichment_app.view.mood;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
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

import java.util.ArrayList;
import java.util.List;

public class MoodEditEntry extends Fragment {
    private ImageButton BTNCancelEditDiaryEntry, BTNSubmitEditDiaryEntry;
    private ChipGroup CGEditMoodDiary;
    private RadioGroup RGEditMood;
    private RadioButton RBEditSelectedMood;
    private EditText ETEditDiaryEntry;
    private NavController navController;
    private String editingDate;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private MoodDiaryViewModel moodDiaryViewModel;
    private String userId;

    public MoodEditEntry() {
        // Required empty public constructor
    }

    public static MoodEditEntry newInstance(String param1, String param2) {
        MoodEditEntry fragment = new MoodEditEntry();
        Bundle args = new Bundle();
        fragment.setArguments(args);
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
        return inflater.inflate(R.layout.fragment_mood_edit_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();
        moodDiaryViewModel = new ViewModelProvider(this).get(MoodDiaryViewModel.class);

        // Cancel editing an entry
        BTNCancelEditDiaryEntry = view.findViewById(R.id.BTNCancelEditDiaryEntry);
        BTNCancelEditDiaryEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_destMoodEditEntry_to_destMood);
            }
        });

        // Editing an entry
        BTNSubmitEditDiaryEntry = view.findViewById(R.id.BTNSubmitEditDiaryEntry);
        BTNSubmitEditDiaryEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Getting the mood from the radio group
                RGEditMood = view.findViewById(R.id.RGEditMood);
                RBEditSelectedMood = (RadioButton) view.findViewById(RGEditMood.getCheckedRadioButtonId());
                String mood = RBEditSelectedMood.getText().toString();
                Log.d("Steven", "Selected id: " + RBEditSelectedMood + " Mood: " + mood);
                // Getting the reasons from the chip group
                CGEditMoodDiary = view.findViewById(R.id.CGEditMoodDiary);
                List<Integer> ids = CGEditMoodDiary.getCheckedChipIds();
                List<String> reasons = new ArrayList<>();
                for (Integer id:ids){
                    Chip chip = CGEditMoodDiary.findViewById(id);
                    reasons.add(chip.getText().toString());
                }

                // Getting the entry from the edittext
                ETEditDiaryEntry = view.findViewById(R.id.ETEditDiaryEntry);
                String entryDescription = ETEditDiaryEntry.getText().toString();

                // Uploading all this data into the database
                MoodDiaryEntry updateEntry = new MoodDiaryEntry(mood, reasons, entryDescription, userId, editingDate);
                updateEntry.setCreatedDate(editingDate);
                moodDiaryViewModel.updateMoodDiaryEntry(updateEntry);

                // Navigating back to the mood diary main page
                Toast.makeText(getContext(), "Entry updated", Toast.LENGTH_SHORT).show();
                Bundle bundle = new Bundle();
                bundle.putString("editingDate", editingDate);
                navController.navigate(R.id.action_destMoodEditEntry_to_destMood, bundle);
            }
        });
    }
}