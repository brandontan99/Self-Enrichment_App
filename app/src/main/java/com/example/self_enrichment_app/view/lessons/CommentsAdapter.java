package com.example.self_enrichment_app.view.lessons;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.recyclerview.widget.RecyclerView;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.data.model.User;
import com.example.self_enrichment_app.viewmodel.LessonsLearntViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentsAdapter extends FirestoreRecyclerAdapter<Comment,CommentsAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private LessonsLearntViewModel lessonsLearntViewModel;
    private LifecycleOwner lifecycleOwner;

    public CommentsAdapter(@NonNull FirestoreRecyclerOptions<Comment> options, LessonsLearntViewModel lessonsLearntViewModel, LifecycleOwner lifecycleOwner) {
        super(options);
        this.lessonsLearntViewModel = lessonsLearntViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_comment_lessonslearnt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull Comment comment) {
        lessonsLearntViewModel.getLiveUserData(comment.getUserId()).observe(lifecycleOwner, commentUser -> {
//            holder.tvName.setText(commentUser.getUserName());
//            Picasso.get().load(commentUser.getImageURL()).resize(45, 45).into(holder.ivProfilePic);
            holder.tvName.setText("Brandon");
            Picasso.get().load("https://encrypted-tbn0.gstatic.com/images?q=tbn:ANd9GcScSc2b1SgH7LH8wFw_vrAX85vVftQ0c8Pc3SxrU71e0Fa2SwXikvhS_LekmWu-pj26CVE&usqp=CAU").resize(45, 45).into(holder.ivProfilePic);
                });
        holder.tvComment.setText(String.valueOf(comment.getCommentMsg()));
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        TextView tvName, tvComment;
        ImageView ivProfilePic;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvName);
            tvComment = itemView.findViewById(R.id.tvComment);
            ivProfilePic = itemView.findViewById(R.id.ivProfilePic);
        }
    }
}
