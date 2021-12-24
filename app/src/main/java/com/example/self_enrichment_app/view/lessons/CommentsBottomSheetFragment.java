package com.example.self_enrichment_app.view.lessons;

import android.app.Activity;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.self_enrichment_app.data.model.Comment;
import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.viewmodel.LessonsLearntViewModel;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link CommentsBottomSheetFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CommentsBottomSheetFragment extends BottomSheetDialogFragment {
    private RecyclerView rvComments;
    private CommentsAdapter commentsAdapter;
    private List<Comment> items = new ArrayList<>();
    private LessonsLearntViewModel lessonsLearntViewModel;
    private String lessonPostId;
    private int likeCount;

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public CommentsBottomSheetFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CommentsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CommentsBottomSheetFragment newInstance(String param1, String param2) {
        CommentsBottomSheetFragment fragment = new CommentsBottomSheetFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NORMAL, R.style. AppBottomSheetDialogTheme);
         if (getArguments() != null) {
            this.lessonPostId = getArguments().getString("lessonPostId");
            this.likeCount = getArguments().getInt("likeCount");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.btmsheetfragment_comments_lessonslearnt, container, false);
    }
    private void setupFullHeight(BottomSheetDialog bottomSheetDialog) {
        FrameLayout bottomSheet = (FrameLayout) bottomSheetDialog.findViewById(R.id.design_bottom_sheet);
        BottomSheetBehavior behavior = BottomSheetBehavior.from(bottomSheet);
        ViewGroup.LayoutParams layoutParams = bottomSheet.getLayoutParams();

        int windowHeight = getWindowHeight();
        if (layoutParams != null) {
            layoutParams.height = windowHeight;
        }
        bottomSheet.setLayoutParams(layoutParams);
        behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
    }

    private int getWindowHeight() {
        // Calculate window height for fullscreen use
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.heightPixels;
    }
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);

        dialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d = (BottomSheetDialog) dialog;
                setupFullHeight(d);
            }
        });

        // Do something with your dialog like setContentView() or whatever
        return dialog;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView tvLikeCount = view.findViewById(R.id.tvLikeCountComment);
        rvComments = view.findViewById(R.id.rvComments);
        commentsAdapter = new CommentsAdapter(items);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this.getActivity());
        rvComments.setLayoutManager(layoutManager);
        rvComments.setAdapter(commentsAdapter);
        lessonsLearntViewModel = new ViewModelProvider(this).get(LessonsLearntViewModel.class);
        lessonsLearntViewModel.getLiveCommentData(lessonPostId).observe(getViewLifecycleOwner(), commentList -> {
            commentsAdapter = new CommentsAdapter(commentList);
            rvComments.setAdapter(commentsAdapter);
            commentsAdapter.notifyDataSetChanged();
        });
        lessonsLearntViewModel.getLiveLikeCountData(lessonPostId).observe(getViewLifecycleOwner(), likeCount -> {
            tvLikeCount.setText(String.valueOf(likeCount));
        });
        ImageButton ibSendComment = view.findViewById(R.id.ibSendComment);
        EditText etComment = view.findViewById(R.id.etComment);
        ibSendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                lessonsLearntViewModel.addComment(lessonPostId, new Comment(etComment.getText().toString()));
            }
        });
        CheckBox cbLike = view.findViewById(R.id.cbLikeComment);
        cbLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (cbLike.isChecked()){
                    lessonsLearntViewModel.updateLikeCount(lessonPostId,1);
                }else{
                    lessonsLearntViewModel.updateLikeCount(lessonPostId,-1);
                }
            }
        });
    }
}