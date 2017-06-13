package shuvalov.nikita.clokit.history;


import android.os.Bundle;
import android.support.v4.app.Fragment;
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
    private FrameLayout mTestlayout;

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
        mTestlayout = (FrameLayout) view.findViewById(R.id.test_framelayout);
        PieChartView pieChartView = new PieChartView(getContext(), mWeekGoals);
        mTestlayout.addView(pieChartView);
        return view;
    }

}
