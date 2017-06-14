package shuvalov.nikita.clokit.history;


import android.content.Context;
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
import shuvalov.nikita.clokit.pojos.Goal;

public class WeekBreakdownFragment extends Fragment {
    private static final String WEEK_NUM = "Week Number";
    private FrameLayout mGraphContainer;
    private RecyclerView mLegendRecycler;
    private PieChartView mPieChartView;

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
        mGraphContainer.addView(mPieChartView);
        mLegendRecycler = (RecyclerView)view.findViewById(R.id.stats_recycler);
        setUpRecycler();
        return view;
    }

    private void setUpRecycler(){
        ArrayList<Integer> colors = new ArrayList<>();
        for(Paint p : mPieChartView.getColorPaints()){
            colors.add(p.getColor());
        }

        WeeklyStatsRecyclerAdapter weeklyStatsRecyclerAdapter = new WeeklyStatsRecyclerAdapter(mWeekGoals, colors);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        mLegendRecycler.setAdapter(weeklyStatsRecyclerAdapter);
        mLegendRecycler.setLayoutManager(linearLayoutManager);
    }
}
