package shuvalov.nikita.clokit.lifetime_results;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import shuvalov.nikita.clokit.pojos.Goal;
import shuvalov.nikita.clokit.R;

/**
 * Created by NikitaShuvalov on 3/6/17.
 */

public class LifetimeRecyclerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private ArrayList<Goal> mLifetimeResults;
    private GoalSelectedListener mGoalSelectedListener;

    public static final int HEADER = 0;
    public static final int CHILD = 1;

    public LifetimeRecyclerAdapter(ArrayList<Goal> lifetimeResults, GoalSelectedListener goalSelectedListener) {
        mLifetimeResults = lifetimeResults;
        mGoalSelectedListener = goalSelectedListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==HEADER){
            return new LifetimeHeaderViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_lifetime_header, null));
        }
        return new LifetimeViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_lifetime,null));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final Goal goal = mLifetimeResults.get(position);
        if (holder.getItemViewType() == 0) {
            ((LifetimeHeaderViewHolder) holder).bindDataToViews(goal);
            ((LifetimeHeaderViewHolder) holder).mContainerCard.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mGoalSelectedListener.onGoalSelected(goal.getGoalName());
                }
            });
        } else {
            ((LifetimeViewHolder) holder).bindDataToViews(goal);
            ((LifetimeViewHolder) holder).mContainerCard.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View view) {
                    mGoalSelectedListener.onGoalSelected(goal.getGoalName());
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        return mLifetimeResults.size();
    }

    @Override
    public int getItemViewType(int position) {
        if(position==0){
            return HEADER;
        }
        return CHILD;
    }

    public interface GoalSelectedListener{
        void onGoalSelected(String goalName);
    }
}
