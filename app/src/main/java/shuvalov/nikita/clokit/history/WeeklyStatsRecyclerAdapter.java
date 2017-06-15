package shuvalov.nikita.clokit.history;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;

import shuvalov.nikita.clokit.AppConstants;
import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.pojos.Goal;

/**
 * Created by NikitaShuvalov on 6/13/17.
 */

public class WeeklyStatsRecyclerAdapter extends RecyclerView.Adapter<WeeklyStatsViewHolder> {
    private ArrayList<Goal> mGoals;
    private ArrayList<Integer> mColors;
    private boolean mUsingUnclocked;

    public WeeklyStatsRecyclerAdapter(ArrayList<Goal> goals, ArrayList<Integer> colors) {
        mGoals = (ArrayList<Goal>) goals.clone();
        mColors = colors;
        mUsingUnclocked = false;
    }

    @Override
    public WeeklyStatsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new WeeklyStatsViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_weekly_stats,null));
    }

    @Override
    public void onBindViewHolder(WeeklyStatsViewHolder holder, int position) {
        if(position == mGoals.size()-1){
            holder.bindDataToViews(mGoals.get(position), Color.LTGRAY);
        }else {
            holder.bindDataToViews(mGoals.get(position), mColors.get(position));
        }
    }

    @Override
    public int getItemCount() {
        if(mUsingUnclocked){
            return mGoals.size();
        }
        return mGoals.size()-1;
    }

    public void addUnclockedTime(int millisUnclocked){
        if(!mGoals.get(mGoals.size()-1).getGoalName().equals(AppConstants.UNCLOCKED_TIME)) {
            mGoals.add(new Goal(AppConstants.UNCLOCKED_TIME, millisUnclocked, -1, null, -1));
        }
    }

    public void setUsingUnclocked(boolean usingUnclocked){
        if(mUsingUnclocked!= usingUnclocked){
            mUsingUnclocked = usingUnclocked;
            notifyDataSetChanged();
        }
    }
}
