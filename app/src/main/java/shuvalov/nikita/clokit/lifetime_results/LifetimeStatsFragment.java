package shuvalov.nikita.clokit.lifetime_results;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import shuvalov.nikita.clokit.GoalSQLHelper;
import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.graph_views.LineGraphView;


public class LifetimeStatsFragment extends Fragment {
    private FrameLayout mGraphContainer;
    private String mGoalName;

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
        mGraphContainer = (FrameLayout)view.findViewById(R.id.graph_container);
        mGraphContainer.addView(new LineGraphView(getContext(), GoalSQLHelper.getInstance(container.getContext()).getLifetimeListForGoal(mGoalName)));
        return view;
    }

}
