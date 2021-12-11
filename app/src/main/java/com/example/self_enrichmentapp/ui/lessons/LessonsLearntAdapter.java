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

    LessonsLearntAdapter(List<LessonPost> data){
        this.data= data;
    }
    @NonNull
    @Override
    public LessonsLearntAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_post_lessonslearnt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull LessonsLearntAdapter.ViewHolder holder, int position) {
        LessonPost lessonPost = data.get(position);
        holder.tvLesson.setText(lessonPost.getLesson());
        holder.tvLikeCount.setText(String.valueOf(lessonPost.getLikeCount()));
        holder.tvCommentCount.setText(String.valueOf(lessonPost.getCommentCount()) + " Comments");
    }

    @Override
    public int getItemCount() {
        return data.size();
    }
    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvLesson, tvLikeCount, tvCommentCount;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLesson = itemView.findViewById(R.id.tvLesson);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
        }
    }
}
