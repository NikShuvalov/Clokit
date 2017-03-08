package shuvalov.nikita.clokit.goaltracker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.pojos.Goal;


public class HomeFragment extends Fragment {
    private RecyclerView mGoalRecycler;
    private GoalRecyclerAdapter mAdapter;
    private TextView mWeekText, mUnfinishedText;


    public HomeFragment() {
        // Required empty public constructor
    }

    public static HomeFragment newInstance() {
        HomeFragment fragment = new HomeFragment();

        return fragment;
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

        mAdapter = new GoalRecyclerAdapter(CurrentWeekGoalManager.getInstance().getCurrentGoals());
        LinearLayoutManager goalLayoutManager = new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL,false);

        mGoalRecycler.setAdapter(mAdapter);
        mGoalRecycler.setLayoutManager(goalLayoutManager);

        mWeekText = (TextView)view.findViewById(R.id.week_time_text);
        mUnfinishedText  = (TextView)view.findViewById(R.id.unfinished_work_text);

        String weekString = "Time until week reset:\n" + AppUtils.getDisplayForTimeLeft();
        mWeekText.setText(weekString);

        long timeLeft = 0;
        for(Goal goal:CurrentWeekGoalManager.getInstance().getCurrentGoals()){
            timeLeft+=goal.getTimeLeft();
        }
        String unfinishedString = "Unfinished work remaining:\n" + AppUtils.getHoursAndMinutes(timeLeft);
        mUnfinishedText.setText(unfinishedString);

        return view;
    }
}
