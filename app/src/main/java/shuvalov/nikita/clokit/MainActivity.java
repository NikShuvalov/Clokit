package shuvalov.nikita.clokit;

import android.content.DialogInterface;
import android.content.SharedPreferences;
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
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;

import shuvalov.nikita.clokit.POJOs.Goal;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;
    private RecyclerView mGoalView;
    private Toolbar mToolbar;
    private GoalRecyclerAdapter mGoalAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        loadData();
        findViews();
    }


    public void loadData(){
        int weekNum = AppUtils.getCurrentWeekNum();
        CurrentWeekGoalManager.getInstance().setCurrentGoals(GoalSQLHelper.getInstance(this).getCurrentGoals(weekNum));
    }

    public void findViews(){
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavView = (NavigationView)findViewById(R.id.nav_view);
        mGoalView = (RecyclerView)findViewById(R.id.goal_recycler);
        mToolbar = (Toolbar)findViewById(R.id.my_toolbar);


        mGoalAdapter = new GoalRecyclerAdapter(CurrentWeekGoalManager.getInstance().getCurrentGoals());
        LinearLayoutManager goalLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL,false);

        mGoalView.setAdapter(mGoalAdapter);
        mGoalView.setLayoutManager(goalLayoutManager);

        setSupportActionBar(mToolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout, mToolbar, R.string.open_nav,R.string.close_nav);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();


        mNavView.setNavigationItemSelectedListener(this);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu_toolbar,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_option:
                Toast.makeText(this, "Not Yet implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.history_option:
                Toast.makeText(this, "Not yet Implemented", Toast.LENGTH_SHORT).show();
                break;
            case R.id.achievements_option:
                Toast.makeText(this, "Not Yet Implemented", Toast.LENGTH_SHORT).show();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch(item.getItemId()){
            case R.id.new_goal_opt:
                promptUserForGoal();
                break;
            case R.id.settings_opt:
                //ToDo: Take to settings page.
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void promptUserForGoal(){
        final View view = LayoutInflater.from(this).inflate(R.layout.dialog_newgoal, null);
        final AlertDialog alertDialog = new AlertDialog.Builder(this).setTitle("Goal Creation").setView(view)
                .setPositiveButton("Add Goal",null)
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        }).create();

        alertDialog.setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(final DialogInterface dialogInterface) {
                Button button = ((AlertDialog) dialogInterface).getButton(DialogInterface.BUTTON_POSITIVE);

                final EditText nameEntry = (EditText) ((AlertDialog) dialogInterface).findViewById(R.id.goal_name_entry);
                final EditText hourEntry = (EditText) ((AlertDialog) dialogInterface).findViewById(R.id.hour_entry);
                final EditText minuteEntry = (EditText)((AlertDialog) dialogInterface).findViewById(R.id.minute_entry);
                button.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View view) {
                        {
                            String goalName = nameEntry.getText().toString();
                            if(goalName.trim().equals("")){
                                nameEntry.setError("A goal name is required");
                            }else{
                                String hoursString = hourEntry.getText().toString();
                                String minutesString  = minuteEntry.getText().toString();
                                int minutes = 0;
                                int hours = 0;
                                if(hoursString.equals("") && minutesString.equals("")){
                                    hourEntry.setError("A goal time amount is required");
                                    minuteEntry.setError("A goal time amount is required");
                                }else if(hoursString.equals("") || minutesString.equals("")){
                                    if(minutesString.equals("")){
                                        hours = Integer.valueOf(hoursString);
                                    }else{
                                        minutes = Integer.valueOf(minutesString);
                                    }
                                } else{
                                    hours = Integer.valueOf(hoursString);
                                    minutes = Integer.valueOf(minutesString);
                                }
                                long totalMillis = (minutes*1000*60)+(hours*1000*3600);
                                if(totalMillis==0){
                                    hourEntry.setError("A goal time amount is required");
                                    minuteEntry.setError("A goal time amount is required");
                                }else{
                                    Goal goal = new Goal(goalName.trim(), 0, totalMillis, AppUtils.getCurrentWeekNum());
                                    if(CurrentWeekGoalManager.getInstance().addCurrentGoal(goal)){
                                        GoalSQLHelper.getInstance(MainActivity.this).addGoalToCurrentWeek(goal);
                                    dialogInterface.dismiss();
                                    }else{
                                        Toast.makeText(MainActivity.this, "This goal is already set for this week", Toast.LENGTH_SHORT).show();
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
