package com.example.self_enrichment_app.view.dashboard;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.LessonPostNotification;
import com.example.self_enrichment_app.data.model.MainGoals;
import com.example.self_enrichment_app.data.model.User;
import com.example.self_enrichment_app.viewmodel.LessonsLearntViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class GoalsAdapter extends FirestoreRecyclerAdapter<MainGoals,GoalsAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;

    public GoalsAdapter(@NonNull FirestoreRecyclerOptions<MainGoals> options) {
        super(options);
    }

    @NonNull
    @Override
    public GoalsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerviewitem_dashboard_goals, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull MainGoals mainGoals) {
        holder.tvMainGoal.setText(mainGoals.getGoal());
        int count = 0;
        for (boolean subGoalCompletion : mainGoals.getSubGoalsCompletion()) {
            if (subGoalCompletion) count++;
        }
        int sizeOfSubGoals = mainGoals.getSubGoalsCompletion().size()==0?1:mainGoals.getSubGoalsCompletion().size();
        holder.pbMainGoal.setProgress(count*100/sizeOfSubGoals);
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvMainGoal;
        ProgressBar pbMainGoal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvMainGoal = itemView.findViewById(R.id.tvMainGoal);
            pbMainGoal = itemView.findViewById(R.id.pbMainGoal);
        }
    }
}
