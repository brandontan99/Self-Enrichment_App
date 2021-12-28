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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.MainGoals;
import com.example.self_enrichment_app.view.lessons.CommentsBottomSheetFragment;
import com.example.self_enrichment_app.view.lessons.LessonPostsAdapter;
import com.example.self_enrichment_app.viewmodel.GoalsTrackerViewModel;
import com.example.self_enrichment_app.viewmodel.LessonsLearntViewModel;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainGoalsAdapter extends RecyclerView.Adapter<MainGoalsAdapter.ViewHolder>{
    private LayoutInflater layoutInflater;
    private List<MainGoals> data;
    private Context context;
    private GoalsTrackerViewModel goalsTrackerViewModel;


    MainGoalsAdapter(List<MainGoals> data, GoalsTrackerViewModel goalsTrackerViewModel){
        this.data= data;
        this.goalsTrackerViewModel = goalsTrackerViewModel;
    }
    @NonNull
    @Override
    public MainGoalsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        this.layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cardview_maingoals_goalstracker, parent, false);
        return new MainGoalsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainGoalsAdapter.ViewHolder holder, int position) {
        MainGoals mainGoals = data.get(position);
        holder.ETMainGoal.setText(mainGoals.getGoal());
        holder.ETMainGoal.setInputType(InputType.TYPE_NULL);
        holder.ETMainGoal.setTextIsSelectable(false);
        holder.ETMainGoal.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return true;  // Blocks input from hardware keyboards.
            }
        });
        holder.ETMainGoal.setClickable(false);
        holder.ETMainGoal.setBackground(null);
        holder.btnDeleteMainGoal.setVisibility(View.GONE);
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
        });*/
        List<String> subGoals = mainGoals.getSubGoals();
        List<Boolean> subGoalsCompletion = mainGoals.getSubGoalsCompletion();
        Log.d("test","Ok");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.rvSubGoals.setLayoutManager(layoutManager);
        SubGoalsAdapter subGoalsAdapter = new SubGoalsAdapter(subGoals,subGoalsCompletion,goalsTrackerViewModel);
        holder.rvSubGoals.setAdapter(subGoalsAdapter);
        subGoalsAdapter.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return data.size();
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
