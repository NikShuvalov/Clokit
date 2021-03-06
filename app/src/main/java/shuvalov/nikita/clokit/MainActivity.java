package shuvalov.nikita.clokit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

import shuvalov.nikita.clokit.goaltracker.CurrentWeekGoalManager;
import shuvalov.nikita.clokit.goaltracker.HomeFragment;
import shuvalov.nikita.clokit.history.HistoryFragment;
import shuvalov.nikita.clokit.history.HistoryRecyclerAdapter;
import shuvalov.nikita.clokit.history.WeekBreakdownFragment;
import shuvalov.nikita.clokit.lifetime_results.LifetimeFragment;
import shuvalov.nikita.clokit.lifetime_results.LifetimeRecyclerAdapter;
import shuvalov.nikita.clokit.lifetime_results.LifetimeStatsFragment;
import shuvalov.nikita.clokit.lifetime_results.LifetimeStatsManager;
import shuvalov.nikita.clokit.pojos.Goal;
import shuvalov.nikita.clokit.pojos.Week;
import shuvalov.nikita.clokit.todo_list.ToDoAsyncTask;
import shuvalov.nikita.clokit.todo_list.ToDoListFragment;
import shuvalov.nikita.clokit.todo_list.ToDoListManager;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener,
        HistoryRecyclerAdapter.WeekSelectedListener,
        LifetimeRecyclerAdapter.GoalSelectedListener,
        ToDoListFragment.OnFragmentInteractionListener{

    DrawerLayout mDrawerLayout;
    NavigationView mNavView;
    Toolbar mToolbar;
    private String mCurrentDisplay;
    private boolean mBackRecentlyPressed;

    public static final String CURRENT_DISPLAY_KEY = "Current Display key";

    //ToDo: Refactor into ENUM
    public static final String HOME_FRAG = "Home fragment";
    public static final String HISTORY_FRAG = "History fragment";
    public static final String LIFETIME_FRAG = "Lifetime fragment";
    public static final String WEEK_BREAKDOWN_FRAG = "Week Breakdown Fragment";
    public static final String LIFETIME_STATS_FRAG = "The fragment that displays the stats of the lifetime goals and such";
    public static final String TODO_LIST_FRAG = "The To-Do List fragment";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mBackRecentlyPressed = false;
        loadData();
        findViews();
        if(savedInstanceState==null){
            startHomeFragment();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        mCurrentDisplay = savedInstanceState.getString(CURRENT_DISPLAY_KEY);
    }

    public void loadData(){
        int weekNum = AppUtils.getCurrentWeekNum();
        CurrentWeekGoalManager.getInstance().setCurrentGoals(GoalSQLHelper.getInstance(this).getGoalsByWeek(weekNum));
    }

    public void findViews(){
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavView = (NavigationView)findViewById(R.id.nav_view);
        mToolbar = (Toolbar)findViewById(R.id.my_toolbar);

        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout, mToolbar, R.string.open_nav,R.string.close_nav);
        mDrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        mNavView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if(mCurrentDisplay.equals(TODO_LIST_FRAG)){
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_todo,menu);
        }else {
            MenuInflater menuInflater = getMenuInflater();
            menuInflater.inflate(R.menu.menu_toolbar, menu);
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_option:
                if(!mCurrentDisplay.equals(HOME_FRAG)){
                    startHomeFragment();
                }else{
                    Toast.makeText(this, "Already in home activity", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.history_option:
                if(!mCurrentDisplay.equals(HISTORY_FRAG)){
                    startHistoryFragment();
                }else{
                    Toast.makeText(this, "Already in history activity", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.lifetime_option:
                if(!mCurrentDisplay.equals(LIFETIME_FRAG)){
                    startLifetimeFragment();
                }else{
                    Toast.makeText(this, "Already in lifetime activity", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.todo_option:
                if(!mCurrentDisplay.equals(TODO_LIST_FRAG)){
                    startToDoFragment();
                }else{
                    Toast.makeText(this, "Already in To-Do list", Toast.LENGTH_SHORT).show();
                }
                break;
        }
        mDrawerLayout.closeDrawers();
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_goal_opt:
                promptUserForGoal(false);
                break;
            case R.id.existing_goal_opt:
                promptUserForGoal(true);
                break;
            case R.id.copy_goals_opt:
                copyLastWeeksGoals();
                break;
            case R.id.remove_empty_goals_opt:
                removeUnusedGoals();
                break;
            case R.id.remove_complete_todo_opt:
                ((ToDoListFragment)getSupportFragmentManager().findFragmentByTag(TODO_LIST_FRAG)).removeCompletedToDos();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void removeUnusedGoals(){
        CurrentWeekGoalManager manager = CurrentWeekGoalManager.getInstance();
        SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.PREFERENCES_NAME, MODE_PRIVATE);
        String activeGoalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);

        String activeSubCat = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, null);
        int activeWeekNum = sharedPreferences.getInt(AppConstants.PREFERENCES_CURRENT_GOAL_WEEK_NUM, -1);
        int removed =0;
        boolean noActive = activeGoalName.equals(AppConstants.PREFERENCES_NO_GOAL);
        ArrayList<Goal> goals = manager.getCurrentGoals();
        for(int i = goals.size(); i-1>=0; i--){
            Goal goal  = goals.get(i-1);
            if(goal.getCurrentMilli()<60000){
                if(!noActive){
                    if(goal.getGoalName().equals(activeGoalName)
                            && (goal.getSubCategory()==null && activeSubCat == null ||(goal.getSubCategory()!=null && goal.getSubCategory().equals(activeSubCat)))
                            && activeWeekNum == goal.getWeekNum()){
                        continue;
                    }
                }
                removed++;
                int r = GoalSQLHelper.getInstance(this).removeCurrentGoal(goal);
                if(r!=1){
                    Log.w("MainActivity", "removed: " + i , new Exception("Unexpected amount of goals removed") );
                }
                manager.removeGoal(goal);
            }
        }
        if(CurrentWeekGoalManager.getInstance().getCurrentGoals().size()==0){
            GoalSQLHelper.getInstance(this).removeWeekReference(AppUtils.getCurrentWeekNum());
        }
        if(removed>0){
            CurrentWeekGoalManager.getInstance().notifyNewData();
        }
        if (mCurrentDisplay.equals(HOME_FRAG)) {
            ((HomeFragment)getSupportFragmentManager().findFragmentByTag(HOME_FRAG)).updateTimeLeftDisplay();
        }
    }

    public void promptUserForGoal(boolean existing) {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this).setPositiveButton("Add Goal",null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
        if(existing){
            final View view = LayoutInflater.from(this).inflate(R.layout.dialog_existinggoal, null);
            RecyclerView optionsRecycler = (RecyclerView)view.findViewById(R.id.existing_goal_recycler);
            optionsRecycler.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));

            final ExistingGoalsRecyclerAdapter goalsAdapter = new ExistingGoalsRecyclerAdapter(GoalSQLHelper.getInstance(view.getContext()).getLifetimeResults());
            optionsRecycler.setAdapter(goalsAdapter);

            AlertDialog alertDialog = builder.setTitle("Goal Selection").setView(view).create();

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialogInterface) {
                    Button button = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);

                    final EditText hourEntry = (EditText) ((AlertDialog) dialogInterface).findViewById(R.id.hour_entry);
                    final EditText minuteEntry = (EditText)((AlertDialog) dialogInterface).findViewById(R.id.minute_entry);
                    final EditText subCatEntry = (EditText)((AlertDialog)dialogInterface).findViewById(R.id.sub_cat_entry);
                    button.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            {

                                //ToDo: Consolidate this and other alertDialog
                                String goalName;
                                Goal selectedGoal = goalsAdapter.getSelectedGoal();
                                if(selectedGoal == null){
                                    Toast.makeText(MainActivity.this, "No Goal Selected", Toast.LENGTH_SHORT).show();
                                }else{
                                    boolean validTimeEntry = false;
                                    goalName = selectedGoal.getGoalName();
                                    goalsAdapter.resetSelection();
                                    String hoursString = hourEntry.getText().toString();
                                    String minutesString  = minuteEntry.getText().toString();
                                    long minutes = 0;
                                    long hours = 0;
                                    if(hoursString.equals("") && minutesString.equals("")){
                                        hourEntry.setError("A goal time amount is required");
                                        minuteEntry.setError("A goal time amount is required");
                                        goalsAdapter.resetSelection(); //Needed to reset selection, otherwise two can be selected
                                    } else if (hoursString.length() > 3 || minutesString.length() > 5){
                                        if( hoursString.length() > 3) {
                                            hourEntry.setError("Hour Input can't be longer than 3 digits");
                                        }
                                        if(minutesString.length() > 5){
                                            minuteEntry.setError("Minute Input can't be longer than 5 digits");
                                        }
                                    }else if(hoursString.equals("") || minutesString.equals("")){
                                        if(minutesString.equals("")){
                                            hours = Integer.valueOf(hoursString);
                                        }else{
                                            minutes = Integer.valueOf(minutesString);
                                        }
                                        validTimeEntry = true;
                                    } else{
                                        hours = Integer.valueOf(hoursString);
                                        minutes = Integer.valueOf(minutesString);
                                        validTimeEntry = true;
                                    }
                                    if(validTimeEntry) {
                                        long totalMillis = (minutes * 1000 * 60) + (hours * 1000 * 3600);
                                        if (totalMillis == 0) {
                                            hourEntry.setError("A goal time amount is required");
                                            minuteEntry.setError("A goal time amount is required");
                                            goalsAdapter.resetSelection();
                                        } else if (totalMillis > 604800000) {
                                            hourEntry.setError("Goal Time can't be greater than length of week");
                                            minuteEntry.setError("Goal Time can't be greater than length of week");
                                            goalsAdapter.resetSelection();
                                        } else {
                                            String subCategory = subCatEntry.getText().toString();
                                            Goal goal = new Goal(goalName.trim(), 0, totalMillis, AppUtils.getCurrentWeekNum(), subCategory.trim());
                                            if (CurrentWeekGoalManager.getInstance().addCurrentGoal(goal)) {
                                                GoalSQLHelper.getInstance(MainActivity.this).addGoalToWeeklyTable(goal);
                                                CurrentWeekGoalManager.getInstance().notifyNewData();
                                                if (mCurrentDisplay.equals(HOME_FRAG)) {
                                                    ((HomeFragment) getSupportFragmentManager().findFragmentByTag(HOME_FRAG)).updateTimeLeftDisplay();
                                                }
                                                dialogInterface.dismiss();
                                            } else {
                                                goalsAdapter.resetSelection();
                                                Toast.makeText(MainActivity.this, "This goal is already set for this week", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });

                }
            });
            alertDialog.show();
        }else{
            final View view = LayoutInflater.from(this).inflate(R.layout.dialog_newgoal, null);
            AlertDialog alertDialog = builder.setTitle("Goal Creation").setView(view).create();

            alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
                @Override
                public void onShow(final DialogInterface dialogInterface) {
                    Button button = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);

                    final EditText nameEntry = (EditText) ((AlertDialog) dialogInterface).findViewById(R.id.goal_name_entry);
                    final EditText hourEntry = (EditText) ((AlertDialog) dialogInterface).findViewById(R.id.hour_entry);
                    final EditText minuteEntry = (EditText)((AlertDialog) dialogInterface).findViewById(R.id.minute_entry);
                    final EditText subCatEntry = (EditText)((AlertDialog)dialogInterface).findViewById(R.id.sub_cat_entry);
                    button.setOnClickListener(new View.OnClickListener() {

                        @Override
                        public void onClick(View view) {
                            {
                                //ToDo: Consolidate this and other alertDialog
                                String goalName = nameEntry.getText().toString();
                                String subCat = subCatEntry.getText().toString();
                                if(goalName.trim().equals("")){
                                    nameEntry.setError("A goal name is required");
                                }else{
                                    boolean validTimeEntry = false;
                                    String hoursString = hourEntry.getText().toString();
                                    String minutesString  = minuteEntry.getText().toString();
                                    long minutes = 0;
                                    long hours = 0;
                                    if(hoursString.equals("") && minutesString.equals("")){
                                        hourEntry.setError("A goal time amount is required");
                                        minuteEntry.setError("A goal time amount is required");
                                    } else if (hoursString.length() > 3 || minutesString.length() > 5){
                                        if( hoursString.length() > 3) {
                                            hourEntry.setError("Hour Input can't be longer than 3 digits");
                                        }
                                        if(minutesString.length() > 5){
                                            minuteEntry.setError("Minute Input can't be longer than 5 digits");
                                        }
                                    }else if(hoursString.equals("") || minutesString.equals("")){
                                        if(minutesString.equals("")){
                                            hours = Integer.valueOf(hoursString);
                                        }else{
                                            minutes = Integer.valueOf(minutesString);
                                        }
                                        validTimeEntry = true;
                                    }
                                    else{
                                        hours = Integer.valueOf(hoursString);
                                        minutes = Integer.valueOf(minutesString);
                                        validTimeEntry = true;
                                    }

                                    if(validTimeEntry) {
                                        long totalMillis = (minutes * 1000 * 60) + (hours * 1000 * 3600);
                                        if (totalMillis == 0) {
                                            Log.d("MainAcivity", "onClick: " + totalMillis);
                                            hourEntry.setError("A goal time amount is required");
                                            minuteEntry.setError("A goal time amount is required");
                                        } else if (totalMillis > (604800 * 1000)) {
                                            hourEntry.setError("Total goal time can't be greater than week length");
                                            minuteEntry.setError("Total goal time can't be greater than week length");
                                        } else {
                                            Goal goal = new Goal(goalName.trim(), 0, totalMillis, AppUtils.getCurrentWeekNum(), subCat.trim());
                                            if (CurrentWeekGoalManager.getInstance().addCurrentGoal(goal)) {
                                                GoalSQLHelper.getInstance(MainActivity.this).addGoalToWeeklyTable(goal);
                                                if (mCurrentDisplay.equals(HOME_FRAG)) {
                                                    ((HomeFragment) getSupportFragmentManager().findFragmentByTag(HOME_FRAG)).updateTimeLeftDisplay();
                                                }
                                                dialogInterface.dismiss();
                                            } else {
                                                Toast.makeText(MainActivity.this, "This goal is already set for this week", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    });
                }
            });
            alertDialog.show();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(CURRENT_DISPLAY_KEY, mCurrentDisplay);
    }

    @Override
    public void onBackPressed() {
        if(mCurrentDisplay.equals(HOME_FRAG)){
            if(mBackRecentlyPressed){
                mBackRecentlyPressed=false;
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_HOME);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }else{
                mBackRecentlyPressed = true;
                Toast.makeText(this, "Press back again to close", Toast.LENGTH_SHORT).show();
            }
        }else if(mCurrentDisplay.equals(WEEK_BREAKDOWN_FRAG)){
            startHistoryFragment();
        }else if (mCurrentDisplay.equals(LIFETIME_STATS_FRAG)){
            startLifetimeFragment();
        }
        else{
            startHomeFragment();
        }
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        menu.setGroupVisible(0, mCurrentDisplay.equals(HOME_FRAG) || mCurrentDisplay.equals(TODO_LIST_FRAG));
        return super.onPrepareOptionsMenu(menu);
    }

    private void startHistoryFragment(){
        mBackRecentlyPressed = false;
        mCurrentDisplay = HISTORY_FRAG;
        invalidateOptionsMenu();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HistoryFragment.newInstance(), HISTORY_FRAG).commit();
    }

    private void startHomeFragment(){
        mBackRecentlyPressed = false;
        mCurrentDisplay = HOME_FRAG;
        invalidateOptionsMenu();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, HomeFragment.newInstance(),HOME_FRAG).commit();
    }

    private void startWeekBreakdownFragment(int weekNum){
        mBackRecentlyPressed = false;
        mCurrentDisplay = WEEK_BREAKDOWN_FRAG;
        invalidateOptionsMenu();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, WeekBreakdownFragment.newInstance(weekNum), WEEK_BREAKDOWN_FRAG).addToBackStack(String.valueOf(weekNum)).commit();
    }

    private void startLifetimeStatsFragment(String goalName){
        mBackRecentlyPressed = false;
        mCurrentDisplay = LIFETIME_STATS_FRAG;
        invalidateOptionsMenu();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, LifetimeStatsFragment.newInstance(goalName),LIFETIME_STATS_FRAG).addToBackStack(goalName).commit();
    }

    private void startLifetimeFragment(){
        mBackRecentlyPressed = false;
        mCurrentDisplay = LIFETIME_FRAG;
        invalidateOptionsMenu();
        LifetimeStatsManager.getInstance().setSelectedOption(0);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, LifetimeFragment.newInstance(), LIFETIME_FRAG).commit();
    }

    private void startToDoFragment(){
        ToDoListManager.getInstance().setToDoItems(GoalSQLHelper.getInstance(this).getToDoList());
        mCurrentDisplay = TODO_LIST_FRAG;
        mBackRecentlyPressed = false;
        invalidateOptionsMenu();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, ToDoListFragment.newInstance(), TODO_LIST_FRAG).commit();
    }

    /**
     * Copies the last active week's goals, clears the progress, adjusts the weeknumber and places it back in the database as new tasks.
     * This keeps the total goalTime
     */
    public void copyLastWeeksGoals(){
        GoalSQLHelper sqlHelper = GoalSQLHelper.getInstance(this);
        String lastActiveWeek = sqlHelper.getLastActiveWeekNum();
        if(lastActiveWeek!=null){ //If a week is found, adjust values and put in as current week.
            int previousWeek = Integer.valueOf(lastActiveWeek);
            ArrayList<Goal> previousGoals = sqlHelper.getGoalsByWeek(previousWeek);
            for(Goal goal: previousGoals){
                goal.setCurrentMilli(0);
                goal.setWeekNum(AppUtils.getCurrentWeekNum());
                CurrentWeekGoalManager.getInstance().addCurrentGoal(goal);
            }
            sqlHelper.addGoalsToCurrentWeek(previousGoals);
            if (mCurrentDisplay.equals(HOME_FRAG)) {
                ((HomeFragment)getSupportFragmentManager().findFragmentByTag(HOME_FRAG)).updateTimeLeftDisplay();
            }
        }else{
            String nothingFound = "Couldn't find previous active week";
            Toast.makeText(this, nothingFound, Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWeekSelected(Week week) {
        startWeekBreakdownFragment(week.getWeekNum());
    }

    @Override
    public void onGoalSelected(String goalName) {
        startLifetimeStatsFragment(goalName);
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    public void onFragmentInteraction(Uri uri) {
    }
}