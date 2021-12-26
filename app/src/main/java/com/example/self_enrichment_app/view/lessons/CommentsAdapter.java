package com.example.self_enrichment_app.view.lessons;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentsAdapter extends FirestoreRecyclerAdapter<Comment,CommentsAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;

    public CommentsAdapter(@NonNull FirestoreRecyclerOptions<Comment> options) {
        super(options);
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
        holder.tvName.setText(comment.getName());
        holder.tvComment.setText(String.valueOf(comment.getCommentMsg()));
        Picasso.get().load(comment.getImageURL()).resize(45, 45).into(holder.ivProfilePic);
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
