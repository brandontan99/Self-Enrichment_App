package com.example.self_enrichment_app.view.goals;

import android.content.Context;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.MainGoals;
import com.example.self_enrichment_app.view.lessons.CommentsBottomSheetFragment;
import com.example.self_enrichment_app.view.lessons.LessonPostsAdapter;
import com.example.self_enrichment_app.viewmodel.GoalsTrackerViewModel;
import com.example.self_enrichment_app.viewmodel.LessonsLearntViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainGoalsAdapter extends FirestoreRecyclerAdapter<MainGoals,MainGoalsAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private boolean edit;

    public MainGoalsAdapter(@NonNull FirestoreRecyclerOptions<MainGoals> options,boolean edit) {
        super(options);
        this.edit=edit;
    }

    @NonNull
    @Override
    public MainGoalsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        this.layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cardview_maingoals_goalstracker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainGoalsAdapter.ViewHolder holder, int position, @NonNull MainGoals mainGoals) {
        holder.ETMainGoal.setText(mainGoals.getGoal());
        holder.ETMainGoal.setInputType(InputType.TYPE_NULL);
        holder.ETMainGoal.setTextIsSelectable(edit);
        holder.ETMainGoal.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return true;  // Blocks input from hardware keyboards.
            }
        });
        holder.ETMainGoal.setClickable(edit);
        if (edit){
            //holder.ETMainGoal.setBackground(android.R.drawable.edit_text);
            holder.btnDeleteMainGoal.setVisibility(View.VISIBLE);
        }
        else{
            holder.ETMainGoal.setBackground(null);
            holder.btnDeleteMainGoal.setVisibility(View.GONE);
        }
        /*holder.btnComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
                CommentsBottomSheetFragment commentsFragment = new CommentsBottomSheetFragment();
                Bundle bundle = new Bundle();
                bundle.putString("lessonPostId",lessonPost.getLessonPostId());
                bundle.putInt("likeCount",lessonPost.getLikeCount());
                commentsFragment.setArguments(bundle);
//                fragmentManager.beginTransaction().replace(R.id.content, commentsFragment).commit();
                commentsFragment.show(fragmentManager,commentsFragment.getTag());
            }
        });
        List<String> subGoals = mainGoals.getSubGoals();
        List<Boolean> subGoalsCompletion = mainGoals.getSubGoalsCompletion();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.rvSubGoals.setLayoutManager(layoutManager);
        for (int i=0;i<subGoals.size();i++) {
            Log.d("Test",subGoals.get(i));
            SubGoalsAdapter subGoalsAdapter = new SubGoalsAdapter(subGoals, subGoalsCompletion);
            holder.rvSubGoals.setAdapter(subGoalsAdapter);
            subGoalsAdapter.notifyDataSetChanged();
        }
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.rvSubGoals.setLayoutManager(layoutManager);
        holder.rvSubGoals.setItemAnimator(new DefaultItemAnimator());
        //goalsTrackerViewModel = new ViewModelProvider(this).get(GoalsTrackerViewModel.class);
        Query query = FirebaseFirestore.getInstance()
                .collection("MainGoals").orderBy("goal", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<MainGoals> options = new FirestoreRecyclerOptions.Builder<MainGoals>().setQuery(query, MainGoals.class).build();
        SubGoalsAdapter subGoalsAdapter = new SubGoalsAdapter(options);
        subGoalsAdapter.registerAdapterDataObserver( new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                holder.rvSubGoals.scrollToPosition(0);
            }
        });
        holder.rvSubGoals.setAdapter(subGoalsAdapter);*/
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        EditText ETMainGoal;
        Button btnDeleteMainGoal;
        CheckBox CBMainGoal;
        RecyclerView rvSubGoals;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ETMainGoal = itemView.findViewById(R.id.ETMainGoal);
            btnDeleteMainGoal = itemView.findViewById(R.id.btnDeleteMainGoal);
            CBMainGoal = itemView.findViewById(R.id.CBMainGoal);
            rvSubGoals = itemView.findViewById(R.id.rvSubGoals);
        }
    }
}
