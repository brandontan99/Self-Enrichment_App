package com.example.self_enrichment_app.view.lessons;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.self_enrichment_app.MainActivity;
import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.viewmodel.LessonsLearntViewModel;

public class LessonsLearntFragment extends Fragment {

    private LessonsLearntViewModel lessonsLearntViewModel;
    private RecyclerView rvPosts;
    private LessonPostsAdapter lessonPostsAdapter;
    private EditText etLesson;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        ((MainActivity)getActivity()).setToolbarTitle(R.string.title_lessons);

        return inflater.inflate(R.layout.fragment_lessonslearnt, container, false);
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvPosts = view.findViewById(R.id.rvPosts);
        rvPosts.setLayoutManager(layoutManager);
        lessonsLearntViewModel = new ViewModelProvider(this).get(LessonsLearntViewModel.class);
        lessonsLearntViewModel.getLiveLessonPostData().observe(getViewLifecycleOwner(), lessonPostList -> {
            lessonPostsAdapter = new LessonPostsAdapter(lessonPostList, lessonsLearntViewModel);
            rvPosts.setAdapter(lessonPostsAdapter);
            lessonPostsAdapter.notifyDataSetChanged();
        });
        Button btnSendPost = view.findViewById(R.id.btnSendPost);
        btnSendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnView) {
                etLesson = view.findViewById(R.id.etLesson);
                lessonsLearntViewModel.addLessonPost(new LessonPost(etLesson.getText().toString()));
            }
        });
    }
}