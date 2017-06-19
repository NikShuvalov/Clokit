package shuvalov.nikita.clokit.lifetime_results;

import java.util.ArrayList;

import shuvalov.nikita.clokit.pojos.Goal;

/**
 * Created by NikitaShuvalov on 6/16/17.
 */

public class LifetimeStatsManager {
    private ArrayList<Goal> mLifetimeGoalList;
    private int mSelectedOption;
    private long mTotalTime;
    private String mGoalName;

    private static LifetimeStatsManager sLifeTimeStatsManager;

    private LifetimeStatsManager() {
        mSelectedOption = 0;
        mLifetimeGoalList = new ArrayList<>();
        mTotalTime = -1;
    }

    public static LifetimeStatsManager getInstance() {
        if(sLifeTimeStatsManager == null){
            sLifeTimeStatsManager = new LifetimeStatsManager();
        }
        return sLifeTimeStatsManager;
    }

    public ArrayList<Goal> getLifetimeGoalList() {
        return mLifetimeGoalList;
    }

    public void setLifetimeGoalList(ArrayList<Goal> lifetimeGoalList) {

        mTotalTime = 0;
        mLifetimeGoalList = lifetimeGoalList;
        for(Goal g: lifetimeGoalList){
            mTotalTime+= g.getCurrentMilli();
        }
    }

    public String getGoalName(){
        return mGoalName;
    }

    public void setGoalName(String goalName){
        mGoalName = goalName;
    }

    public void setSelectedOption(int selectedOption) {
        mSelectedOption = selectedOption;
    }

    public int getSelectedOption() {
        return mSelectedOption;
    }

    public long getTotalTime() {
        return mTotalTime;
    }


}
