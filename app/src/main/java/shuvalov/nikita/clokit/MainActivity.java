package shuvalov.nikita.clokit;

import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener{
    private DrawerLayout mDrawerLayout;
    private NavigationView mNavView;
    private RecyclerView mGoalView;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();

    }

    public void findViews(){
        mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mNavView = (NavigationView)findViewById(R.id.nav_view);
        mGoalView = (RecyclerView)findViewById(R.id.goal_recycler);
        mToolbar = (Toolbar)findViewById(R.id.my_toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this,mDrawerLayout,R.string.open_nav,R.string.close_nav);
        mDrawerLayout.setDrawerListener(toggle);
        toggle.syncState();

        setSupportActionBar(mToolbar);

        mNavView.setNavigationItemSelectedListener(this);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.home_option:
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
}
