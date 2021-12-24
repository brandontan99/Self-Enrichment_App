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
import com.squareup.picasso.Picasso;

import java.util.List;

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private List<Comment> data;

    CommentsAdapter(List<Comment> data){
        this.data= data;
    }
    @NonNull
    @Override
    public CommentsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.cardview_comment_lessonslearnt, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentsAdapter.ViewHolder holder, int position) {
        Comment comment = data.get(position);
        holder.tvName.setText(comment.getName());
        holder.tvComment.setText(String.valueOf(comment.getCommentMsg()));
        Picasso.get().load(comment.getImageURL()).resize(45, 45).into(holder.ivProfilePic);
    }

    @Override
    public int getItemCount() {
        if (data == null){
            return 0;
        }
        return data.size();
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
