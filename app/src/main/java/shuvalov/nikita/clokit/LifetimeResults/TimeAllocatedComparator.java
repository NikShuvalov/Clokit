package shuvalov.nikita.clokit.LifetimeResults;

import android.os.Build;

import java.util.Comparator;

import shuvalov.nikita.clokit.POJOs.Goal;

/**
 * Created by NikitaShuvalov on 3/6/17.
 */

public class TimeAllocatedComparator implements Comparator<Goal> {
    @Override
    public int compare(Goal goal2, Goal goal) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            return Long.compare(goal.getCurrentMilli(),goal2.getCurrentMilli());
        }else{
            int goalMinutes = (int)goal.getCurrentMilli()/60000;
            int goal2Minutes = (int)goal2.getCurrentMilli()/60000;
            return goalMinutes-goal2Minutes;
        }
    }
}
