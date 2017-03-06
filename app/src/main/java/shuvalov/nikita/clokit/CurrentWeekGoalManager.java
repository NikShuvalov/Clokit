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

    public boolean addCurrentGoal(Goal goal){
        for(Goal existingGoal: mCurrentGoals){ //Quadratic, but it should be fine considering no one should be making more than double digit number of goals per week
            if(goal.getGoalName().equals(existingGoal.getGoalName()) && goal.getWeekNum() == existingGoal.getWeekNum()){
                return false;
            }
        }
        mCurrentGoals.add(goal);
        return true;
    }
}
