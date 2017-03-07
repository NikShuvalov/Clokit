package shuvalov.nikita.clokit.POJOs;

import android.os.Build;

import java.util.Comparator;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class Goal {
    private String mGoalName;
    private long mCurrentMilli, mEndMilli;
    private long[] mWeekBreakdown;
    private int mWeekNum;

    public Goal(String goalName, long currentMilli, long endMilli, long[] weekBreakdown, int weekNum) {
        mGoalName = goalName;
        mCurrentMilli = currentMilli;
        mEndMilli = endMilli;
        mWeekBreakdown = weekBreakdown;
        mWeekNum = weekNum;
    }

    public Goal(String goalName, long currentMilli, long endMilli, int weekNum) {
        mGoalName = goalName;
        mCurrentMilli = currentMilli;
        mEndMilli = endMilli;
        mWeekBreakdown = new long[7];
        mWeekNum = weekNum;
    }


    public String getGoalName() {
        return mGoalName;
    }

    public void setGoalName(String goalName) {
        mGoalName = goalName;
    }

    public long getCurrentMilli() {
        return mCurrentMilli;
    }

    public void setCurrentMilli(long currentMilli) {
        mCurrentMilli = currentMilli;
    }

    public long getEndMilli() {
        return mEndMilli;
    }

    public void setEndMilli(long endMilli) {
        mEndMilli = endMilli;
    }

    public int getWeekNum() {
        return mWeekNum;
    }

    public void setWeekNum(int weekNum) {
        mWeekNum = weekNum;
    }

    public long addTimeSpent(long timeSpent){
        mCurrentMilli+= timeSpent;
        return mCurrentMilli;
    }
    public long getTimeLeft(){
        return mEndMilli-mCurrentMilli;
    }

    /**
     * Goes from 0-6, where 0 = Monday, 6 = Sunday;
     * @return Array for values per days of the week.
     */
    public long[] getWeekBreakdown() {
        return mWeekBreakdown;
    }

    public long getMondayValue(){
        return mWeekBreakdown[0];
    }

    public long getTuesdayValue(){
        return mWeekBreakdown[1];
    }

    public long getWednesdayValue(){
        return mWeekBreakdown[2];
    }

    public long getThursdayValue(){
        return mWeekBreakdown[3];
    }

    public long getFridayValue(){
        return mWeekBreakdown[4];
    }

    public long getSaturdayValue(){
        return mWeekBreakdown[5];
    }

    public long getSundayValue(){
        return mWeekBreakdown[6];
    }


}
