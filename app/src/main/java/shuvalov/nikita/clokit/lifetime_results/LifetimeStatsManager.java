package shuvalov.nikita.clokit.lifetime_results;

import java.util.ArrayList;

import shuvalov.nikita.clokit.pojos.Goal;

/**
 * Created by NikitaShuvalov on 6/16/17.
 */

public class LifetimeStatsManager {
    private ArrayList<Goal> mLifetimeGoalList;
    private String mGoalName;

    private static LifetimeStatsManager sLifeTimeStatsManager;

    private LifetimeStatsManager() {
        mLifetimeGoalList = new ArrayList<>();
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
        mLifetimeGoalList = lifetimeGoalList;
    }

    public String getGoalName(){
        return mLifetimeGoalList.get(0).getGoalName();
    }

}
