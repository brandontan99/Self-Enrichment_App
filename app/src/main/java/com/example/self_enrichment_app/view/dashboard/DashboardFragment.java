package com.example.self_enrichment_app.view.dashboard;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.LessonPostNotification;
import com.example.self_enrichment_app.data.model.MainGoals;
import com.example.self_enrichment_app.data.model.MoodDiaryEntry;
import com.example.self_enrichment_app.data.model.User;
import com.example.self_enrichment_app.view.MainActivity;
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
                rvDashboardNotifications.scrollToPosition(0);
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
                rvDashboardGoals.scrollToPosition(0);
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
        getUserInfo();
        notificationsAdapter.notifyDataSetChanged();
        goalsAdapter.notifyDataSetChanged();
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
}