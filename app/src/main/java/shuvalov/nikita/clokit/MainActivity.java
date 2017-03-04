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
                final View view = LayoutInflater.from(this).inflate(R.layout.dialog_newgoal, null);
                new AlertDialog.Builder(this).setTitle("Goal Creation").setView(view)
                        .setPositiveButton("Add Goal", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        EditText nameEntry = (EditText) view.findViewById(R.id.goal_name_entry);
                        EditText hourEntry = (EditText) view.findViewById(R.id.hour_entry);
                        EditText minuteEntry = (EditText)view.findViewById(R.id.minute_entry);

                        String goalName = nameEntry.getText().toString();
                        if(goalName.isEmpty()){
                            nameEntry.setError("A goal name is required");
                        }else{
                            int hours = Integer.valueOf(nameEntry.getText().toString());
                            int minutes = Integer.valueOf(nameEntry.getText().toString());
                            long totalMillis = (minutes*1000*60)+(hours*1000*3600);
                            if(totalMillis==0){
                                hourEntry.setError("A goal time amount is required");
                                minuteEntry.setError("A goal time amount is required");
                            }else{
                                Goal goal = new Goal(goalName, 0, totalMillis, AppUtils.getCurrentWeekNum());

                            }

                        }
                    }
                }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                });
                break;
            case R.id.settings_opt:
                //ToDo: Take to settings page.
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
