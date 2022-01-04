package com.example.self_enrichment_app.view.goals;

import android.content.Context;
import android.text.InputType;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.self_enrichment_app.R;
import com.example.self_enrichment_app.data.model.MainGoals;
import com.example.self_enrichment_app.data.model.SubGoals;
import com.example.self_enrichment_app.viewmodel.GoalsTrackerViewModel;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import java.util.ArrayList;
import java.util.List;

public class SubGoalsAdapter extends RecyclerView.Adapter<SubGoalsAdapter.ViewHolder> {
    private LayoutInflater layoutInflater;
    private Context context;
    private ArrayList<SubGoals> subGoalsArrayList;
    private boolean edit;

    public SubGoalsAdapter(Context context, ArrayList<SubGoals> subGoalsArrayList, boolean edit) {
        this.context = context;
        this.subGoalsArrayList = subGoalsArrayList;
        this.edit=edit;
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

        holder.ETSubGoal.setText(subGoalsArrayList.get(position).getGoal());
        if (edit) {
            holder.ETSubGoal.setInputType(InputType.TYPE_CLASS_TEXT);
            holder.ETSubGoal.setTextIsSelectable(true);
            holder.ETSubGoal.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    return true;  // Blocks input from hardware keyboards.
                }
            });
            holder.ETSubGoal.setClickable(true);
            //holder.ETSubGoal.setBackground(null);
            holder.btnDeleteSubGoal.setVisibility(View.VISIBLE);
        }
        else{
            holder.ETSubGoal.setInputType(InputType.TYPE_NULL);
            holder.ETSubGoal.setTextIsSelectable(false);
            holder.ETSubGoal.setClickable(false);
            holder.ETSubGoal.setBackground(null);
            holder.btnDeleteSubGoal.setVisibility(View.GONE);
        }
        holder.CBSubGoal.setChecked(subGoalsArrayList.get(position).getCompleted());

    }
    @Override
    public int getItemCount() {
        return subGoalsArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        EditText ETSubGoal;
        Button btnDeleteSubGoal;
        CheckBox CBSubGoal;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            ETSubGoal = itemView.findViewById(R.id.ETSubGoal);
            btnDeleteSubGoal = itemView.findViewById(R.id.btnDeleteSubGoal);
            CBSubGoal = itemView.findViewById(R.id.CBSubGoal);
        }
    }
}
