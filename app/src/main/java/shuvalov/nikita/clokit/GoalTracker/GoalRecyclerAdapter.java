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
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
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

public class GoalRecyclerAdapter extends RecyclerView.Adapter<GoalViewHolder> {
    private ArrayList<Goal> mGoals;
    private OnGoalChangeListener mGoalChangeListener;
    private boolean mGoalEdited;
    private Goal mCachedGoal;

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
        final SharedPreferences sharedPreferences = holder.mToggleButton.getContext().getSharedPreferences(AppConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        Goal goal = mGoals.get(holder.getAdapterPosition());
        holder.bindDataToViews(goal);
        String activeGoalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
        String subCatName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, null);
        holder.mToggleButton.setOnCheckedChangeListener(null); //If the holder is being reloaded the onCheckedListener is already attached; it needs to be removed because setChecked() because triggers onCheckedChangeListener;
        holder.mToggleButton.setChecked(AppUtils.isGoalCurrentlyActive(goal, activeGoalName, subCatName));

        holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                String activeGoalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
                String subCatName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, null);
                final Goal goal = mGoals.get(holder.getAdapterPosition());
                if (AppUtils.isGoalCurrentlyActive(goal, activeGoalName, subCatName)) {
                    Toast.makeText(view.getContext(), "An active goal can't be removed", Toast.LENGTH_SHORT).show();
                } else if (goal.getCurrentMilli() > 0) {
                    Log.d("Test", "Before Goal Size: " + mGoals.size());
                    warnUser(goal, holder);
                } else {
                    Log.d("Test", "Before Goal Size: " + mGoals.size());
                    removeGoalLogic(holder, goal);
                }
            }
        });


        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String activeGoalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
                String subCatName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, null);
                mGoalEdited = false;
                final Goal goal = mGoals.get(holder.getAdapterPosition());
                mCachedGoal = goal;
                //Only allow editing if there is no active goal, or if the goal ISN'T the goal that's active.
                if (!AppUtils.isGoalCurrentlyActive(goal, activeGoalName, subCatName)) {
                    openEditDialog(view.getContext(), holder.getAdapterPosition());
//                    final View dialogView = LayoutInflater.from(view.getContext()).inflate(R.layout.dialog_editgoal, null);
//                    AlertDialog alertDialog = new AlertDialog.Builder(dialogView.getContext())
//                            .setTitle("Edit Goal")
//                            .setView(dialogView)
//                            .setPositiveButton("Save Changes", null)
//                            .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                                @Override
//                                public void onClick(DialogInterface dialogInterface, int i) {
//                                    dialogInterface.dismiss();
//                                }
//                            })
//                            .create();
//                    alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
//                        @Override
//                        public void onShow(final DialogInterface dialogInterface) {
//                            final EditText hourEntry = (EditText) dialogView.findViewById(R.id.hour_entry);
//                            final EditText minuteEntry = (EditText) dialogView.findViewById(R.id.minute_entry);
//
//                            Button addTimeButt = (Button) dialogView.findViewById(R.id.add_time_butt);
//                            Button removeTimeButt = (Button) dialogView.findViewById(R.id.remove_time_butt);
//                            Button zeroTimeButt = (Button) dialogView.findViewById(R.id.set_zer0_butt);
//
//                            Button saveChangesButt = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);
//
//                            zeroTimeButt.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    mCachedGoal.setCurrentMilli(0);
//                                    mGoalEdited = true;
//                                }
//                            });
//
//                            addTimeButt.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    String hours = hourEntry.getText().toString();
//                                    String minutes = minuteEntry.getText().toString();
//                                    if (hours.isEmpty() && minutes.isEmpty()) {
//                                        hourEntry.setError("Need a value to add time");
//                                        minuteEntry.setError("Need a value to add time");
//                                    } else {
//                                        long hourValue = 0, minuteValue = 0;
//                                        if (!hours.isEmpty()) {
//                                            hourValue = Long.valueOf(hours) * 60000 * 60;
//                                        }
//                                        if (!minutes.isEmpty()) {
//                                            minuteValue = Long.valueOf(minutes) * 60000;
//                                        }
//                                        long totalTimeAdd = hourValue + minuteValue;
//                                        if (totalTimeAdd == 0) {
//                                            Toast.makeText(view.getContext(), "To leave goal unchanged press Cancel instead", Toast.LENGTH_LONG).show();
//                                        }else{
//                                            mCachedGoal.addTimeSpent(totalTimeAdd);
//                                            mGoalEdited = true;
//                                        }
//                                    }
//                                }
//                            });
//
//                            removeTimeButt.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    String hours = hourEntry.getText().toString();
//                                    String minutes = minuteEntry.getText().toString();
//                                    if (hours.isEmpty() && minutes.isEmpty()) {
//                                        hourEntry.setError("Need a value to add time");
//                                        minuteEntry.setError("Need a value to add time");
//                                    } else {
//                                        long hourValue = Long.valueOf(hourEntry.getText().toString()) * 60000 * 60;
//                                        long minuteValue = Long.valueOf(minuteEntry.getText().toString()) * 60000;
//                                        long timeRemoved = hourValue + minuteValue;
//                                        if (timeRemoved == 0) {
//                                            Toast.makeText(view.getContext(), "To leave goal unchanged press Cancel instead", Toast.LENGTH_LONG).show();
//                                        }else{
//                                            mCachedGoal.removeTimeSpent(timeRemoved);
//                                            mGoalEdited=true;
//                                        }
//                                    }
//                                }
//                            });
//                            saveChangesButt.setOnClickListener(new View.OnClickListener() {
//                                @Override
//                                public void onClick(View view) {
//                                    if (mGoalEdited) {
//                                        goal.applyChangedValues(mCachedGoal);
//                                        Toast.makeText(view.getContext(), "Changes applied", Toast.LENGTH_SHORT).show();
//                                        mGoalChangeListener.goalValuesChanged();
//                                        dialogInterface.dismiss();
//                                    } else {
//                                        Toast.makeText(view.getContext(), "No changes were made", Toast.LENGTH_SHORT).show();
//                                    }
//                                }
//                            });
//                        }
//                    });
//                    alertDialog.show();
                } else { //If user tries to edit an active goal.
                    Toast.makeText(view.getContext(), "Can't edit an active task", Toast.LENGTH_SHORT).show();
                }

            }
        });

        holder.mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String currentGoalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
                String currentSubCat = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, null);
                Goal selectedGoal = mGoals.get(holder.getAdapterPosition());
                final long startTime = sharedPreferences.getLong(AppConstants.PREFERENCES_START_TIME, -1);
                int savedWeekOfYear = AppUtils.getWeekOfYear(startTime);
                int currentWeekOfYear = AppUtils.getWeekOfYear(System.currentTimeMillis());
                if (currentGoalName.equals(AppConstants.PREFERENCES_NO_GOAL)) { //If no goal is currently active, then allow this goal to become new goal.
                    AppUtils.setActiveGoalToPreferences(sharedPreferences, selectedGoal);
                    startNotificationService(holder.mToggleButton.getContext());
                } else if (AppUtils.isGoalCurrentlyActive(selectedGoal, currentGoalName, currentSubCat)) {
                    long currentTime = System.currentTimeMillis();
                    int savedWeekNum = sharedPreferences.getInt(AppConstants.PREFERENCES_CURRENT_GOAL_WEEK_NUM, -1);
                    AppUtils.resetActiveGoalPreferences(sharedPreferences);
                    if (savedWeekNum == -1) {
                        Log.e("GoalRecyclerAdapter", "Missing preference value", new Exception());
                    }else if (savedWeekOfYear+1 == currentWeekOfYear) { //The task crossed over into a second week.
                        //This snippet of code can only be called RARELY and is a fail-safe.
                        // This only happens if the user leaves the app open with screen on when passing into the new week.
                        // Otherwise, the goals should be reset in the homefragment onResume.
                        Log.d("GoalRecyclerAdapter", "Crossed weeks");

                        long weekEndTime = AppUtils.getWeekEndMillis(savedWeekNum);
                        long timeSpentLastWeek = weekEndTime - startTime;

                        updateAllGoalReferences(holder, timeSpentLastWeek, savedWeekNum, sharedPreferences);
                        setUpThisWeek(holder,sharedPreferences);

                        mGoalChangeListener.goalValuesChanged();

                        stopNotification(compoundButton.getContext());
                    } else if (currentWeekOfYear == savedWeekOfYear) { //The task started and ended in the same week, simply update values.
                        long timeSpent = currentTime - startTime;
                        updateAllGoalReferences(holder, timeSpent, savedWeekNum, sharedPreferences);
                        stopNotification(compoundButton.getContext());
                    } else if (savedWeekOfYear + 1 < currentWeekOfYear) { //If the current week is 2 or more than the saved week, that means the user just left it on all week, by mistake or to cheat the system!
                        Toast.makeText(compoundButton.getContext(), "So.... you spent every minute of your last week on your goal?", Toast.LENGTH_LONG).show();
                        //ToDo: Achievement for forgetting to turn off tracking over the course of a week.
                        stopNotification(compoundButton.getContext());
                    }
                    if (startTime == -1) {
                        Log.e("GoalRecyclerAdapter", "onCheckedChanged: ", new IllegalArgumentException("Error in retrieving start time of previous goal."));
                    }
                } else { //If there is a current goal, and the goal is not the selected goal
                    if (b) {
                        Toast.makeText(compoundButton.getContext(), "There is currently another active goal", Toast.LENGTH_SHORT).show();
                        compoundButton.setChecked(false); //This counters the user's click of the button, otherwise it'd falsely be set as checked.
                    }
                }
            }
        });
    }

    public void setUpThisWeek(GoalViewHolder holder, SharedPreferences sharedPreferences){
        int newWeekNum = AppUtils.getCurrentWeekNum();
        long weekStartTime = AppUtils.getWeekStartMillis(newWeekNum);
        long timeSpentThisWeek = System.currentTimeMillis() - weekStartTime;

        Goal newGoal = mGoals.get(holder.getAdapterPosition());
        newGoal.setCurrentMilli(0);
        newGoal.setWeekNum(newWeekNum);

        GoalSQLHelper sqlHelper = GoalSQLHelper.getInstance(holder.mRemoveButton.getContext());

        sqlHelper.addGoalToWeeklyTable(newGoal);
        sqlHelper.addNewWeekReference(new Week(weekStartTime, AppUtils.getWeekEndMillis(newWeekNum), newWeekNum));
        CurrentWeekGoalManager.getInstance().addCurrentGoal(newGoal);

        AppUtils.addLifeTimeTotalTrackedTime(sharedPreferences, timeSpentThisWeek);
        AppUtils.setActiveGoalToPreferences(sharedPreferences,newGoal);
    }

    private void warnUser(final Goal goal, final GoalViewHolder holder) {
        AlertDialog alertDialog = new AlertDialog.Builder(holder.mRemoveButton.getContext()).setMessage("Time spent on this goal for this week will be deleted (Lifetime tracking stats will be unaffected).\nPress \"Okay\" to remove goal for this week.")
                .setTitle("Are you sure?")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeGoalLogic(holder, goal);
                        dialogInterface.dismiss();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
    }

    private void removeGoalLogic(GoalViewHolder holder, Goal goal) {
        String weekNum = String.valueOf(goal.getWeekNum());
        GoalSQLHelper goalSQLHelper = GoalSQLHelper.getInstance(holder.mRemoveButton.getContext());
        int rowsremoved = goalSQLHelper.removeCurrentGoal(goal.getGoalName(), goal.getSubCategory(), weekNum);
        if (rowsremoved > 1) {
            Log.e("GoalRecyclerAdapter", "Removed: " + rowsremoved, new Exception("Excessive amount of rows deleted"));
        }
        if (CurrentWeekGoalManager.getInstance().removeGoal(goal)) {
            mGoalChangeListener.goalValuesChanged();
            notifyItemRemoved(holder.getAdapterPosition());
            Log.d("Test", "After Goal size: " + mGoals.size());
        } else {
            Log.w("GoalRecyclerAdapter", "onClick: ", new Exception("Couldn't remove goal because it wasn't found in CurrentWeekGoalManager"));
        }
        if (mGoals.size() == 0) {
            int removedRows = goalSQLHelper.removeWeekReference(AppUtils.getCurrentWeekNum());
            if (removedRows == 0) { //If a non-existing reference was removed, it's not a big deal, but could be optimized.
                Log.w("GoalRecyclerAdapter", "Tried to remove a week reference that didn't exist");
            } else if (removedRows > 1) { //If more than one row is being deleted then we have a problem.
                Log.e("GoalRecyclerAdapter", "Removed: " + rowsremoved, new Exception("Excessive amount of rows deleted"));
            }
            Log.d("GoalRecyclerAdapter", "Removed :" + removedRows);
        }
    }

    public void startNotificationService(Context context) {
        Intent intent = new Intent(context, GoalTrackerIntentService.class);
        //Consider putting name of goal in the intent instead.
        context.startService(intent);
    }

    public void stopNotification(Context context) {
        NotificationManager noteMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        noteMan.cancel(AppConstants.NOTIFICATION_ID);
        Intent intent = new Intent(context, GoalTrackerIntentService.class);
        context.stopService(intent);
    }

    @Override
    public int getItemCount() {
        return mGoals.size();
    }


    public interface OnGoalChangeListener {
        void goalValuesChanged();
    }

    public void updateAllGoalReferences(GoalViewHolder holder, long timeSpent, int savedWeekNum, SharedPreferences sharedPreferences){
        //Updates selected goal.
        Goal updatedGoal = mGoals.get(holder.getAdapterPosition());
        updatedGoal.addTimeSpent(timeSpent);
        Week week = new Week(AppUtils.getWeekStartMillis(savedWeekNum), AppUtils.getWeekEndMillis(savedWeekNum), savedWeekNum);

        //Make changes in SQL db
        GoalSQLHelper sqlHelper = GoalSQLHelper.getInstance(holder.mToggleButton.getContext());
        sqlHelper.updateTimeSpentOnGoal(updatedGoal); //Updates weekly stats
        sqlHelper.updateLifetimeByGoalName(updatedGoal.getGoalName(), timeSpent); //Updates lifetime stats
        sqlHelper.addNewWeekReference(week);//Adds this week as an active week for reference in history view, the method ignores duplicate entries.

        //Update the visuals
        notifyItemChanged(holder.getAdapterPosition());
        mGoalChangeListener.goalValuesChanged();

        AppUtils.addLifeTimeTotalTrackedTime(sharedPreferences, timeSpent);
    }

    public void openEditDialog(Context context, final int holderPosition){
        final View dialogView = LayoutInflater.from(context).inflate(R.layout.dialog_editgoal, null);
        AlertDialog alertDialog = new AlertDialog.Builder(dialogView.getContext())
                .setTitle("Edit Goal")
                .setView(dialogView)
                .setPositiveButton("Save Changes", null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                })
                .create();
        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                final EditText hourEntry = (EditText) dialogView.findViewById(R.id.hour_entry);
                final EditText minuteEntry = (EditText) dialogView.findViewById(R.id.minute_entry);

                Button addTimeButt = (Button) dialogView.findViewById(R.id.add_time_butt);
                Button removeTimeButt = (Button) dialogView.findViewById(R.id.remove_time_butt);
                Button zeroTimeButt = (Button) dialogView.findViewById(R.id.set_zer0_butt);

                Button saveChangesButt = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);

                zeroTimeButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        mCachedGoal.setCurrentMilli(0);
                        mGoalEdited = true;
                    }
                });

                addTimeButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String hours = hourEntry.getText().toString();
                        String minutes = minuteEntry.getText().toString();
                        if (hours.isEmpty() && minutes.isEmpty()) {
                            hourEntry.setError("Need a value to add time");
                            minuteEntry.setError("Need a value to add time");
                        } else {
                            long hourValue = 0, minuteValue = 0;
                            if (!hours.isEmpty()) {
                                hourValue = Long.valueOf(hours) * 60000 * 60;
                            }
                            if (!minutes.isEmpty()) {
                                minuteValue = Long.valueOf(minutes) * 60000;
                            }
                            long totalTimeAdd = hourValue + minuteValue;
                            if (totalTimeAdd == 0) {
                                Toast.makeText(view.getContext(), "To leave goal unchanged press Cancel instead", Toast.LENGTH_LONG).show();
                            }else{
                                mCachedGoal.addTimeSpent(totalTimeAdd);
                                mGoalEdited = true;
                            }
                        }
                    }
                });

                removeTimeButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        String hours = hourEntry.getText().toString();
                        String minutes = minuteEntry.getText().toString();
                        if (hours.isEmpty() && minutes.isEmpty()) {
                            hourEntry.setError("Need a value to add time");
                            minuteEntry.setError("Need a value to add time");
                        } else {
                            long hourValue = 0, minuteValue = 0;
                            if(!hours.isEmpty()){
                                hourValue = Long.valueOf(hourEntry.getText().toString()) * 60000 * 60;
                            }
                            if(!minutes.isEmpty()){
                                minuteValue = Long.valueOf(minuteEntry.getText().toString()) * 60000;
                            }
                            long timeRemoved = hourValue + minuteValue;
                            if (timeRemoved == 0) {
                                Toast.makeText(view.getContext(), "To leave goal unchanged press Cancel instead", Toast.LENGTH_LONG).show();
                            }else{
                                mCachedGoal.removeTimeSpent(timeRemoved);
                                mGoalEdited=true;
                            }
                        }
                    }
                });
                saveChangesButt.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if (mGoalEdited) {
                            Goal goal = mGoals.get(holderPosition);
                            goal.applyChangedValues(mCachedGoal);
                            Toast.makeText(view.getContext(), "Changes applied", Toast.LENGTH_SHORT).show();
                            mGoalChangeListener.goalValuesChanged();
                            //ToDo: Need to update the SQL db on the amount changed otherwise I'm just changing it in the short-term manager.
                            dialogInterface.dismiss();
                        } else {
                            Toast.makeText(view.getContext(), "No changes were made", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });
        alertDialog.show();

    }
}