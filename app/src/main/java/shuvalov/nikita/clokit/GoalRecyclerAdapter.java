package shuvalov.nikita.clokit;

import android.app.NotificationManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.app.NotificationCompat;
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
    private NotificationManager mNotificationManager;

    public GoalRecyclerAdapter(ArrayList<Goal> goals) {
        mGoals = goals;
    }

    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_goal, null));
    }

    @Override
    public void onBindViewHolder(final GoalViewHolder holder, int position) {
        final SharedPreferences sharedPreferences = holder.mToggleButton.getContext().getSharedPreferences(AppConstants.PREFERENCES_NAME,Context.MODE_PRIVATE);
        holder.bindDataToViews(mGoals.get(holder.getAdapterPosition()));
        String activeGoalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
        if(activeGoalName.equals(mGoals.get(holder.getAdapterPosition()).getGoalName())){
            holder.mToggleButton.setChecked(true);
        }else{
            holder.mToggleButton.setChecked(false);
        }
        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //ToDo: Logic for editing or removing entry.
            }
        });
        holder.mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mNotificationManager = (NotificationManager) compoundButton.getContext().getSystemService(Context.NOTIFICATION_SERVICE);
                String currentGoal = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL,AppConstants.PREFERENCES_NO_GOAL);
                Goal selectedGoal = mGoals.get(holder.getAdapterPosition());
                if(currentGoal.equals(AppConstants.PREFERENCES_NO_GOAL)){ //If no goal is currently active, then allow this goal to become new goal.
                    sharedPreferences.edit().putString(AppConstants.PREFERENCES_CURRENT_GOAL,selectedGoal.getGoalName()).apply(); //Save name of current goal.
                    sharedPreferences.edit().putLong(AppConstants.PREFERENCES_START_TIME, System.currentTimeMillis()).apply(); //Save the current time of the selected goal.
                    sharedPreferences.edit().putInt(AppConstants.PREFERENCES_CURRENT_GOAL_WEEK_NUM, AppUtils.getCurrentWeekNum()).apply(); //Adds the weeknum of when the goal was started

                    //ToDo: Might move this into a service that updates the current amount of time spent every minute or so.
                    createNotification(compoundButton.getContext(), selectedGoal);

                } else if(currentGoal.equals(selectedGoal.getGoalName())){ //If selected goal is the current goal, then we end current goal.
                    mNotificationManager.cancel(AppConstants.NOTIFICATION_ID);
                    long currentTime = System.currentTimeMillis();

                    sharedPreferences.edit().putString(AppConstants.PREFERENCES_CURRENT_GOAL,AppConstants.PREFERENCES_NO_GOAL).apply();
                    long startTime = sharedPreferences.getLong(AppConstants.PREFERENCES_START_TIME, -1);

                    int savedWeekNum = sharedPreferences.getInt(AppConstants.PREFERENCES_CURRENT_GOAL_WEEK_NUM, -1);
                    if(savedWeekNum==-1){
                        Log.e("GoalRecyclerAdapter", "Missing preference value", new Exception());
                    }
                    if(savedWeekNum == AppUtils.getCurrentWeekNum()-1){ //The task crossed over into a second week. Divide the task time between the goals of both weeks
                        //Gets the time spent on task for last week by using the task start time and endOfWeekTime, then updates the values in the Database accordingly.
                        long weekEndTime = AppUtils.getWeekEndMillis(savedWeekNum);
                        long timeSpentLastWeek = weekEndTime-startTime;
                        selectedGoal.addTimeSpent(timeSpentLastWeek);
                        GoalSQLHelper sqlHelper = GoalSQLHelper.getInstance(compoundButton.getContext());
                        sqlHelper.updateTimeSpentOnGoal(selectedGoal);


                        //Gets the time spent on task for this week by using the week start time and task end time, then adds the goal to the weekly table, the GoalManager, and the Database.
                        //Since the new goal is the same as the last week's goal, the user may want to edit their goal time.
                        long weekStartTime = AppUtils.getWeekStartMillis(savedWeekNum+1);
                        long timeSpentThisWeek = System.currentTimeMillis() - weekStartTime;
                        selectedGoal.setWeekNum(AppUtils.getCurrentWeekNum());
                        selectedGoal.setCurrentMilli(timeSpentThisWeek);
                        sqlHelper.addGoalToWeeklyTable(selectedGoal);
                        CurrentWeekGoalManager.getInstance().addCurrentGoal(selectedGoal);


                    }else if (savedWeekNum == AppUtils.getCurrentWeekNum()){ //The task started and ended in the same week, simply update values.
                        long timeSpent = currentTime-startTime;
                        Goal updatedGoal = mGoals.get(holder.getAdapterPosition());
                        updatedGoal.addTimeSpent(timeSpent); //Updates the value in the adapter.
                        GoalSQLHelper sqlHelper = GoalSQLHelper.getInstance(compoundButton.getContext());
                        sqlHelper.updateTimeSpentOnGoal(updatedGoal);
                        sqlHelper.updateLifetimeByGoalName(updatedGoal.getGoalName(),timeSpent);
                        notifyItemChanged(holder.getAdapterPosition());
                    }else if (savedWeekNum+1<AppUtils.getCurrentWeekNum()){ //If the current week is 2 or more than the saved week, that means the user just left it on all week, by mistake or to cheat the system!
                        Toast.makeText(compoundButton.getContext(), "So.... you spent every minute of your last week on your goal?", Toast.LENGTH_LONG).show();
                    }

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
    public void createNotification(Context context, Goal selectedGoal){
        NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(context);
        noteBuilder
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("You're on the clock")
                .setContentText("You've selected to work on " + selectedGoal.getGoalName()+". Work diligently!")
                .setOngoing(true);

        mNotificationManager.notify(AppConstants.NOTIFICATION_ID,noteBuilder.build());

    }

    @Override
    public int getItemCount() {
        return mGoals.size();
    }
}
