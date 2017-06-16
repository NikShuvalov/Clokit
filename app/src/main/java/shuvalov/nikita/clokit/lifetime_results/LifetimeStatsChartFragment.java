package shuvalov.nikita.clokit.lifetime_results;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import shuvalov.nikita.clokit.GoalSQLHelper;
import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.graph_views.LineGraphView;
import shuvalov.nikita.clokit.graph_views.PieChartView;


public class LifetimeStatsChartFragment extends Fragment {

    public LifetimeStatsChartFragment() {
    }

    public static LifetimeStatsChartFragment newInstance() {
        return new LifetimeStatsChartFragment();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =inflater.inflate(R.layout.fragment_lifetime_stats_chart, container, false);
        FrameLayout graphContainer = (FrameLayout) view.findViewById(R.id.graph_container);
        LineGraphView lineGraphView= new LineGraphView(getContext(), GoalSQLHelper.getInstance(getContext()).getLifetimeListForGoal(LifetimeStatsManager.getInstance().getGoalName()));
        graphContainer.addView(lineGraphView);
        return view;
    }

}
