package shuvalov.nikita.clokit.history;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import shuvalov.nikita.clokit.GoalSQLHelper;
import shuvalov.nikita.clokit.MainActivity;
import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.pojos.Week;

public class HistoryFragment extends Fragment {

    public HistoryFragment() {
        // Required empty public constructor
    }

    public static HistoryFragment newInstance() {
        return new HistoryFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history, container, false);


        RecyclerView historyRecycler = (RecyclerView)view.findViewById(R.id.history_recycler);
        ArrayList<Week> activeWeeks = GoalSQLHelper.getInstance(getContext()).getActiveWeeks();
        HistoryRecyclerAdapter historyAdapter = new HistoryRecyclerAdapter(activeWeeks, (MainActivity)getActivity());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,true);

        historyRecycler.setAdapter(historyAdapter);
        historyRecycler.setLayoutManager(linearLayoutManager);
        linearLayoutManager.scrollToPosition(activeWeeks.size()-1);
        return view;
    }

}
