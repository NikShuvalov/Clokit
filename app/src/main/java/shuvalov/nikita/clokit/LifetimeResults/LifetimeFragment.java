package shuvalov.nikita.clokit.LifetimeResults;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import shuvalov.nikita.clokit.GoalSQLHelper;
import shuvalov.nikita.clokit.R;


public class LifetimeFragment extends Fragment {


    public LifetimeFragment() {
    }

    public static LifetimeFragment newInstance() {
        return new LifetimeFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_lifetime, container, false);
        RecyclerView lifetimeRecycler = (RecyclerView) view.findViewById(R.id.lifetime_recycler);

        LifetimeTrackerManager.getInstance().setLifetimeResults(GoalSQLHelper.getInstance(getContext()).getLifetimeResults());

        LifetimeRecyclerAdapter lifetimeRecyclerAdapter = new LifetimeRecyclerAdapter(LifetimeTrackerManager.getInstance().getLifetimeResults());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL, false);

        lifetimeRecycler.setLayoutManager(linearLayoutManager);
        lifetimeRecycler.setAdapter(lifetimeRecyclerAdapter);

        return view;
    }

}
