package com.example.self_enrichment_app.view.goals;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.self_enrichment_app.view.MainActivity;
import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.MainGoals;
import com.example.self_enrichment_app.view.lessons.CommentsBottomSheetFragment;
import com.example.self_enrichment_app.view.lessons.LessonPostsAdapter;
import com.example.self_enrichment_app.viewmodel.GoalsTrackerViewModel;
import com.example.self_enrichment_app.viewmodel.LessonsLearntViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

import soup.neumorphism.NeumorphCardView;

public class GoalsFragment extends Fragment {
    private static boolean edit=false, completed=false;
    private GoalsTrackerViewModel goalsTrackerViewModel;
    private RecyclerView rvGoals;
    private MainGoalsAdapter mainGoalsAdapter;
    private NavController navController;

    public GoalsFragment() {
        // Required empty public constructor
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if (bundle!=null) {
            this.edit = bundle.getBoolean("edit", false);
            this.completed= bundle.getBoolean("completed", false);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setToolbarTitle(R.string.title_goals);
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_goals, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        navController = Navigation.findNavController(view);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvGoals = view.findViewById(R.id.rvGoals);
        rvGoals.setLayoutManager(layoutManager);
        rvGoals.setItemAnimator(new DefaultItemAnimator());
        //goalsTrackerViewModel = new ViewModelProvider(this).get(GoalsTrackerViewModel.class);
        Query query = FirebaseFirestore.getInstance()
                .collection("MainGoals").whereEqualTo("completed",completed).orderBy("createdAt",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MainGoals> options = new FirestoreRecyclerOptions.Builder<MainGoals>().setQuery(query, MainGoals.class).build();
        mainGoalsAdapter = new MainGoalsAdapter(options,edit,completed,navController);
        mainGoalsAdapter.registerAdapterDataObserver( new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                rvGoals.scrollToPosition(0);
            }
        });
        rvGoals.setAdapter(mainGoalsAdapter);
        Button btnEditGoals = view.findViewById(R.id.btnEditGoals);
        Button btnCancelEditGoals = view.findViewById(R.id.btnCancelEditGoals);
        if (edit){
            btnCancelEditGoals.setVisibility(View.VISIBLE);
        }
        else{
            btnCancelEditGoals.setVisibility(View.GONE);
        }
        btnCancelEditGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnView) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("edit", false);
                navController.navigate(R.id.action_destGoals_self,bundle);
            }
        });
        btnEditGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnView) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("edit", true);
                navController.navigate(R.id.action_destGoals_self,bundle);
            }
        });
        Button btnActiveGoals = view.findViewById(R.id.btnActiveGoals);
        Button btnCompletedGoals = view.findViewById(R.id.btnCompletedGoals);
        NeumorphCardView addGoalsCardView=view.findViewById(R.id.neumorphCardViewAddMainGoals);
        if (completed){
            btnCompletedGoals.setBackgroundColor(getResources().getColor(R.color.yellow));
            btnActiveGoals.setBackgroundColor(getResources().getColor(R.color.white));
            addGoalsCardView.setVisibility(View.GONE);
        }
        else{
            btnActiveGoals.setBackgroundColor(getResources().getColor(R.color.yellow));
            btnCompletedGoals.setBackgroundColor(getResources().getColor(R.color.white));
            addGoalsCardView.setVisibility(View.VISIBLE);
        }
        btnActiveGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnView) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("edit", false);
                bundle.putBoolean("completed", false);
                navController.navigate(R.id.action_destGoals_self,bundle);
            }
        });
        btnCompletedGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnView) {
                Bundle bundle = new Bundle();
                bundle.putBoolean("edit", false);
                bundle.putBoolean("completed", true);
                navController.navigate(R.id.action_destGoals_self,bundle);
            }
        });
        Button btnAddMainGoal=view.findViewById(R.id.btnAddMainGoal);
        EditText ETNewMainGoal=view.findViewById(R.id.ETNewMainGoal);
        goalsTrackerViewModel = new ViewModelProvider(this).get(GoalsTrackerViewModel.class);
        btnAddMainGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnView) {
                if (ETNewMainGoal.getText().toString().isEmpty()){
                    ETNewMainGoal.setError("The goal must have a description!");
                }
                else{
                    goalsTrackerViewModel.addMainGoals(ETNewMainGoal.getText().toString());
                    ETNewMainGoal.getText().clear();
                }
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        mainGoalsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        mainGoalsAdapter.stopListening();
    }
}