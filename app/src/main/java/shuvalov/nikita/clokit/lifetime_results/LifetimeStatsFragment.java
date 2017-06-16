package shuvalov.nikita.clokit.lifetime_results;


import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import shuvalov.nikita.clokit.GoalSQLHelper;
import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.graph_views.LineGraphView;
import shuvalov.nikita.clokit.pojos.Goal;


public class LifetimeStatsFragment extends Fragment implements View.OnClickListener{
    private String mGoalName;
    private ViewPager mViewPager;
    private ImageView mChartButton, mStatsButton;
    private RelativeLayout mChartBg, mStatsBg;
    private LifetimeStatsPagerAdapter mLifetimeStatsPagerAdapter;

    public static final String GOAL_NAME = "Goal name";

    public LifetimeStatsFragment() {
        // Required empty public constructor
    }

    public static LifetimeStatsFragment newInstance(String goalName) {
        LifetimeStatsFragment fragment = new LifetimeStatsFragment();
        Bundle bundle = new Bundle();
        bundle.putString(GOAL_NAME, goalName);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null){
            mGoalName = getArguments().getString(GOAL_NAME);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_lifetime_stats, container, false);

        ArrayList<Goal> goals = GoalSQLHelper.getInstance(container.getContext()).getLifetimeListForGoal(mGoalName);
        LifetimeStatsManager lifetimeStatsManager = LifetimeStatsManager.getInstance();
        lifetimeStatsManager.setLifetimeGoalList(goals);
        mLifetimeStatsPagerAdapter = new LifetimeStatsPagerAdapter(getChildFragmentManager());

        mViewPager = (ViewPager) view.findViewById(R.id.view_pager);
        mViewPager.setCurrentItem(lifetimeStatsManager.getSelectedOption());

        mChartButton = (ImageView) view.findViewById(R.id.chart_option);
        mStatsButton = (ImageView) view.findViewById(R.id.stats_option);
        mChartBg = (RelativeLayout)view.findViewById(R.id.chart_bg);
        mStatsBg = (RelativeLayout)view.findViewById(R.id.stats_bg);

        changeButtonColors();

        mChartButton.setOnClickListener(this);
        mStatsButton.setOnClickListener(this);

        mViewPager.setAdapter(mLifetimeStatsPagerAdapter);


        return view;
    }

    private void changeButtonColors(){
        if(LifetimeStatsManager.getInstance().getSelectedOption() == 0){
            mChartButton.setImageResource(R.drawable.ic_show_chart_selected);
            mStatsButton.setImageResource(R.drawable.ic_list);
            mChartBg.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mStatsBg.setBackgroundColor(Color.WHITE);
        }else{
            mChartButton.setImageResource(R.drawable.ic_show_chart);
            mStatsButton.setImageResource(R.drawable.ic_list_selected);
            mStatsBg.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
            mChartBg.setBackgroundColor(Color.WHITE);
        }
    }



    @Override
    public void onClick(View view) {
        switch(view.getId()){
            case R.id.chart_option:
                LifetimeStatsManager.getInstance().setSelectedOption(0);
                mViewPager.setCurrentItem(0);
                changeButtonColors();
                break;
            case R.id.stats_option:
                LifetimeStatsManager.getInstance().setSelectedOption(1);
                mViewPager.setCurrentItem(1);
                changeButtonColors();
                break;
        }
    }
}
