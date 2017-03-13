package shuvalov.nikita.clokit.goaltracker;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.Toast;

import java.util.ArrayList;

import shuvalov.nikita.clokit.AppConstants;
import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.GoalSQLHelper;
import shuvalov.nikita.clokit.pojos.Goal;
import shuvalov.nikita.clokit.pojos.Week;
import shuvalov.nikita.clokit.R;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class GoalRecyclerAdapter extends RecyclerView.Adapter<GoalViewHolder>{
    private ArrayList<Goal> mGoals;
    private OnGoalChangeListener mGoalChangeListener;

    public GoalRecyclerAdapter(ArrayList<Goal> goals, OnGoalChangeListener goalChangeListener) {
        mGoals = goals;
        mGoalChangeListener = goalChangeListener;
    }

    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new GoalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_goal, null));
    }

    @Override
    public void onBindViewHolder(final GoalViewHolder holder, int position) {
        final SharedPreferences sharedPreferences = holder.mToggleButton.getContext().getSharedPreferences(AppConstants.PREFERENCES_NAME,Context.MODE_PRIVATE);
        Goal goal = mGoals.get(holder.getAdapterPosition());
        holder.bindDataToViews(goal);
        String activeGoalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
        String subCatName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, null);
        holder.mToggleButton.setOnCheckedChangeListener(null); //If the holder is being reloaded the onCheckedListener is already attached; it needs to be removed because setChecked() because triggers onCheckedChangeListener;
        if(activeGoalName.equals(goal.getGoalName())
                && ((subCatName==null && goal.getSubCategory()==null) || (subCatName!=null && goal.getSubCategory()!=null && subCatName.equals(goal.getSubCategory())))){ //This visually toggles the active goal ON.
            holder.mToggleButton.setChecked(true);
            Log.d("Holder", "onBindViewHolder: "+ holder.getAdapterPosition());
        }else{
            holder.mToggleButton.setChecked(false);
        }


        holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String activeGoalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
                String subCatName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, null);
                final Goal goal = mGoals.get(holder.getAdapterPosition());
                Log.d("ADAPTER", "onClick: "+ goal.getGoalName() + goal.getSubCategory());
                if((goal.getGoalName().equals(activeGoalName)) &&
                        ((goal.getSubCategory()==null && subCatName==null) || (goal.getSubCategory()!=null && goal.getSubCategory().equals(subCatName)))){
                    Toast.makeText(view.getContext(), "An active goal can't be removed", Toast.LENGTH_SHORT).show();
                }else if (goal.getCurrentMilli()>0){
                    Log.d("Test", "Before Goal Size: "+ mGoals.size());
                    AlertDialog alertDialog = new AlertDialog.Builder(view.getContext()).setMessage("Time spent on this goal for this week will be deleted (Lifetime tracking stats will be unaffected).\nPress \"Okay\" to remove goal for this week.")
                            .setTitle("Are you sure?")
                            .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    String weekNum = String.valueOf(goal.getWeekNum());
                                    GoalSQLHelper goalSQLHelper = GoalSQLHelper.getInstance(view.getContext());
                                    int rowsremoved = goalSQLHelper.removeCurrentGoal(goal.getGoalName(), goal.getSubCategory(), weekNum);
                                    if(rowsremoved>1){
                                        Log.e("GoalRecyclerAdapter", "Removed: "+rowsremoved, new Exception("Excessive amount of rows deleted"));
                                    }
                                    if(CurrentWeekGoalManager.getInstance().removeGoal(goal)){
                                        notifyItemRemoved(holder.getAdapterPosition());
                                        Log.d("Test", "After Goal size: " + mGoals.size());
                                    }else{
                                        Log.w("GoalRecyclerAdapter", "onClick: ", new Exception("Couldn't remove goal because it wasn't found in CurrentWeekGoalManager"));
                                    }
                                    if(mGoals.size()==0){
                                        int removedRows = goalSQLHelper.removeWeekReference(AppUtils.getCurrentWeekNum());
                                        if(removedRows==0){ //If a non-existing reference was removed, it's not a big deal, but could be optimized.
                                            Log.w("GoalRecyclerAdapter", "Tried to remove a week reference that didn't exist");
                                        }else if (removedRows>1){ //If more than one row is being deleted then we have a problem.
                                            Log.e("GoalRecyclerAdapter", "Removed: "+ rowsremoved,new Exception("Excessive amount of rows deleted"));
                                        }
                                        Log.d("GoalRecyclerAdapter", "Removed :"+removedRows);
                                        mGoalChangeListener.goalValuesChanged();
                                        dialogInterface.dismiss();
                                    }
                                }
                            })
                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialogInterface, int i) {
                                    dialogInterface.dismiss();
                                }
                            }).create();
                    alertDialog.show();
                }else{
                    Log.d("Test", "Before Goal Size: "+ mGoals.size());
                    GoalSQLHelper goalSQLHelper = GoalSQLHelper.getInstance(view.getContext());
                    String weekNum = String.valueOf(goal.getWeekNum());
                    int i = goalSQLHelper.removeCurrentGoal(goal.getGoalName(), goal.getSubCategory(), weekNum);
                    if(i>1){
                        Log.e("GoalRecyclerAdapter", "Removed: "+i, new Exception("Excessive amount of rows deleted"));
                    }
                    if(CurrentWeekGoalManager.getInstance().removeGoal(goal)){
                        notifyItemRemoved(holder.getAdapterPosition());
                        mGoalChangeListener.goalValuesChanged();
                        Log.d("Test", "After Goal size: " + mGoals.size());
                    }else{
                        Log.w("GoalRecyclerAdapter", "onClick: ", new Exception("Couldn't remove goal because it wasn't found in CurrentWeekGoalManager"));
                    }
                    if(mGoals.size()==0){
                        int p = goalSQLHelper.removeWeekReference(AppUtils.getCurrentWeekNum());
                        Log.d("GoalRecyclerAdapter", "Removed :"+p);
                    }
                }
            }
        });


//        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                //ToDo: Logic for editing or removing entry.
//            }
//        });


        holder.mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String currentGoalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL,AppConstants.PREFERENCES_NO_GOAL);
                String currentSubCat = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, null);
                Goal selectedGoal = mGoals.get(holder.getAdapterPosition());
                if(currentGoalName.equals(AppConstants.PREFERENCES_NO_GOAL)){ //If no goal is currently active, then allow this goal to become new goal.
                    sharedPreferences.edit().putString(AppConstants.PREFERENCES_CURRENT_GOAL,selectedGoal.getGoalName()).apply(); //Save name of current goal.
                    sharedPreferences.edit().putLong(AppConstants.PREFERENCES_START_TIME, System.currentTimeMillis()).apply(); //Save the current time of the selected goal.
                    sharedPreferences.edit().putInt(AppConstants.PREFERENCES_CURRENT_GOAL_WEEK_NUM, AppUtils.getCurrentWeekNum()).apply(); //Adds the weeknum of when the goal was started
                    sharedPreferences.edit().putString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, selectedGoal.getSubCategory()).apply(); //Saves the subcategory of current goal.

                    startNotificationService(holder.mToggleButton.getContext());
                } else if(currentGoalName.equals(selectedGoal.getGoalName()) && ((currentSubCat==null && selectedGoal.getSubCategory()==null)||(currentSubCat!=null && currentSubCat.equals(selectedGoal.getSubCategory())))){ //If selected goal is the current goal, then we end current goal.
                    long currentTime = System.currentTimeMillis();

                    sharedPreferences.edit().putString(AppConstants.PREFERENCES_CURRENT_GOAL,AppConstants.PREFERENCES_NO_GOAL).apply();
                    sharedPreferences.edit().putString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, null).apply();
                    long startTime = sharedPreferences.getLong(AppConstants.PREFERENCES_START_TIME, -1);

                    int savedWeekNum = sharedPreferences.getInt(AppConstants.PREFERENCES_CURRENT_GOAL_WEEK_NUM, -1);
                    if(savedWeekNum==-1){
                        Log.e("GoalRecyclerAdapter", "Missing preference value", new Exception());
                    }
                    if(savedWeekNum == AppUtils.getCurrentWeekNum()-1){ //The task crossed over into a second week. Divide the task time between the goals of both weeks
                        Log.d("GoalRecyclerAdapter", "Crossed weeks");
                        //Gets the time spent on task for last week by using the task start time and endOfWeekTime, then updates the values in the Database accordingly.
                        long weekEndTime = AppUtils.getWeekEndMillis(savedWeekNum);
                        long timeSpentLastWeek = weekEndTime-startTime;
                        selectedGoal.addTimeSpent(timeSpentLastWeek);
                        GoalSQLHelper sqlHelper = GoalSQLHelper.getInstance(compoundButton.getContext());
                        sqlHelper.updateTimeSpentOnGoal(selectedGoal);
                        sqlHelper.addNewWeekReference(new Week(AppUtils.getWeekStartMillis(savedWeekNum),weekEndTime,savedWeekNum));


                        //Gets the time spent on task for this week by using the week start time and task end time, then adds the goal to the weekly table, the GoalManager, and the Database.
                        //Since the new goal is the same as the last week's goal, the user may want to edit their goal time.
                        long weekStartTime = AppUtils.getWeekStartMillis(savedWeekNum+1);
                        long timeSpentThisWeek = System.currentTimeMillis() - weekStartTime;
                        selectedGoal.setWeekNum(AppUtils.getCurrentWeekNum());
                        selectedGoal.setCurrentMilli(timeSpentThisWeek);
                        sqlHelper.addGoalToWeeklyTable(selectedGoal);
                        sqlHelper.addNewWeekReference(new Week(weekStartTime,AppUtils.getWeekEndMillis(savedWeekNum+1), savedWeekNum+1));
                        CurrentWeekGoalManager.getInstance().addCurrentGoal(selectedGoal);

                        long previousTotalTime = sharedPreferences.getLong(AppConstants.PREFERENCES_TOTAL_TRACKED_TIME, 0);
                        long newTotal = previousTotalTime+timeSpentLastWeek+ timeSpentLastWeek;
                        sharedPreferences.edit().putLong(AppConstants.PREFERENCES_TOTAL_TRACKED_TIME, newTotal).apply();
                        mGoalChangeListener.goalValuesChanged();

                        stopNotification(compoundButton.getContext());
                    }else if (savedWeekNum == AppUtils.getCurrentWeekNum()){ //The task started and ended in the same week, simply update values.
                        Log.d("GoalRecyclerAdapter", "Same week ");
                        long timeSpent = currentTime-startTime;
                        Goal updatedGoal = mGoals.get(holder.getAdapterPosition());
                        updatedGoal.addTimeSpent(timeSpent); //Updates the value in the adapter.
                        GoalSQLHelper sqlHelper = GoalSQLHelper.getInstance(compoundButton.getContext());
                        sqlHelper.updateTimeSpentOnGoal(updatedGoal); //Updates weekly stats
                        sqlHelper.updateLifetimeByGoalName(updatedGoal.getGoalName(),timeSpent); //Updates lifetime stats
                        Log.d("GoalRecyclerAdapter", "SavedWeekNum: "+ savedWeekNum);
                        Week week = new Week(AppUtils.getWeekStartMillis(savedWeekNum), AppUtils.getWeekEndMillis(savedWeekNum), savedWeekNum);
                        Log.d("GoalRecyclerAdapter", "Week Start:"+week.getStartTime()+" End: "+ week.getEndTime());
                        sqlHelper.addNewWeekReference(week);//Adds this week as an active week for reference in history view, the method ignores duplicate entries.
                        notifyItemChanged(holder.getAdapterPosition());
                        long previousTotalTime = sharedPreferences.getLong(AppConstants.PREFERENCES_TOTAL_TRACKED_TIME, 0);
                        mGoalChangeListener.goalValuesChanged();
                        sharedPreferences.edit().putLong(AppConstants.PREFERENCES_TOTAL_TRACKED_TIME, timeSpent+previousTotalTime).apply();
                        stopNotification(compoundButton.getContext());
                    }else if (savedWeekNum+1<AppUtils.getCurrentWeekNum()){ //If the current week is 2 or more than the saved week, that means the user just left it on all week, by mistake or to cheat the system!
                        Toast.makeText(compoundButton.getContext(), "So.... you spent every minute of your last week on your goal?", Toast.LENGTH_LONG).show();
                    }
                    if(startTime == -1){
                        Log.e("GoalRecyclerAdapter", "onCheckedChanged: ", new IllegalArgumentException("Error in retrieving start time of previous goal."));
                    }
                }
                else{ //If there is a current goal, and the goal is not the selected goal
                    if(b){
                        Toast.makeText(compoundButton.getContext(), "There is currently another active goal", Toast.LENGTH_SHORT).show();
                        compoundButton.setChecked(false);
                    }
                }
            }
        });
    }
    public void startNotificationService(Context context){
        Intent intent = new Intent(context, GoalTrackerService.class);
        intent.putExtra(AppConstants.INTENT_MINUTES_LAST_SOUND, 0);
        context.startService(intent);
    }

    public void stopNotification(Context context){
        NotificationManager noteMan = (NotificationManager)context.getSystemService(Context.NOTIFICATION_SERVICE);
        noteMan.cancel(AppConstants.NOTIFICATION_ID);
        Intent intent = new Intent(context, GoalTrackerService.class);
        context.stopService(intent);
    }

    @Override
    public int getItemCount() {
        return mGoals.size();
    }

    public interface OnGoalChangeListener{
        public void goalValuesChanged();
    }

}
