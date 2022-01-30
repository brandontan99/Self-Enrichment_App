package com.example.self_enrichment_app.view.dashboard;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.LessonPostNotification;
import com.example.self_enrichment_app.data.model.MainGoals;
import com.example.self_enrichment_app.data.model.MoodDiaryEntry;
import com.example.self_enrichment_app.data.model.User;
import com.example.self_enrichment_app.view.MainActivity;
import com.example.self_enrichment_app.view.health.HealthFragment;
import com.example.self_enrichment_app.view.health.StepsCountBackgroundService;
import com.example.self_enrichment_app.view.lessons.CommentsAdapter;
import com.example.self_enrichment_app.view.lessons.LessonPostsAdapter;
import com.example.self_enrichment_app.viewmodel.LessonsLearntViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class DashboardFragment extends Fragment {
    private RecyclerView rvDashboardNotifications,rvDashboardGoals;
    private LessonsLearntViewModel lessonsLearntViewModel;
    private FirebaseAuth mAuth;
    private FirebaseFirestore db;
    private DocumentReference documentReference;
    private String userId;
    private NotificationsAdapter notificationsAdapter;
    private GoalsAdapter goalsAdapter;
    private ImageView IVDashboardMoodDiary;
    private TextView TVGoalValueDashboard;
    private ProgressBar PBDashboardStepsCount;

    public DashboardFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
//         Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_dashboard, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        //Initialize firebase auth and firestore
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();
        db = FirebaseFirestore.getInstance();
        documentReference = db.collection("Users").document(userId);
        rvDashboardNotifications = view.findViewById(R.id.rvDashboardNotifications);
        rvDashboardGoals = view.findViewById(R.id.rvDashboardGoals);
        rvDashboardNotifications.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvDashboardGoals.setLayoutManager(new LinearLayoutManager(this.getActivity()));
        rvDashboardNotifications.setItemAnimator(new DefaultItemAnimator());
        rvDashboardGoals.setItemAnimator(new DefaultItemAnimator());
        lessonsLearntViewModel = new ViewModelProvider(this).get(LessonsLearntViewModel.class);
        Query query = FirebaseFirestore.getInstance()
                .collection("Users").document(userId).collection("Notifications").orderBy("createdAt", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<LessonPostNotification> notificationsOptions = new FirestoreRecyclerOptions.Builder<LessonPostNotification>().setQuery(query, LessonPostNotification.class).build();
        notificationsAdapter = new NotificationsAdapter(notificationsOptions, lessonsLearntViewModel, getViewLifecycleOwner());
        rvDashboardNotifications.setAdapter(notificationsAdapter);
        notificationsAdapter.registerAdapterDataObserver(    new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                rvDashboardNotifications.scrollToPosition(notificationsAdapter.getItemCount() - 1);
            }
        });
        query = FirebaseFirestore.getInstance()
                .collection("MainGoals").whereEqualTo("userId",userId).orderBy("createdAt",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<MainGoals> mainGoalsOptions = new FirestoreRecyclerOptions.Builder<MainGoals>().setQuery(query, MainGoals.class).build();
        goalsAdapter = new GoalsAdapter(mainGoalsOptions);
        rvDashboardGoals.setAdapter(goalsAdapter);
        goalsAdapter.registerAdapterDataObserver(    new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                rvDashboardGoals.scrollToPosition(goalsAdapter.getItemCount() - 1);
            }
        });

        // Mood diary section
        IVDashboardMoodDiary = view.findViewById(R.id.IVDashboardMoodDiary);
        Date today = Calendar.getInstance().getTime();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd MMM yyyy", Locale.getDefault());
        String currentDate = simpleDateFormat.format(today);
        DocumentReference docRef = db.collection("MoodDiaryEntries").document(userId+currentDate);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        MoodDiaryEntry latestEntry = document.toObject(MoodDiaryEntry.class);
                        if (latestEntry.getMood() != null) {
                            if (latestEntry.getMood().equalsIgnoreCase("happy")){
                                IVDashboardMoodDiary.setImageResource(R.drawable.ic_mood_happy);
                            } else if (latestEntry.getMood().equalsIgnoreCase("sad")) {
                                IVDashboardMoodDiary.setImageResource(R.drawable.ic_mood_sad);
                            } else if (latestEntry.getMood().equalsIgnoreCase("angry")) {
                                IVDashboardMoodDiary.setImageResource(R.drawable.ic_mood_angry);
                            } else if (latestEntry.getMood().equalsIgnoreCase("tired")) {
                                IVDashboardMoodDiary.setImageResource(R.drawable.ic_mood_tired);
                            }
                        } else {
                            IVDashboardMoodDiary.setImageResource(R.drawable.ic_mood_tired);
                        }
                    }
                }
            }
        });

        // Health and fitness section

        TVGoalValueDashboard = view.findViewById(R.id.TVGoalValueDashboard);
        PBDashboardStepsCount = view.findViewById(R.id.PBDashboardStepsCount);
        Date stepstoday = Calendar.getInstance().getTime();
        SimpleDateFormat stepsdate = new SimpleDateFormat("dd/M/yyyy");
        String stepstodaydate = stepsdate.format(stepstoday);
        Query stepsquery = FirebaseFirestore.getInstance().collection("HealthEntries").whereEqualTo("userId",userId).whereEqualTo("date",stepstodaydate);
        stepsquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getDocuments().isEmpty()){
                        Log.d("TAG", "No data");
                        Toast.makeText(getActivity(),"Empty data",Toast.LENGTH_SHORT).show();
                        TVGoalValueDashboard.setText("0");
                        PBDashboardStepsCount.setProgress(100);
                    }
                    else{
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("TAG", "Document Id: " + document.getId() + "==" + document.getData());
                            TVGoalValueDashboard.setText(document.get("steps_goal").toString());
                            double stepscountnum = Integer.parseInt(document.get("steps_count").toString());
                            double goalvalue = Integer.parseInt(document.get("steps_goal").toString());
                            if (goalvalue > 0) {
                                int countprogress = (int) ((stepscountnum / goalvalue) * 100);
                                if (countprogress >= 100) {
                                    countprogress = 100;
                                }
                                Log.d("PROGRESS", "PERCENTAGE" + countprogress);
                                PBDashboardStepsCount.setProgress(countprogress);
                            } else {
                                PBDashboardStepsCount.setProgress(100);
                            }
                        }
                    }
                }
            }
        });
    }
    @Override
    public void onStart() {
        super.onStart();
        notificationsAdapter.startListening();
        goalsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        notificationsAdapter.stopListening();
        goalsAdapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        steps();
        getUserInfo();
    }
    //Get user name and profile picture from database
    private void getUserInfo() {
        documentReference.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                User user = documentSnapshot.toObject(User.class);
                String name = user.getUserName();
                ((MainActivity)getActivity()).setToolbarTitle("Welcome",name);
            }
        });
    }

    private void steps(){
        Date stepstoday = Calendar.getInstance().getTime();
        SimpleDateFormat stepsdate = new SimpleDateFormat("dd/M/yyyy");
        String stepstodaydate = stepsdate.format(stepstoday);
        Query stepsquery = FirebaseFirestore.getInstance().collection("HealthEntries").whereEqualTo("userId",userId).whereEqualTo("date",stepstodaydate);
        stepsquery.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    if (task.getResult().getDocuments().isEmpty()){
                        Log.d("TAG", "No data");
                        Toast.makeText(getActivity(),"Empty data",Toast.LENGTH_SHORT).show();
                        TVGoalValueDashboard.setText("0");
                        PBDashboardStepsCount.setProgress(100);
                    }
                    else{
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.d("TAG", "Document Id: " + document.getId() + "==" + document.getData());
                            if (Integer.parseInt(document.get("weight").toString()) > -1) {
                                TVGoalValueDashboard.setText(document.get("steps_goal").toString());
                                double stepscountnum = Integer.parseInt(document.get("steps_count").toString());
                                double goalvalue = Integer.parseInt(document.get("steps_goal").toString());
                                if (goalvalue > 0) {
                                    int countprogress = (int) ((stepscountnum / goalvalue) * 100);
                                    if (countprogress >= 100) {
                                        countprogress = 100;
                                    }
                                    Log.d("PROGRESS", "PERCENTAGE" + countprogress);
                                    PBDashboardStepsCount.setProgress(countprogress);
                                } else {
                                    PBDashboardStepsCount.setProgress(100);
                                }
                            }
                        }
                    }
                }
            }
        });
    }
}