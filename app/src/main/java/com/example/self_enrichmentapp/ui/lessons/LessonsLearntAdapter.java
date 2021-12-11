package com.example.self_enrichmentapp.ui.lessons;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.self_enrichmentapp.R;
import com.example.self_enrichmentapp.data.model.LessonPost;

import java.util.List;

public class LessonsLearntAdapter extends RecyclerView.Adapter<LessonsLearntAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<LessonPost> data;

    LessonsLearntAdapter(Context context, List<LessonPost> data){
        this.layoutInflater = LayoutInflater.from(context);
        this.data= data;

    }
    @NonNull
    @Override
    public LessonsLearntAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = layoutInflater.inflate(R.layout.cardview_post_lessonslearnt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonsLearntAdapter.ViewHolder holder, int position) {
        LessonPost lessonPost = data.get(position);
        holder.TVLesson.setText(lessonPost.getLesson());
        holder.TVLikeCount.setText(String.valueOf(lessonPost.getLikeCount()));
        holder.TVCommentCount.setText(String.valueOf(lessonPost.getCommentCount()) + " Comments");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView TVLesson, TVLikeCount, TVCommentCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            TVLesson = itemView.findViewById(R.id.tvLesson);
            TVLikeCount = itemView.findViewById(R.id.tvLikeCount);
            TVCommentCount = itemView.findViewById(R.id.tvCommentCount);
        }
    }
}
