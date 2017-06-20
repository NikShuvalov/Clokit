package shuvalov.nikita.clokit.lifetime_results;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.CompoundButton;

import java.util.ArrayList;
import java.util.HashMap;

import shuvalov.nikita.clokit.R;
import shuvalov.nikita.clokit.pojos.Goal;

/**
 * Created by NikitaShuvalov on 6/15/17.
 */

public class LifeTimeBreakdownRecyclerAdapter extends RecyclerView.Adapter<LifeTimeBreakDownViewHolder>{
    private HashMap<String, Long> mConsolidatedGoals;
    private ArrayList<String> mKeys;
    private ArrayList<Boolean> mCheckedPositions;
    private GoalCheckedListener mGoalCheckedListener;

    public LifeTimeBreakdownRecyclerAdapter(ArrayList<Goal> goalList, GoalCheckedListener goalCheckedListener){
        mGoalCheckedListener = goalCheckedListener;
        consolidateAndCreateKeys(goalList);
    }

    private void consolidateAndCreateKeys(ArrayList<Goal> goalList){
        mConsolidatedGoals = new HashMap<>();
        mCheckedPositions = new ArrayList<>();
        for(int i = 0; i < goalList.size(); i++){
            mCheckedPositions.add(false);
        }
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
    public void onBindViewHolder(final LifeTimeBreakDownViewHolder holder, int position) {
        holder.mCheckBox.setChecked(mCheckedPositions.get(position));
        holder.mCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                mCheckedPositions.set(holder.getAdapterPosition(), b);
                mGoalCheckedListener.onCheckedStatusChange(b || mCheckedPositions.contains(true));
            }
        });
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

    interface GoalCheckedListener{
        void onCheckedStatusChange(boolean b);
    }

    public void updateGoalList(ArrayList<Goal> updatedGoalList){
        consolidateAndCreateKeys(updatedGoalList);
        notifyDataSetChanged();
    }

    public ArrayList<String> getSelectedSubCategories(){
        if(!mCheckedPositions.contains(true)){
            return null;
        }
        ArrayList<String> selectedGoals = new ArrayList<>();
        for(int i =0; i< mConsolidatedGoals.size(); i++){
            if(mCheckedPositions.get(i)){
                selectedGoals.add(mKeys.get(i));
            }
        }
        return selectedGoals;
    }

    public void unselectAll(){
        for(int i = 0; i < mCheckedPositions.size(); i ++){
            mCheckedPositions.set(i, false);
        }
        notifyDataSetChanged();
        mGoalCheckedListener.onCheckedStatusChange(false);
    }


}
