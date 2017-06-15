package shuvalov.nikita.clokit.history;

import shuvalov.nikita.clokit.graph_views.PieChartView;

/**
 * Created by NikitaShuvalov on 6/15/17.
 */

public class WeeklyBreakdownAdapter {
    private WeeklyStatsRecyclerAdapter mWeeklyStatsRecyclerAdapter;
    private PieChartView mPieChartView;
    private boolean mUsingUnclockedTime;

    public WeeklyBreakdownAdapter(WeeklyStatsRecyclerAdapter weeklyStatsRecyclerAdapter, PieChartView pieChartView) {
        this(weeklyStatsRecyclerAdapter, pieChartView, false);
    }

    public WeeklyBreakdownAdapter(WeeklyStatsRecyclerAdapter weeklyStatsRecyclerAdapter, PieChartView pieChartView, boolean usingUnclockedTime) {
        mWeeklyStatsRecyclerAdapter = weeklyStatsRecyclerAdapter;
        mPieChartView = pieChartView;
        mUsingUnclockedTime = usingUnclockedTime;
        mWeeklyStatsRecyclerAdapter.addUnclockedTime(mPieChartView.getUnclockedTime());
        syncValues();
    }

    public boolean isUsingUnclockedTime() {
        return mUsingUnclockedTime;
    }

    public void setUsingUnclockedTime(boolean usingUnclockedTime) {
        if(mUsingUnclockedTime != usingUnclockedTime) {
            mUsingUnclockedTime = usingUnclockedTime;
            syncValues();
        }
    }

    public void syncValues(){
        mPieChartView.setUseUnclockedTime(mUsingUnclockedTime);
        mWeeklyStatsRecyclerAdapter.setUsingUnclocked(mUsingUnclockedTime);
    }

}
