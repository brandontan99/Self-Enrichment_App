package com.example.selfenrichmentapp_general.view.lessons;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.selfenrichmentapp_general.data.model.LessonPost;
import com.example.selfenrichmentapp_general.R;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class LessonPostsAdapter extends FirestoreRecyclerAdapter<LessonPost, LessonPostsAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;

    public LessonPostsAdapter(@NonNull FirestoreRecyclerOptions<LessonPost> options) {
        super(options);
    }

    @NonNull
    @Override
    public LessonPostsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        this.layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cardview_post_lessonslearnt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull LessonPost lessonPost) {
        holder.tvLesson.setText(lessonPost.getLesson());
        if(lessonPost.getLikeCount()==0){
            holder.tvLikeCount.setVisibility(View.GONE);
            holder.ivLikeCount.setVisibility(View.GONE);
        }else{
            holder.tvLikeCount.setVisibility(View.VISIBLE);
            holder.ivLikeCount.setVisibility(View.VISIBLE);
            holder.tvLikeCount.setText(String.valueOf(lessonPost.getLikeCount()));
        }
        int commentCount = lessonPost.getCommentCount();
        if(commentCount==0){
            holder.tvCommentCount.setVisibility(View.GONE);
        }else{
            holder.tvCommentCount.setVisibility(View.VISIBLE);
            holder.tvCommentCount.setText(commentCount + " Comments");
        }
        holder.ibDeletePost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LessonsLearntHelper.deleteLessonPost(lessonPost.getLessonPostId());
            }
        });
        holder.cbLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(holder.cbLike.isChecked()){
                    LessonsLearntHelper.updateLikeCount(lessonPost.getLessonPostId(),1);
                    holder.cbLike.setTextColor(context.getResources().getColor(R.color.orange));
                    holder.cbLike.setTypeface(ResourcesCompat.getFont(context, R.font.montserrat_bold));
                }else{
                    LessonsLearntHelper.updateLikeCount(lessonPost.getLessonPostId(),-1);
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
                commentsFragment.show(fragmentManager,commentsFragment.getTag());
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvLesson, tvLikeCount, tvCommentCount;
        Button btnComment;
        CheckBox cbLike;
        ImageView ivLikeCount;
        ImageButton ibDeletePost;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvLesson = itemView.findViewById(R.id.tvLesson);
            tvLikeCount = itemView.findViewById(R.id.tvLikeCount);
            tvCommentCount = itemView.findViewById(R.id.tvCommentCount);
            btnComment = itemView.findViewById(R.id.btnComment);
            cbLike = itemView.findViewById(R.id.cbLike);
            ivLikeCount = itemView.findViewById(R.id.ivLikeCount);
            ibDeletePost = itemView.findViewById(R.id.ibDeletePost);
        }
    }

}
