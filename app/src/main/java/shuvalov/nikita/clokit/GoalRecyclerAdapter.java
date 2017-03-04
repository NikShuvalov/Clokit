package shuvalov.nikita.clokit;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;

import shuvalov.nikita.clokit.POJOs.Goal;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class GoalRecyclerAdapter extends RecyclerView.Adapter<GoalViewHolder> {
    private ArrayList<Goal> mGoals;

    public GoalRecyclerAdapter(ArrayList<Goal> goals) {
        mGoals = goals;
    }

    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_goal, null));
    }

    @Override
    public void onBindViewHolder(final GoalViewHolder holder, int position) {
        holder.bindDataToViews(mGoals.get(holder.getAdapterPosition()));
        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ToDo: Logic for editing or removing entry.
            }
        });
        holder.mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                SharedPreferences sharedPreferences = holder.mToggleButton.getContext().getSharedPreferences(AppConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
                String currentGoal = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL,AppConstants.PREFERENCES_NO_GOAL);
                Goal selectedGoal = mGoals.get(holder.getAdapterPosition());
                if(currentGoal.equals(AppConstants.PREFERENCES_NO_GOAL)){ //If no goal is currently active, then allow this goal to become new goal.
                    sharedPreferences.edit().putString(AppConstants.PREFERENCES_CURRENT_GOAL,selectedGoal.getGoalName()).apply(); //Save name of current goal.
                    sharedPreferences.edit().putLong(AppConstants.PREFERENCES_START_TIME, System.currentTimeMillis()).apply(); //Save the current time of the selected goal.
                    //ToDo: Logic for starting a new task.
                } else if(currentGoal.equals(selectedGoal.getGoalName())){ //If selected goal is the current goal, then we end current goal.
                    long currentTime = System.currentTimeMillis();

                    //ToDo: Get database entry, or singleton storage for the goal in question.

                    sharedPreferences.edit().putString(AppConstants.PREFERENCES_CURRENT_GOAL,AppConstants.PREFERENCES_NO_GOAL).apply();
                    long startTime = sharedPreferences.getLong(AppConstants.PREFERENCES_START_TIME, -1);
                    long timeSpent = currentTime-startTime;
                    //ToDo: Get goal and add timeSpent to the goal, and save the goal into database.
                    if(startTime == -1){
                        Log.e("GoalRecyclerAdapter", "onCheckedChanged: ", new IllegalArgumentException("Error in retrieving start time of previous goal."));
                    }

                }
                else{ //If there is a current goal, and the goal is not the selected goal
                    if(b){
                        Toast.makeText(holder.mEditButton.getContext(), "There is currently another active goal", Toast.LENGTH_SHORT).show();
                        compoundButton.setChecked(false);
                    }

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mGoals.size();
    }
}
