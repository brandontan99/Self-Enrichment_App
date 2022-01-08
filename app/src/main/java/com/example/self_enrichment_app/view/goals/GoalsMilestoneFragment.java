package com.example.self_enrichment_app.view.goals;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.view.MainActivity;

public class GoalsMilestoneFragment extends Fragment {
    private NavController navController;
    private String numGoals;
    public GoalsMilestoneFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if (bundle!=null) {
            this.numGoals = bundle.getString("numGoals");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setToolbarTitle(R.string.title_goals);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goals_milestone, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        navController = Navigation.findNavController(view);
        Button btnGoalsAchieved=view.findViewById(R.id.btnGoalsAchieved);
        TextView TVGoalsAchieved=view.findViewById(R.id.TVGoalsAchieved);
        TVGoalsAchieved.setText(TVGoalsAchieved.getText().toString().replace("x",numGoals));
        btnGoalsAchieved.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navController.navigate(R.id.action_destGoalsMilestoneFragment_to_destGoals);
            }
        });
    }
}