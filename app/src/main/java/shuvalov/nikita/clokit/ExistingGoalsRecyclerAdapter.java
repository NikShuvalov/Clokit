package shuvalov.nikita.clokit;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import shuvalov.nikita.clokit.POJOs.Goal;

/**
 * Created by NikitaShuvalov on 3/7/17.
 */

public class ExistingGoalsRecyclerAdapter extends RecyclerView.Adapter<ExistingViewHolder>{
    private ArrayList<Goal> mExistingGoals;
    private int mSelectedPosition;
    private ExistingViewHolder mSelectedHolder;

    public ExistingGoalsRecyclerAdapter(ArrayList<Goal> existingGoals) {
        mExistingGoals = existingGoals;
        mSelectedPosition=-1;
    }

    @Override
    public ExistingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ExistingViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_existing, null));
    }

    @Override
    public void onBindViewHolder(final ExistingViewHolder holder, int position) {
        holder.bindDataToViews(mExistingGoals.get(position));
        if(position!=mSelectedPosition){
            holder.unSelectColor();
        }
        holder.mCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(mSelectedPosition == -1){ //User has not selected any position yet.
                    mSelectedPosition = holder.getAdapterPosition();
                    mSelectedHolder = holder;
                    holder.selectColor();
                }else if (mSelectedPosition == holder.getAdapterPosition()){//If user selected the same goal, unselect it.
                    mSelectedPosition=-1;
                    mSelectedHolder.unSelectColor();
                    mSelectedHolder = null;
                }else{//User had a previous selection, change it to the new selection.
                    mSelectedHolder.unSelectColor();
                    holder.selectColor();
                    mSelectedPosition = holder.getAdapterPosition();
                    mSelectedHolder= holder;

                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mExistingGoals.size();
    }

    public Goal getSelectedGoal(){
        if(mSelectedPosition == -1){
            return null; //There is nothing selected, return null and handle it in whatever is calling the method.
        }
        Goal selectedGoal = mExistingGoals.get(mSelectedPosition);
        return selectedGoal;
    }

    public void resetSelection(){
        if(mSelectedHolder!=null){
            mSelectedHolder.unSelectColor();
        }
        mSelectedPosition =-1;
        mSelectedHolder = null;
    }
}
