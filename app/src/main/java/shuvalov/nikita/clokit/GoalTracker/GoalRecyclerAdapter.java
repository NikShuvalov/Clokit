package shuvalov.nikita.clokit.goaltracker;

import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
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

import static android.content.ContentValues.TAG;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

//ToDo: Idea? Make another class to manage user input so that less logic can go in here.
public class GoalRecyclerAdapter extends RecyclerView.Adapter<GoalViewHolder> implements PopupMenu.OnMenuItemClickListener, ProgressEditorAlertDialogFactory.OnChangeConfirmListener{
    private ArrayList<Goal> mGoals;
    private OnGoalChangeListener mGoalChangeListener;
    private GoalViewHolder mSelectedHolder;
    private ProgressEditorAlertDialogFactory mProgressEditorAlertDialogFactory;

    public GoalRecyclerAdapter(ArrayList<Goal> goals, OnGoalChangeListener goalChangeListener) {
        mGoals = goals;
        mGoalChangeListener = goalChangeListener;
    }

    @Override
    public GoalViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int parentSize = parent.getWidth() < parent.getHeight() ? parent.getWidth() :
                (int)(parent.getWidth()/2.05);
        return new GoalViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_goal, null), parentSize);
    }

    @Override
    public void onBindViewHolder(final GoalViewHolder holder, int position) {
        SharedPreferences sharedPreferences = holder.mToggleButton.getContext().getSharedPreferences(AppConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        Goal goal = mGoals.get(holder.getAdapterPosition());
        holder.bindDataToViews(goal);
        String activeGoalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
        String subCatName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, null);
        holder.mToggleButton.setOnCheckedChangeListener(null); //If the holder is being reloaded the onCheckedListener is already attached; it needs to be removed because setChecked() because triggers onCheckedChangeListener;
        holder.mToggleButton.setChecked(AppUtils.isGoalCurrentlyActive(goal, activeGoalName, subCatName));
        setRemoveClickerLogic(holder);
        setEditClickerLogic(holder);
        setToggleCheckLogic(holder, sharedPreferences);
        setContainerClickOptions(holder);
    }

    private void setContainerClickOptions(final GoalViewHolder holder){
        holder.mContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedHolder = holder;
                PopupMenu popupMenu = new PopupMenu(mSelectedHolder.mContainer.getContext(),view);
                popupMenu.inflate(R.menu.menu_popup);
                popupMenu.show();
                popupMenu.setOnMenuItemClickListener(GoalRecyclerAdapter.this);
            }
        });
    }


    @Override
    public boolean onMenuItemClick(MenuItem item) {
        Context context = mSelectedHolder.mContainer.getContext();
        switch(item.getItemId()){
            case R.id.popup_adjust_progress_opt:
                editGoalLogic(context);
                return true;
            case R.id.popup_remove_opt:
                removeGoalLogic(context, mSelectedHolder);
                return true;
            default:
                return false;
        }
    }

    private void setRemoveClickerLogic(final GoalViewHolder holder){
        holder.mRemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                SharedPreferences sharedPreferences = view.getContext().getSharedPreferences(AppConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
                String activeGoalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
                String subCatName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, null);
                final Goal goal = mGoals.get(holder.getAdapterPosition());
                if (AppUtils.isGoalCurrentlyActive(goal, activeGoalName, subCatName)) {
                    Toast.makeText(view.getContext(), "An active goal can't be removed", Toast.LENGTH_SHORT).show();
                } else if (goal.getCurrentMilli() > 0) {
                    warnUser(goal, holder);
                } else {
                    removeGoal(holder, goal);
                }
            }
        });

    }

    private void removeGoalLogic(Context context, GoalViewHolder holder){
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        String activeGoalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
        String subCatName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, null);
        Goal goal = mGoals.get(holder.getAdapterPosition());
        if (AppUtils.isGoalCurrentlyActive(goal, activeGoalName, subCatName)) {
            Toast.makeText(context, "An active goal can't be removed", Toast.LENGTH_SHORT).show();
        } else if (goal.getCurrentMilli() > 0) {
            warnUser(goal, holder);
        } else {
            removeGoal(holder, goal);
        }
    }

    private void setEditClickerLogic(final GoalViewHolder holder){
        holder.mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mSelectedHolder = holder;
                editGoalLogic(view.getContext());
            }
        });
    }


    private void editGoalLogic(Context context){
        SharedPreferences sharedPreferences = context.getSharedPreferences(AppConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        String activeGoalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
        String subCatName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, null);
        Goal goal = mGoals.get(mSelectedHolder.getAdapterPosition());
        if (!AppUtils.isGoalCurrentlyActive(goal, activeGoalName, subCatName)) {
            mProgressEditorAlertDialogFactory = new ProgressEditorAlertDialogFactory(context, goal.createCachedCopy(), GoalRecyclerAdapter.this);
            AlertDialog progressEditorDialog= mProgressEditorAlertDialogFactory.getAlertDialog();
            progressEditorDialog.show();
        } else { //If user tries to edit an active goal.
            Toast.makeText(context, "Can't edit an active task", Toast.LENGTH_SHORT).show();
        }
    }


    //FixMe: Make shorter... later
    private void setToggleCheckLogic(final GoalViewHolder holder, final SharedPreferences sharedPreferences){
        holder.mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                String currentGoalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
                String currentSubCat = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, null);
                Goal selectedGoal = mGoals.get(holder.getAdapterPosition());
                if (currentGoalName.equals(AppConstants.PREFERENCES_NO_GOAL)) { //If no goal is currently active, then allow this goal to become new goal.
                    AppUtils.setActiveGoalToPreferences(sharedPreferences, selectedGoal);
                    startNotificationService(holder.mToggleButton.getContext());
                } else if (AppUtils.isGoalCurrentlyActive(selectedGoal, currentGoalName, currentSubCat)) {
                    handleRecordingTime(compoundButton.getContext(),sharedPreferences,holder);
                } else { //If there is a current goal, and the goal is not the selected goal
                    if (b) {
                        Toast.makeText(compoundButton.getContext(), "There is currently another active goal", Toast.LENGTH_SHORT).show();
                        compoundButton.setChecked(false); //This counters the user's click of the button, otherwise it'd falsely be set as checked.
                    }
                }
            }
        });
    }

    private void handleRecordingTime(Context context, SharedPreferences sharedPreferences, GoalViewHolder holder){
        long startTime = sharedPreferences.getLong(AppConstants.PREFERENCES_START_TIME, -1);
        int savedWeekOfYear = AppUtils.getWeekOfYear(startTime);
        int currentWeekOfYear = AppUtils.getWeekOfYear(System.currentTimeMillis());
        long currentTime = System.currentTimeMillis();
        int savedWeekNum = sharedPreferences.getInt(AppConstants.PREFERENCES_CURRENT_GOAL_WEEK_NUM, -1);
        AppUtils.resetActiveGoalPreferences(sharedPreferences);
        if (savedWeekNum == -1) {
            Log.e("GoalRecyclerAdapter", "Missing preference value", new Exception("Expected Preference Value not found"));
        }else if (savedWeekOfYear+1 == currentWeekOfYear) { //The task crossed over into a second week.
            // This snippet of code can only be called RARELY and is a fail-safe.
            // This only happens if the user leaves the app open with screen on when passing into the new week.
            // Otherwise, the goals should be reset in the homefragment onResume.
            long weekEndTime = AppUtils.getWeekEndMillis(savedWeekNum);
            long timeSpentLastWeek = weekEndTime - startTime;
            updateAllGoalReferences(holder, timeSpentLastWeek, savedWeekNum, sharedPreferences);
            setUpThisWeek(holder,sharedPreferences);
            //FixMe: I think this means it cancels the previous week's goal but then sets up this week's goal to be active.
            mGoalChangeListener.goalValuesChanged();

        } else if (currentWeekOfYear == savedWeekOfYear) { //The task started and ended in the same week, simply update values.
            long timeSpent = currentTime - startTime;
            updateAllGoalReferences(holder, timeSpent, savedWeekNum, sharedPreferences);
        } else if (savedWeekOfYear + 1 < currentWeekOfYear) { //If the current week is 2 or more than the saved week, that means the user just left it on all week, by mistake or to cheat the system!
            Toast.makeText(context, "So.... you spent every minute of your last week on your goal?", Toast.LENGTH_LONG).show();
            //ToDo: Achievement for forgetting to turn off tracking over the course of a week.
        }
        stopNotification(context);
        if (startTime == -1) {
            Log.e("GoalRecyclerAdapter", "onCheckedChanged: ", new IllegalArgumentException("Error in retrieving start time of previous goal."));
        }

    }

    public void setUpThisWeek(GoalViewHolder holder, SharedPreferences sharedPreferences){
        int newWeekNum = AppUtils.getCurrentWeekNum();
        long weekStartTime = AppUtils.getWeekStartMillis(newWeekNum);
        long timeSpentThisWeek = System.currentTimeMillis() - weekStartTime;
        Goal newGoal = mGoals.get(holder.getAdapterPosition());
        newGoal.setCurrentMilli(timeSpentThisWeek);
        newGoal.setWeekNum(newWeekNum);
        GoalSQLHelper sqlHelper = GoalSQLHelper.getInstance(holder.mContainer.getContext());
        sqlHelper.addGoalToWeeklyTable(newGoal);

        sqlHelper.addNewWeekReference(new Week(newWeekNum));

        //FixMe: Might be able to consolidate this code with code in updateSQLReferences
        sqlHelper.updateLifetimeByGoalName(newGoal.getGoalName(),timeSpentThisWeek);
        CurrentWeekGoalManager.getInstance().addCurrentGoal(newGoal);
        AppUtils.addLifeTimeTotalTrackedTime(sharedPreferences, timeSpentThisWeek);
        updateDisplay();
    }

    private void warnUser(final Goal goal, final GoalViewHolder holder) {
        AlertDialog alertDialog = new AlertDialog.Builder(holder.mContainer.getContext()).setMessage("Time spent on this goal for this week will be deleted (Lifetime tracking stats will be unaffected).\nPress \"Okay\" to remove goal for this week.")
                .setTitle("Are you sure?")
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        removeGoal(holder, goal);
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

    private void removeGoal(GoalViewHolder holder, Goal goal) {
        String weekNum = String.valueOf(goal.getWeekNum());
        GoalSQLHelper goalSQLHelper = GoalSQLHelper.getInstance(holder.mContainer.getContext());
        int rowsremoved = goalSQLHelper.removeCurrentGoal(goal.getGoalName(), goal.getSubCategory(), weekNum);
        if (rowsremoved > 1) {
            Log.e("GoalRecyclerAdapter", "Removed: " + rowsremoved, new Exception("Excessive amount of rows deleted"));
        }
        if (CurrentWeekGoalManager.getInstance().removeGoal(goal)) {
            updateDisplayOnRemove(holder.getAdapterPosition());
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
        }
    }

    private void startNotificationService(Context context) {
        Intent intent = new Intent(context, GoalTrackerIntentService.class);
        //ToDO: Consider putting name of goal in the intent instead.
        context.startService(intent);
    }

    private void stopNotification(Context context) {
        NotificationManager noteMan = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        noteMan.cancel(AppConstants.NOTIFICATION_ID);
        Intent intent = new Intent(context, GoalTrackerIntentService.class);
        context.stopService(intent);
    }

    @Override
    public int getItemCount() {
        return mGoals.size();
    }

    @Override
    public void changeConfirmed() {
        Goal changedGoal = mProgressEditorAlertDialogFactory.getCachedGoal();
        long editedTime = mProgressEditorAlertDialogFactory.getEditedTime();
        mProgressEditorAlertDialogFactory.resetCachedData();
        int holderPosition = mSelectedHolder.getAdapterPosition();
        Goal goal = mGoals.get(holderPosition);
        goal.applyChangedValues(changedGoal);
        Context context = mSelectedHolder.mContainer.getContext();
        Toast.makeText(context, "Changes applied", Toast.LENGTH_SHORT).show();
        updateDisplayOnChange(holderPosition);
        updateSQLReferences(context,goal, editedTime, new Week(goal.getWeekNum()));
    }

    public interface OnGoalChangeListener {
        void goalValuesChanged();
    }

    //FixMe: Confusing name. Should probably pass Goal as parameter instead of getting it in the method, but I do need it to get context.
    private void updateAllGoalReferences(GoalViewHolder holder, long timeSpent, int savedWeekNum, SharedPreferences sharedPreferences){
        //Updates selected goal.
        Goal updatedGoal = mGoals.get(holder.getAdapterPosition());
        updatedGoal.addTimeSpent(timeSpent);
        Week week = new Week(AppUtils.getWeekStartMillis(savedWeekNum), AppUtils.getWeekEndMillis(savedWeekNum), savedWeekNum);
        updateSQLReferences(holder.mToggleButton.getContext(),updatedGoal,timeSpent,week);
        updateDisplayOnChange(holder.getAdapterPosition());
        AppUtils.addLifeTimeTotalTrackedTime(sharedPreferences, timeSpent);
    }

    private void updateDisplayOnChange(int adapterPos){
        notifyItemChanged(adapterPos);
        mGoalChangeListener.goalValuesChanged();
    }

    private void updateDisplayOnRemove(int adapterPos){
        notifyItemRemoved(adapterPos);
        mGoalChangeListener.goalValuesChanged();
    }

    private void updateDisplay(){
        notifyDataSetChanged();
        mGoalChangeListener.goalValuesChanged();
    }

    private void updateSQLReferences(Context context, Goal updatedGoal, long timeSpent, Week week){
        GoalSQLHelper sqlHelper = GoalSQLHelper.getInstance(context);
        sqlHelper.updateTimeSpentOnGoal(updatedGoal); //Updates weekly stats
        sqlHelper.updateLifetimeByGoalName(updatedGoal.getGoalName(), timeSpent); //Updates lifetime stats
        sqlHelper.addNewWeekReference(week);
    }
}