package shuvalov.nikita.clokit.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import shuvalov.nikita.clokit.pojos.Week;
import shuvalov.nikita.clokit.R;

/**
 * Created by NikitaShuvalov on 3/6/17.
 */

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryViewHolder> {
    private ArrayList<Week> mActiveWeeks;
    private WeekSelectedListener mWeekSelectedListener;

    public HistoryRecyclerAdapter(ArrayList<Week> activeWeeks, WeekSelectedListener weekSelectedListener) {
        mActiveWeeks = activeWeeks;
        mWeekSelectedListener = weekSelectedListener;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_history, null));
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        final Week week = mActiveWeeks.get(position);
        holder.bindDataToViews(week);
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWeekSelectedListener.onWeekSelected(week);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mActiveWeeks.size();
    }

    public interface WeekSelectedListener{
        void onWeekSelected(Week week);
    }
}
