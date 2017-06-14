package shuvalov.nikita.clokit.history;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.pojos.Goal;

/**
 * Created by NikitaShuvalov on 6/13/17.
 */

public class WeeklyStatsRecyclerAdapter extends RecyclerView.Adapter<WeeklyStatsViewHolder> {
    private ArrayList<Goal> mGoals;
    private ArrayList<Integer> mColors;

    public WeeklyStatsRecyclerAdapter(ArrayList<Goal> goals, ArrayList<Integer> colors) {
        mGoals = goals;
        mColors = colors;
    }

    @Override
    public WeeklyStatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WeeklyStatsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_weekly_stats,null));
    }

    @Override
    public void onBindViewHolder(WeeklyStatsViewHolder holder, int position) {
        holder.bindDataToViews(mGoals.get(position), mColors.get(position));
    }

    @Override
    public int getItemCount() {
        return mGoals.size();
    }
}
