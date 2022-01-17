package com.example.self_enrichment_app.view.mood;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.example.self_enrichment_app.R;

public class MoodEditEntry extends Fragment {
    private ImageButton BTNCancelEditDiaryEntry, BTNSubmitEditDiaryEntry;
    private NavController navController;

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

        // Adding a new entry
        BTNCancelEditDiaryEntry = view.findViewById(R.id.BTNCancelEditDiaryEntry);
        BTNCancelEditDiaryEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add functionality
                navController.navigate(R.id.action_destMoodEditEntry_to_destMood);
            }
        });

        // Adding a new entry
        BTNSubmitEditDiaryEntry = view.findViewById(R.id.BTNSubmitEditDiaryEntry);
        BTNSubmitEditDiaryEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO: Add functionality
                navController.navigate(R.id.action_destMoodEditEntry_to_destMood);
            }
        });
    }
}