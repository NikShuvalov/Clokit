package shuvalov.nikita.clokit.lifetime_results;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.Collections;

import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.pojos.Goal;


public class LifetimeStatsSubcatFragment extends Fragment {
    private RecyclerView mRecyclerView;

    public LifetimeStatsSubcatFragment() {
    }

    public static LifetimeStatsSubcatFragment newInstance() {
        return new LifetimeStatsSubcatFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lifetime_stats_subcat, container, false);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.subcat_recycler);
        ArrayList<Goal> goalList = LifetimeStatsManager.getInstance().getLifetimeGoalList();
        Collections.sort(goalList,new TimeAllocatedComparator());
        setUpRecycler(goalList);
        return view;
    }

    private void setUpRecycler(ArrayList< Goal > goals){
        LifeTimeBreakdownRecyclerAdapter lifeTimeBreakdownRecyclerAdapter = new LifeTimeBreakdownRecyclerAdapter(goals);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);

        mRecyclerView.setAdapter(lifeTimeBreakdownRecyclerAdapter);
        mRecyclerView.setLayoutManager(linearLayoutManager);
    }

}
