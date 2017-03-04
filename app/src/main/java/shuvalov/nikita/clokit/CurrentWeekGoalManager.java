package shuvalov.nikita.clokit;

import java.util.ArrayList;

import shuvalov.nikita.clokit.POJOs.Goal;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class CurrentWeekGoalManager {
    private ArrayList<Goal> mCurrentGoals;

    private static CurrentWeekGoalManager sCurrentWeekGoalManager;

    public static CurrentWeekGoalManager getInstance() {
        if(sCurrentWeekGoalManager==null){
            sCurrentWeekGoalManager = new CurrentWeekGoalManager();
        }
        return sCurrentWeekGoalManager;
    }

    private CurrentWeekGoalManager(){
        mCurrentGoals = new ArrayList<>();
    }


    public ArrayList<Goal> getCurrentGoals() {
        return mCurrentGoals;
    }

    public void setCurrentGoals(ArrayList<Goal> currentGoals) {
        mCurrentGoals = currentGoals;
    }
}
