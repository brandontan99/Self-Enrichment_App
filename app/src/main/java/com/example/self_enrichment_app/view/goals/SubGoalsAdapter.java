package com.example.self_enrichment_app.view.goals;

import android.content.Context;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.MainGoals;
import com.example.self_enrichment_app.viewmodel.GoalsTrackerViewModel;

import java.util.List;

public class SubGoalsAdapter extends RecyclerView.Adapter<SubGoalsAdapter.ViewHolder>{
    private LayoutInflater layoutInflater;
    private List<String> data;
    private List<Boolean> subData;
    private Context context;
    private GoalsTrackerViewModel goalsTrackerViewModel;

    SubGoalsAdapter(List<String> data, List<Boolean> subData, GoalsTrackerViewModel goalsTrackerViewModel){
        this.data= data;
        this.subData=subData;
        this.goalsTrackerViewModel=goalsTrackerViewModel;
    }
    @NonNull
    @Override
    public SubGoalsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        this.layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cardview_subgoals_goalstracker, parent, false);
        return new SubGoalsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubGoalsAdapter.ViewHolder holder, int position) {
        String subGoals = data.get(position);
        holder.ETSubGoal.setText(subGoals);
        holder.ETSubGoal.setInputType(InputType.TYPE_NULL);
        holder.ETSubGoal.setTextIsSelectable(false);
        holder.ETSubGoal.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                return true;  // Blocks input from hardware keyboards.
            }
        });
        holder.ETSubGoal.setClickable(false);
        holder.ETSubGoal.setBackground(null);
        Boolean subGoalsCompletion = subData.get(position);
        holder.CBSubGoal.setChecked(subGoalsCompletion);
        holder.btnDeleteSubGoal.setVisibility(View.GONE);
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
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        EditText ETSubGoal;
        Button btnDeleteSubGoal;
        CheckBox CBSubGoal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ETSubGoal = itemView.findViewById(R.id.ETSubGoal);
            btnDeleteSubGoal = itemView.findViewById(R.id.btnDeleteSubGoal);
            CBSubGoal = itemView.findViewById(R.id.CBSubGoal);
        }
    }
}
