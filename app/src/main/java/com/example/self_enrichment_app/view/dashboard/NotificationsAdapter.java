package com.example.self_enrichment_app.view.dashboard;

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
import com.example.self_enrichment_app.data.model.LessonPostNotification;
import com.example.self_enrichment_app.data.model.User;
import com.example.self_enrichment_app.viewmodel.LessonsLearntViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NotificationsAdapter extends FirestoreRecyclerAdapter<LessonPostNotification,NotificationsAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private LessonsLearntViewModel lessonsLearntViewModel;
    private LifecycleOwner lifecycleOwner;

    public NotificationsAdapter(@NonNull FirestoreRecyclerOptions<LessonPostNotification> options, LessonsLearntViewModel lessonsLearntViewModel, LifecycleOwner lifecycleOwner) {
        super(options);
        this.lessonsLearntViewModel = lessonsLearntViewModel;
        this.lifecycleOwner = lifecycleOwner;
    }

    @NonNull
    @Override
    public NotificationsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        this.layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.recyclerviewitem_dashboard_notification, parent, false);
        return new ViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull ViewHolder holder, int position, @NonNull LessonPostNotification lessonPostNotification) {
        int imgRes;
        switch(lessonPostNotification.getAction()){
            case "commented":
                imgRes = R.drawable.ic_comment;
                break;
            case "liked":
                imgRes = R.drawable.ic_liked;
                break;
            default:
                imgRes = R.drawable.ic_like;
                break;
        }

        holder.ivActionIcon.setImageResource(imgRes);
        StringBuilder notificationText = new StringBuilder("");
        lessonsLearntViewModel.getLiveUserData(lessonPostNotification.getCreatedBy()).observe(lifecycleOwner, notificationUser -> {
            notificationText.append(notificationUser.getUserName());
            notificationText.append(" ").append(lessonPostNotification.getAction()).append(" on your post");
            holder.tvNotification.setText(notificationText.toString());
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        ImageView ivActionIcon;
        TextView tvNotification;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ivActionIcon = itemView.findViewById(R.id.ivActionIcon);
            tvNotification = itemView.findViewById(R.id.tvNotification);
        }
    }
}
