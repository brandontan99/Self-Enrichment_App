package com.example.self_enrichment_app.view.goals;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
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
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.List;

public class SubGoalsAdapter extends FirestoreRecyclerAdapter<MainGoals,SubGoalsAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;

    public SubGoalsAdapter(@NonNull FirestoreRecyclerOptions<MainGoals> options) {
        super(options);
    }
    @NonNull
    @Override
    public SubGoalsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        this.layoutInflater = LayoutInflater.from(context);
        Log.d("SubGoals","Test");
        View view = layoutInflater.inflate(R.layout.cardview_subgoals_goalstracker, parent, false);
        return new SubGoalsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubGoalsAdapter.ViewHolder holder, int position, MainGoals mainGoals) {
        holder.ETSubGoal.setText(mainGoals.getGoal());
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
        //Boolean subGoalsCompletion = subData.get(position);
        //holder.CBSubGoal.setChecked(subGoalsCompletion);
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
