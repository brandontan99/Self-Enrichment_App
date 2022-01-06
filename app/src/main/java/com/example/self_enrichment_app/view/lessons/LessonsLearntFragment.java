package com.example.self_enrichment_app.view.lessons;

import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.self_enrichment_app.view.MainActivity;
import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.LessonPost;
import com.example.self_enrichment_app.viewmodel.LessonsLearntViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.List;

public class LessonsLearntFragment extends Fragment {
    private RecyclerView rvPosts;
    private LessonPostsAdapter lessonPostsAdapter;
    private EditText etLesson;
    private LessonsLearntViewModel lessonsLearntViewModel;
    private FirebaseFirestore mFirestore = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    private String userId;

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
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getUid();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvPosts = view.findViewById(R.id.rvPosts);
        rvPosts.setLayoutManager(layoutManager);
        rvPosts.setItemAnimator(new DefaultItemAnimator());
        lessonsLearntViewModel = new ViewModelProvider(this).get(LessonsLearntViewModel.class);
        Query query = mFirestore
                .collection("LessonPosts").orderBy("createdAt", Query.Direction.DESCENDING);
        FirestoreRecyclerOptions<LessonPost> options = new FirestoreRecyclerOptions.Builder<LessonPost>().setQuery(query, LessonPost.class).build();
        lessonPostsAdapter = new LessonPostsAdapter(options, userId);
        lessonPostsAdapter.registerAdapterDataObserver(    new RecyclerView.AdapterDataObserver() {
            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                rvPosts.scrollToPosition(0);
            }
        });
        rvPosts.setAdapter(lessonPostsAdapter);
        Button btnSendPost = view.findViewById(R.id.btnSendPost);
        etLesson = view.findViewById(R.id.etLesson);
        etLesson.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if(charSequence.toString().trim().length()==0){
                    btnSendPost.setVisibility(View.GONE);
                }else{
                    btnSendPost.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        btnSendPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View btnView) {
                Toast.makeText(getActivity(), "You have posted a lesson successfully.",
                        Toast.LENGTH_SHORT).show();
                lessonsLearntViewModel.addLessonPost(new LessonPost(etLesson.getText().toString()));
                etLesson.getText().clear();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        lessonPostsAdapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        lessonPostsAdapter.stopListening();
    }
}