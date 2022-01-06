package com.example.self_enrichment_app.view.goals;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.MainGoals;
import com.example.self_enrichment_app.data.model.SubGoals;
import com.example.self_enrichment_app.viewmodel.GoalsTrackerViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class MainGoalsAdapter extends FirestoreRecyclerAdapter<MainGoals,MainGoalsAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private boolean edit;
    private SubGoalsAdapter subGoalsAdapter;
    private GoalsTrackerViewModel goalsTrackerViewModel;

    public MainGoalsAdapter(@NonNull FirestoreRecyclerOptions<MainGoals> options,boolean edit) {
        super(options);
        this.edit=edit;
    }

    @NonNull
    @Override
    public MainGoalsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        this.layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cardview_maingoals_goalstracker, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MainGoalsAdapter.ViewHolder holder, int position, @NonNull MainGoals mainGoals) {
        goalsTrackerViewModel = new ViewModelProvider((AppCompatActivity)context).get(GoalsTrackerViewModel.class);
        holder.ETMainGoal.setText(mainGoals.getGoal());
        if (edit){
            holder.ETMainGoal.setInputType(InputType.TYPE_CLASS_TEXT);
        }
        else{
            holder.ETMainGoal.setInputType(InputType.TYPE_NULL);
        }
        holder.ETMainGoal.setTextIsSelectable(edit);
        holder.btnUpdateMainGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goalsTrackerViewModel.updateMainGoals(mainGoals.getMainPostId(),holder.ETMainGoal.getText().toString());
            }
        });
        holder.ETMainGoal.setClickable(edit);
        holder.CBMainGoal.setChecked(mainGoals.isCompleted());
        holder.CBMainGoal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                goalsTrackerViewModel.updateMainGoalsCompletion(mainGoals.getMainPostId(),holder.CBMainGoal.isChecked());
            }
        });
        if (edit){
            //holder.ETMainGoal.setBackground(android.R.drawable.edit_text);
            holder.btnUpdateMainGoal.setVisibility(View.VISIBLE);
        }
        else{
            holder.ETMainGoal.setBackground(null);
            holder.btnUpdateMainGoal.setVisibility(View.GONE);
        }
        List<String> subGoals = mainGoals.getSubGoals();
        List<Boolean> subGoalsCompletion = mainGoals.getSubGoalsCompletion();
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(context);
        holder.rvSubGoals.setLayoutManager(layoutManager);
        ArrayList<SubGoals> subGoalsArrayList=new ArrayList<>();
        if (subGoals!=null) {
            for (int i = 0; i < subGoals.size(); i++) {
                subGoalsArrayList.add(new SubGoals(subGoals.get(i), subGoalsCompletion.get(i)));
            }
            subGoalsAdapter = new SubGoalsAdapter(context, subGoalsArrayList, edit, mainGoals.getMainPostId());
            subGoalsAdapter.registerAdapterDataObserver( new RecyclerView.AdapterDataObserver() {
                @Override
                public void onItemRangeInserted(int positionStart, int itemCount) {
                    holder.rvSubGoals.scrollToPosition(0);
                }
            });
            holder.rvSubGoals.setAdapter(subGoalsAdapter);
            //subGoalsAdapter.startListening();

        }
        holder.btnAddSubGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (holder.ETNewSubGoals.getText().toString().isEmpty()){
                    holder.ETNewSubGoals.setError("The subgoal must have a description!");
                }
                else{
                    SubGoals newSubGoal=new SubGoals(holder.ETNewSubGoals.getText().toString(),false);
                    subGoalsArrayList.add(newSubGoal);
                    goalsTrackerViewModel.addSubGoals(mainGoals.getMainPostId(),subGoalsArrayList);
                    holder.ETNewSubGoals.getText().clear();
                    subGoalsAdapter.notifyItemInserted(subGoalsArrayList.size()-1);
                }
            }
        });
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        EditText ETMainGoal, ETNewSubGoals;
        Button btnUpdateMainGoal, btnAddSubGoal;
        CheckBox CBMainGoal;
        RecyclerView rvSubGoals;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ETMainGoal = itemView.findViewById(R.id.ETMainGoal);
            ETNewSubGoals=itemView.findViewById(R.id.ETNewSubGoals);
            btnUpdateMainGoal = itemView.findViewById(R.id.btnUpdateMainGoal);
            btnAddSubGoal=itemView.findViewById(R.id.btnAddSubGoal);
            CBMainGoal = itemView.findViewById(R.id.CBMainGoal);
            rvSubGoals = itemView.findViewById(R.id.rvSubGoals);
        }
    }

}
