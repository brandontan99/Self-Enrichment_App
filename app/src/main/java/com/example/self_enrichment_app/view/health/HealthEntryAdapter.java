package com.example.self_enrichment_app.view.health;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.HealthEntry;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.view.lessons.LessonPostsAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class HealthEntryAdapter extends FirestoreRecyclerAdapter<HealthEntry,HealthEntryAdapter.ViewHolder>{
    private LayoutInflater layoutInflater;
    private Context context;
    private String userId;

    public HealthEntryAdapter(@NonNull FirestoreRecyclerOptions<HealthEntry> options) {
        super(options);
        //this.userId = userId;
    }

    @NonNull
    @Override
    public HealthEntryAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        this.layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cardview_post_lessonslearnt, parent, false);
        return new HealthEntryAdapter.ViewHolder(view);
    }
}
