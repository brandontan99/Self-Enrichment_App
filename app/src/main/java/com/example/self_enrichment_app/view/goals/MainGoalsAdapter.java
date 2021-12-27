package com.example.self_enrichment_app.view.goals;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.MainGoals;
import com.example.self_enrichment_app.view.lessons.CommentsBottomSheetFragment;
import com.example.self_enrichment_app.view.lessons.LessonPostsAdapter;
import com.example.self_enrichment_app.viewmodel.GoalsTrackerViewModel;
import com.example.self_enrichment_app.viewmodel.LessonsLearntViewModel;

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
        View view = layoutInflater.inflate(R.layout.cardview_post_goalstracker, parent, false);
        return new MainGoalsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonPostsAdapter.ViewHolder holder, int position) {
        MainGoals mainGoals = data.get(position);
        /*holder.goal.setText(mainGoals.getGoal());
        if(lessonPost.getLikeCount()==0){
            holder.tvLikeCount.setVisibility(View.GONE);
            holder.ivLikeCount.setVisibility(View.GONE);
        }else{
            holder.tvLikeCount.setVisibility(View.VISIBLE);
            holder.ivLikeCount.setVisibility(View.VISIBLE);
            holder.tvLikeCount.setText(String.valueOf(lessonPost.getLikeCount()));
        }
        int commentCount = (lessonPost.getCommentList() == null) ? 0 : lessonPost.getCommentList().size();
        if(commentCount==0){
            holder.tvCommentCount.setVisibility(View.GONE);
        }else{
            holder.tvCommentCount.setVisibility(View.VISIBLE);
            holder.tvCommentCount.setText(String.valueOf(commentCount) + " Comments");
        }
        holder.cbLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.cbLike.isChecked()){
                    lessonsLearntViewModel.updateLikeCount(lessonPost.getLessonPostId(),1);
                    holder.cbLike.setTextColor(context.getResources().getColor(R.color.orange));
                    holder.cbLike.setTypeface(ResourcesCompat.getFont(context, R.font.montserrat_bold));
                }else{
                    lessonsLearntViewModel.updateLikeCount(lessonPost.getLessonPostId(),-1);
                    holder.cbLike.setTextColor(context.getResources().getColor(R.color.black));
                    holder.cbLike.setTypeface(ResourcesCompat.getFont(context, R.font.montserrat_light));
                }
            }
        });

        holder.btnComment.setOnClickListener(new View.OnClickListener() {
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
        /*TextView tvLesson, tvLikeCount, tvCommentCount;
        Button btnComment;
        CheckBox cbLike;
        ImageView ivLikeCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLesson = itemView.findViewById(R.id.tvLesson);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
            btnComment = itemView.findViewById(R.id.btnComment);
            cbLike = itemView.findViewById(R.id.cbLike);
            ivLikeCount = itemView.findViewById(R.id.ivLikeCount);
        }*/
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
