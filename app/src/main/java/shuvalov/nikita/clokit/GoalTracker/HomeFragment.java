package shuvalov.nikita.clokit.GoalTracker;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import shuvalov.nikita.clokit.R;


public class HomeFragment extends Fragment {
    private RecyclerView mGoalRecycler;
    private GoalRecyclerAdapter mAdapter;


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

        return view;
    }

}
