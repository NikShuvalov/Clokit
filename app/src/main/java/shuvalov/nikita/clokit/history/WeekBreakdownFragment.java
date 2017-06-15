package shuvalov.nikita.clokit.history;


import android.graphics.Paint;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import java.util.ArrayList;

import shuvalov.nikita.clokit.GoalSQLHelper;
import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.graph_views.LineGraphView;
import shuvalov.nikita.clokit.graph_views.PieChartView;
import shuvalov.nikita.clokit.pojos.Goal;

public class WeekBreakdownFragment extends Fragment implements View.OnClickListener {
    private static final String WEEK_NUM = "Week Number";
    public static final String TOTAL_WEEK_BOOL = "Determines whether user selected to include unclocked time";
    private FrameLayout mGraphContainer;
    private RecyclerView mLegendRecycler;
    private PieChartView mPieChartView;
    private WeeklyBreakdownAdapter mWeeklyBreakdownAdapter;

    private ArrayList<Goal> mWeekGoals;

    public WeekBreakdownFragment() {
    }

    public static WeekBreakdownFragment newInstance(int weekNum) {
        WeekBreakdownFragment fragment = new WeekBreakdownFragment();
        Bundle args = new Bundle();
        args.putInt(WEEK_NUM, weekNum);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mWeekGoals = GoalSQLHelper.getInstance(getContext()).getGoalsByWeek(getArguments().getInt(WEEK_NUM));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_week_breakdown, container, false);
        mGraphContainer = (FrameLayout) view.findViewById(R.id.graph_container);
        mPieChartView = new PieChartView(getContext(), mWeekGoals);
        mPieChartView.setOnClickListener(this);
        mGraphContainer.addView(mPieChartView);
        mPieChartView.invalidate();
        mLegendRecycler = (RecyclerView)view.findViewById(R.id.stats_recycler);
        if(savedInstanceState==null) {
            setBreakdownAdapter(setUpRecycler(), mPieChartView,false);
        }else{
            setBreakdownAdapter(setUpRecycler(),mPieChartView,savedInstanceState.getBoolean(TOTAL_WEEK_BOOL, false));
        }
        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(TOTAL_WEEK_BOOL, mWeeklyBreakdownAdapter.isUsingUnclockedTime());
        super.onSaveInstanceState(outState);

    }

    private WeeklyStatsRecyclerAdapter setUpRecycler(){
        ArrayList<Integer> colors = new ArrayList<>();
        for(Paint p : mPieChartView.getColorPaints()){
            colors.add(p.getColor());
        }

        WeeklyStatsRecyclerAdapter weeklyStatsRecyclerAdapter = new WeeklyStatsRecyclerAdapter(mWeekGoals, colors);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mLegendRecycler.setAdapter(weeklyStatsRecyclerAdapter);
        mLegendRecycler.setLayoutManager(linearLayoutManager);
        return weeklyStatsRecyclerAdapter;
    }

    private void setBreakdownAdapter(WeeklyStatsRecyclerAdapter weeklyStatsRecyclerAdapter, PieChartView pieChartView, boolean usingUnclocked){
        mWeeklyBreakdownAdapter = new WeeklyBreakdownAdapter(weeklyStatsRecyclerAdapter, pieChartView, usingUnclocked);
    }

    @Override
    public void onClick(View view) {
        mWeeklyBreakdownAdapter.setUsingUnclockedTime(!mWeeklyBreakdownAdapter.isUsingUnclockedTime());
    }
}
