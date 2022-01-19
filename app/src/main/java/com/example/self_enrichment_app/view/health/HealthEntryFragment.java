package com.example.self_enrichment_app.view.health;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.appcompat.widget.AppCompatImageButton;
import android.widget.EditText;
import android.widget.TextView;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.HealthEntry;
import com.example.self_enrichment_app.view.MainActivity;
import com.example.self_enrichment_app.viewmodel.HealthEntriesViewModel;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HealthEntryFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HealthEntryFragment extends Fragment {

    private EditText ETEnterWeight, ETEnterHeight, ETEnterSysP, ETEnterDiaP, ETEnterPulse, ETEnterStepsCount;
    private AppCompatImageButton BtnCancelHealthEntry, BtnSubmitHealthEntry;
    private String date;
    private FirebaseAuth mAuth;
    private String userId;
    private HealthEntriesViewModel healthEntriesViewModel;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public HealthEntryFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HealthEntryFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HealthEntryFragment newInstance(String param1, String param2) {
        HealthEntryFragment fragment = new HealthEntryFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
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
        return inflater.inflate(R.layout.fragment_health_entry, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();
        ETEnterWeight = view.findViewById(R.id.ETEnterWeight);
        ETEnterHeight = view.findViewById(R.id.ETEnterHeight);
        ETEnterSysP = view.findViewById(R.id.ETEnterSysP);
        ETEnterDiaP = view.findViewById(R.id.ETEnterDiaP);
        ETEnterPulse = view.findViewById(R.id.ETEnterPulse);
        ETEnterStepsCount = view.findViewById(R.id.ETEnterStepsCount);
        healthEntriesViewModel = new ViewModelProvider(this).get(HealthEntriesViewModel.class);

        Bundle bundle = getArguments();
        if (bundle!=null) {
            date = bundle.getString("date");
        }

        BtnCancelHealthEntry = view.findViewById(R.id.BtnCancelHealthEntry);
        View.OnClickListener OCLCancelHealthEntry = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.destHealth, bundle);
            }
        };
        BtnCancelHealthEntry.setOnClickListener(OCLCancelHealthEntry);

        BtnSubmitHealthEntry = view.findViewById(R.id.BtnSubmitHealthEntry);
        View.OnClickListener OCLSubmitHealthEntry = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Navigation.findNavController(view).navigate(R.id.destHealth, bundle);
                int newWeight = Integer.parseInt(ETEnterWeight.getText().toString());
                int newHeight = Integer.parseInt(ETEnterHeight.getText().toString());
                int newSys = Integer.parseInt(ETEnterSysP.getText().toString());
                int newDia = Integer.parseInt(ETEnterDiaP.getText().toString());
                int newPulse = Integer.parseInt(ETEnterPulse.getText().toString());
                int newStepsGoal = Integer.parseInt(ETEnterStepsCount.getText().toString());
                HealthEntry healthEntry = new HealthEntry(date, userId, newWeight, newHeight,
                newSys, newDia, newPulse, newStepsGoal);
                healthEntriesViewModel.addHealthEntry(healthEntry);
            }
        };
        BtnSubmitHealthEntry.setOnClickListener(OCLSubmitHealthEntry);
    }
}