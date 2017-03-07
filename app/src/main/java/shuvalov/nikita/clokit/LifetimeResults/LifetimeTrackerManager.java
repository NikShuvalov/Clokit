package shuvalov.nikita.clokit.LifetimeResults;

import java.util.ArrayList;
import java.util.Collections;

import shuvalov.nikita.clokit.POJOs.Goal;


/**
 * Created by NikitaShuvalov on 3/6/17.
 */

public class LifetimeTrackerManager {
    private ArrayList<Goal> mLifetimeResults;

    private static LifetimeTrackerManager sLifetimeTrackerManager;

    private LifetimeTrackerManager(){
        mLifetimeResults = new ArrayList<>();
    }

    public static LifetimeTrackerManager getInstance() {
        if(sLifetimeTrackerManager==null){
            sLifetimeTrackerManager = new LifetimeTrackerManager();
        }
        return sLifetimeTrackerManager;
    }

    public ArrayList<Goal> getLifetimeResults() {
        return mLifetimeResults;
    }

    public void setLifetimeResults(ArrayList<Goal> lifetimeResults) {
        mLifetimeResults = lifetimeResults;
    }
    public void addLifeTimeResult(Goal goal){
        mLifetimeResults.add(goal);
    }

    public void sortByTimeAllocated(){
        Collections.sort(mLifetimeResults, new TimeAllocatedComparator());
    }
}
