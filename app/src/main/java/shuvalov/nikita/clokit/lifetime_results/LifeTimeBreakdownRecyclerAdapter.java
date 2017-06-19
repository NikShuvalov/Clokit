package shuvalov.nikita.clokit.lifetime_results;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.HashMap;

import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.pojos.Goal;

/**
 * Created by NikitaShuvalov on 6/15/17.
 */

public class LifeTimeBreakdownRecyclerAdapter extends RecyclerView.Adapter<LifeTimeBreakDownViewHolder> {
    private HashMap<String, Long> mConsolidatedGoals;
    private ArrayList<String> mKeys;

    public LifeTimeBreakdownRecyclerAdapter(ArrayList<Goal> goalList){
        mConsolidatedGoals = new HashMap<>();
        mKeys = new ArrayList<>();
        for(Goal g: goalList){
            String subCat = g.getSubCategory();
            if(mConsolidatedGoals.containsKey(subCat)){
                Long value = mConsolidatedGoals.get(subCat);
                mConsolidatedGoals.put(subCat,value +g.getCurrentMilli());
            }else{
                mKeys.add(subCat);
                mConsolidatedGoals.put(subCat, g.getCurrentMilli());
            }
        }
    }

    @Override
    public LifeTimeBreakDownViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new LifeTimeBreakDownViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_lifetime_breakdown,null));
    }

    @Override
    public void onBindViewHolder(LifeTimeBreakDownViewHolder holder, int position) {
        if(mKeys.isEmpty()){
            holder.emptyListPrompt();
        }else {
            String key = mKeys.get(position);
            holder.bindDataToViews(key, mConsolidatedGoals.get(key), LifetimeStatsManager.getInstance().getTotalTime());
        }
    }

    @Override
    public int getItemCount() {
        return mKeys.size()== 0 ?
                1:
                mKeys.size();
    }
}
