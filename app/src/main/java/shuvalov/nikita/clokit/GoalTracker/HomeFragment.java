package shuvalov.nikita.clokit.goaltracker;


import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.preference.Preference;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import shuvalov.nikita.clokit.AppConstants;
import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.GoalSQLHelper;
import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.pojos.Goal;
import shuvalov.nikita.clokit.pojos.Week;


public class HomeFragment extends Fragment implements GoalRecyclerAdapter.OnGoalChangeListener{
    private RecyclerView mGoalRecycler;
    private GoalRecyclerAdapter mAdapter;
    private TextView mWeekText, mUnfinishedText;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        return new HomeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        mGoalRecycler = (RecyclerView)view.findViewById(R.id.goal_recycler);

        CurrentWeekGoalManager currentWeekGoalManager = CurrentWeekGoalManager.getInstance();
        mAdapter = new GoalRecyclerAdapter(currentWeekGoalManager.getCurrentGoals(), this);
        RecyclerView.LayoutManager goalLayoutManager;
        currentWeekGoalManager.setGoalRecyclerAdapter(mAdapter);

        int orientation = getResources().getConfiguration().orientation;
        if(orientation == Configuration.ORIENTATION_PORTRAIT) {
            goalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        }else{
            goalLayoutManager = new GridLayoutManager(getContext(),2);
        }

        mGoalRecycler.setAdapter(mAdapter);
        mGoalRecycler.setLayoutManager(goalLayoutManager);

        mWeekText = (TextView)view.findViewById(R.id.week_time_text);
        mUnfinishedText  = (TextView)view.findViewById(R.id.unfinished_work_text);

        updateTimeLeftDisplay();
        return view;
    }

    public void updateTimeLeftDisplay(){
        String weekString = "Time until week reset:\n" + AppUtils.getDisplayForTimeLeft();
        mWeekText.setText(weekString);

        long timeLeft = 0;
        for(Goal goal:CurrentWeekGoalManager.getInstance().getCurrentGoals()){
            timeLeft+=goal.getTimeLeft();
        }
        String unfinishedString = "Unfinished work remaining:\n" + AppUtils.getHoursAndMinutes(timeLeft);
        mUnfinishedText.setText(unfinishedString);
    }

    @Override
    public void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = getContext().getSharedPreferences(AppConstants.PREFERENCES_NAME, Context.MODE_PRIVATE);
        int activeGoalWeekNum = sharedPreferences.getInt(AppConstants.PREFERENCES_CURRENT_GOAL_WEEK_NUM, -1);
        String activeGoalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);

        if(!activeGoalName.equals(AppConstants.PREFERENCES_NO_GOAL) && AppUtils.getCurrentWeekNum() != activeGoalWeekNum){//If the current week isn't default, and isn't the same as the week that's saved.

            String lastWeekGoalSubCat = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, null);
            long startTime = sharedPreferences.getLong(AppConstants.PREFERENCES_START_TIME, 0);
            Goal lastWeekGoal = GoalSQLHelper.getInstance(getContext()).getSpecificCurrentGoal(activeGoalName,lastWeekGoalSubCat, String.valueOf(activeGoalWeekNum));
            long weekEndTime = AppUtils.getWeekEndMillis(activeGoalWeekNum);
            long timeSpentLastWeek = weekEndTime-startTime;

            lastWeekGoal.addTimeSpent(timeSpentLastWeek);
            Week lastWeek = new Week(AppUtils.getWeekStartMillis(activeGoalWeekNum), AppUtils.getWeekEndMillis(activeGoalWeekNum),activeGoalWeekNum);
            updateAllGoalReferences(lastWeekGoal, lastWeek, timeSpentLastWeek, sharedPreferences);
            AppUtils.addLifeTimeTotalTrackedTime(sharedPreferences,timeSpentLastWeek);

            lastWeekGoal.setWeekNum(AppUtils.getCurrentWeekNum());
            lastWeekGoal.setCurrentMilli(0);
            setupGoalForThisWeek(lastWeekGoal, sharedPreferences);
        }
        updateTimeLeftDisplay();
    }

    private void setupGoalForThisWeek(Goal lastWeekGoal, SharedPreferences sharedPreferences){
        GoalSQLHelper sqlhelper = GoalSQLHelper.getInstance(getContext());
        sqlhelper.addGoalToWeeklyTable(lastWeekGoal);
        int thisWeekNum = AppUtils.getCurrentWeekNum();
        Week thisWeek = new Week(AppUtils.getWeekStartMillis(thisWeekNum), AppUtils.getWeekEndMillis(thisWeekNum), thisWeekNum);
        sqlhelper.addNewWeekReference(thisWeek);
        CurrentWeekGoalManager.getInstance().addCurrentGoal(lastWeekGoal);
        mAdapter.notifyDataSetChanged();

        sharedPreferences.edit().putString(AppConstants.PREFERENCES_CURRENT_GOAL, lastWeekGoal.getGoalName()).apply(); //Save name of current goal.
        sharedPreferences.edit().putLong(AppConstants.PREFERENCES_START_TIME, AppUtils.getWeekStartMillis(thisWeekNum)).apply(); //Save the current time of the selected goal.
        sharedPreferences.edit().putInt(AppConstants.PREFERENCES_CURRENT_GOAL_WEEK_NUM, thisWeekNum).apply(); //Adds the weeknum of when the goal was started
        sharedPreferences.edit().putString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, lastWeekGoal.getSubCategory()).apply(); //Saves the subcategory of current goal.
    }

    public void updateAllGoalReferences(Goal goal, Week week, long timeSpent, SharedPreferences sharedPreferences){
        GoalSQLHelper sqlHelper = GoalSQLHelper.getInstance(getContext());
        sqlHelper.updateTimeSpentOnGoal(goal); //Updates weekly stats
        sqlHelper.updateLifetimeByGoalName(goal.getGoalName(), timeSpent); //Updates lifetime stats
        sqlHelper.addNewWeekReference(week);//Adds this week as an active week for reference in history view, the method ignores duplicate entries.

        //Update the visuals
        updateTimeLeftDisplay();

        //Update the total tracked time in preferences.
        AppUtils.addLifeTimeTotalTrackedTime(sharedPreferences, timeSpent);
    }

    @Override
    public void goalValuesChanged() {
        updateTimeLeftDisplay();
    }
}
