package shuvalov.nikita.clokit.goaltracker;

import java.util.ArrayList;

import shuvalov.nikita.clokit.pojos.Goal;

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
        for(Goal existingGoal: mCurrentGoals){
            if(goal.getGoalName().toLowerCase()
                    .equals(existingGoal.getGoalName().toLowerCase()) &&
                    goal.getWeekNum() == existingGoal.getWeekNum()){//If the goalName and weekNum are identical, check to see if subcategories are the same as well.
                if(goal.getSubCategory() == null && existingGoal.getSubCategory()==null){ //If both have null subcategories, then treat them as the same.
                    return false;
                }else if (goal.getSubCategory()!=null && existingGoal.getSubCategory() != null
                        && goal.getSubCategory().toLowerCase().equals(existingGoal.getSubCategory().toLowerCase())){//If Neither are null, and the subcategories match, then they are the same.
                    return false;
                }
            }
        }
        mCurrentGoals.add(goal);
        return true;
    }

    public boolean removeGoal(Goal goal){
        for (int i = 0; i<mCurrentGoals.size(); i++){
            Goal storedGoal = mCurrentGoals.get(i);
            if(storedGoal.getGoalName().equals(goal.getGoalName()) &&
                    storedGoal.getSubCategory().equals(goal.getSubCategory()) &&
                    storedGoal.getWeekNum() == goal.getWeekNum()){
                mCurrentGoals.remove(i);
                return true;
            }
        }
        return false;
    }
}
