package com.example.self_enrichment_app.view.goals;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.recyclerview.widget.RecyclerView;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.SubGoals;
import com.example.self_enrichment_app.viewmodel.GoalsTrackerViewModel;

import java.util.ArrayList;
import java.util.List;

public class SubGoalsAdapter extends RecyclerView.Adapter<SubGoalsAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private ArrayList<SubGoals> subGoalsArrayList;
    private boolean edit;
    private String mainPostId;
    private GoalsTrackerViewModel goalsTrackerViewModel;
    private NavController navController;

    public SubGoalsAdapter(Context context, ArrayList<SubGoals> subGoalsArrayList, boolean edit, String mainPostId, NavController navController) {
        this.context = context;
        this.subGoalsArrayList = subGoalsArrayList;
        this.edit=edit;
        this.mainPostId=mainPostId;
        this.navController=navController;
    }
    @NonNull
    @Override
    public SubGoalsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        this.layoutInflater = LayoutInflater.from(context);
        View view = layoutInflater.inflate(R.layout.cardview_subgoals_goalstracker, parent, false);
        return new SubGoalsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SubGoalsAdapter.ViewHolder holder, int position) {
        goalsTrackerViewModel = new ViewModelProvider((AppCompatActivity)context).get(GoalsTrackerViewModel.class);
        SubGoals subGoal=subGoalsArrayList.get(position);
        holder.ETSubGoal.setText(subGoal.getGoal());
        if (edit) {
            holder.ETSubGoal.setInputType(InputType.TYPE_CLASS_TEXT);
            holder.ETSubGoal.setTextIsSelectable(true);
            holder.ETSubGoal.setClickable(true);
            //holder.ETSubGoal.setBackground(null);
            holder.btnUpdateSubGoal.setVisibility(View.VISIBLE);
            holder.btnDeleteSubGoal.setVisibility(View.VISIBLE);
        }
        else{
            holder.ETSubGoal.setInputType(InputType.TYPE_NULL);
            holder.ETSubGoal.setTextIsSelectable(false);
            holder.ETSubGoal.setClickable(false);
            holder.ETSubGoal.setBackground(null);
            holder.btnUpdateSubGoal.setVisibility(View.GONE);
            holder.btnDeleteSubGoal.setVisibility(View.GONE);
        }
        holder.btnUpdateSubGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SubGoals updatedSubGoal=new SubGoals(holder.ETSubGoal.getText().toString(),subGoal.getCompleted());
                subGoalsArrayList.set(position,updatedSubGoal);
                goalsTrackerViewModel.updateSubGoals(mainPostId,subGoalsArrayList);
                Toast.makeText(context,"Sub goal edited.",Toast.LENGTH_SHORT);
            }
        });
        holder.CBSubGoal.setChecked(subGoal.getCompleted());
        holder.CBSubGoal.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                SubGoals updatedSubGoal=new SubGoals(subGoal.getGoal(),isChecked);
                subGoalsArrayList.set(position,updatedSubGoal);
                goalsTrackerViewModel.updateSubGoalsCompletion(mainPostId,subGoalsArrayList);
            }
        });
        holder.btnDeleteSubGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder((AppCompatActivity)context);
                builder.setCancelable(true);
                builder.setTitle("Confirm Deletion");
                builder.setMessage("Are you sure you want to delete this subgoal?");
                builder.setPositiveButton("Confirm",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                subGoalsArrayList.remove(position);
                                goalsTrackerViewModel.deleteSubGoals(mainPostId,subGoalsArrayList);
                                Bundle bundle = new Bundle();
                                bundle.putBoolean("edit", true);
                                navController.navigate(R.id.action_destGoals_self,bundle);
                            }
                        });
                builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.show();
            }
        });
    }
    @Override
    public int getItemCount() {
        return subGoalsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        EditText ETSubGoal;
        Button btnUpdateSubGoal,btnDeleteSubGoal;
        CheckBox CBSubGoal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ETSubGoal = itemView.findViewById(R.id.ETSubGoal);
            btnUpdateSubGoal = itemView.findViewById(R.id.btnUpdateSubGoal);
            CBSubGoal = itemView.findViewById(R.id.CBSubGoal);
            btnDeleteSubGoal=itemView.findViewById(R.id.btnDeleteSubGoal);
        }
    }
}
