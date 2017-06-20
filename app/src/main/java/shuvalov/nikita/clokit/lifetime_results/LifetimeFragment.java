package shuvalov.nikita.clokit.lifetime_results;


import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import shuvalov.nikita.clokit.AppConstants;
import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.GoalSQLHelper;
import shuvalov.nikita.clokit.MainActivity;
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

        LifetimeTrackerManager lifetrackerManager = LifetimeTrackerManager.getInstance();
        lifetrackerManager.setLifetimeResults(GoalSQLHelper.getInstance(getContext()).getLifetimeResults());
        lifetrackerManager.sortByTimeAllocated();


        final LifetimeRecyclerAdapter lifetimeRecyclerAdapter = new LifetimeRecyclerAdapter(LifetimeTrackerManager.getInstance().getLifetimeResults(), (MainActivity)getActivity());

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getContext(), 2);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                switch(lifetimeRecyclerAdapter.getItemViewType(position)){
                    case LifetimeRecyclerAdapter.HEADER:
                        return 2;
                    default:
                        return 1;
                }
            }
        });

        lifetimeRecycler.setLayoutManager(gridLayoutManager);
        lifetimeRecycler.setAdapter(lifetimeRecyclerAdapter);

        long trackedTime = getContext().getSharedPreferences(AppConstants.PREFERENCES_NAME, Context.MODE_PRIVATE).getLong(AppConstants.PREFERENCES_TOTAL_TRACKED_TIME,0);
        String summaryText = "Total tracked time:\n" + AppUtils.getLifetimeSummaryText(trackedTime);
        ((TextView)view.findViewById(R.id.summary_text)).setText(summaryText);
        return view;
    }

}
