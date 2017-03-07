package shuvalov.nikita.clokit.History;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import shuvalov.nikita.clokit.POJOs.Week;
import shuvalov.nikita.clokit.R;

/**
 * Created by NikitaShuvalov on 3/6/17.
 */

public class HistoryRecyclerAdapter extends RecyclerView.Adapter<HistoryViewHolder> {
    private ArrayList<Week> mActiveWeeks;

    public HistoryRecyclerAdapter(ArrayList<Week> activeWeeks) {
        mActiveWeeks = activeWeeks;
    }

    @Override
    public HistoryViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new HistoryViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_history, null));
    }

    @Override
    public void onBindViewHolder(HistoryViewHolder holder, int position) {
        holder.bindDataToViews(mActiveWeeks.get(position));

    }

    @Override
    public int getItemCount() {
        return mActiveWeeks.size();
    }
}
