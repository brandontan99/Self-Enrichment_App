package com.example.self_enrichment_app.view.goals;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

public class GoalsFragment extends Fragment {
    private static boolean edit=false;
    private GoalsTrackerViewModel goalsTrackerViewModel;
    private RecyclerView rvGoals;
    private MainGoalsAdapter mainGoalsAdapter;

    public GoalsFragment() {
        // Required empty public constructor
    }

    public static GoalsFragment newInstance(boolean edit) {
        GoalsFragment fragment = new GoalsFragment();
        Bundle args = new Bundle();
        args.putBoolean("edit", edit);
        fragment.setArguments(args);
        return fragment;
    }
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle=getArguments();
        if (bundle!=null) {
            this.edit = bundle.getBoolean("edit", false);
        }
        Log.d("Edit",Boolean.toString(edit));
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
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvGoals = view.findViewById(R.id.rvGoals);
        rvGoals.setLayoutManager(layoutManager);
        rvGoals.setItemAnimator(new DefaultItemAnimator());
        //goalsTrackerViewModel = new ViewModelProvider(this).get(GoalsTrackerViewModel.class);
        Query query = FirebaseFirestore.getInstance()
                .collection("MainGoals");
        FirestoreRecyclerOptions<MainGoals> options = new FirestoreRecyclerOptions.Builder<MainGoals>().setQuery(query, MainGoals.class).build();
        mainGoalsAdapter = new MainGoalsAdapter(options,edit);
        mainGoalsAdapter.registerAdapterDataObserver( new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                rvGoals.scrollToPosition(0);
            }
        });
        rvGoals.setAdapter(mainGoalsAdapter);
        Button btnEditGoals = view.findViewById(R.id.btnEditGoals);
        btnEditGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnView) {
                /*FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
                GoalsFragment goalsFragment = new GoalsFragment();
                Bundle bundle = new Bundle();
                bundle.putBoolean("edit",true);
                goalsFragment.setArguments(bundle);*/
                FragmentTransaction ft = (getActivity()).getSupportFragmentManager().beginTransaction();
                GoalsFragment fragmentDemo = GoalsFragment.newInstance(true);
                ft.replace(R.id.nhfMain, fragmentDemo);
                ft.commit();
            }
        });
        Button btnActiveGoals = view.findViewById(R.id.btnActiveGoals);
        Button btnCompletedGoals = view.findViewById(R.id.btnCompletedGoals);
        btnActiveGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnView) {
                btnActiveGoals.setBackgroundColor(getResources().getColor(R.color.light_orange));
                btnCompletedGoals.setBackgroundColor(getResources().getColor(R.color.white));
            }
        });
        btnCompletedGoals.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnView) {
                btnCompletedGoals.setBackgroundColor(getResources().getColor(R.color.light_orange));
                btnActiveGoals.setBackgroundColor(getResources().getColor(R.color.white));
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