package shuvalov.nikita.clokit.pojos;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class Goal {
    private String mGoalName, mSubCategory;
    private long mCurrentMilli, mEndMilli;
    private long[] mWeekBreakdown;
    private int mWeekNum;


    public Goal(String goalName, long currentMilli, long endMilli, long[] weekBreakdown, int weekNum) {
        mGoalName = goalName;
        mCurrentMilli = currentMilli;
        mEndMilli = endMilli;
        mWeekBreakdown = weekBreakdown;
        mWeekNum = weekNum;
        mSubCategory = null;
    }



    public Goal(String goalName, long currentMilli, long endMilli, long[] weekBreakdown, int weekNum, String subCategory) {
        mGoalName = goalName;
        mCurrentMilli = currentMilli;
        mEndMilli = endMilli;
        mWeekBreakdown = weekBreakdown;
        mWeekNum = weekNum;
        mSubCategory = subCategory;
    }

    public Goal(String goalName, long currentMilli, long endMilli, int weekNum, String subCategory) {
        mGoalName = goalName;
        mCurrentMilli = currentMilli;
        mEndMilli = endMilli;
        mWeekBreakdown = new long[7];
        mWeekNum = weekNum;
        mSubCategory = subCategory;
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

    public long removeTimeSpent(long timeRemoved){
        mCurrentMilli-= timeRemoved;
        long overKill = mCurrentMilli;
        if(mCurrentMilli<0){
            mCurrentMilli = 0;
        }
        return overKill;
    }
    public long getTimeLeft(){
        long timeLeft = mEndMilli-mCurrentMilli;
        if(timeLeft<0){
            timeLeft=0;
        }
        return timeLeft;
    }

    public void applyChangedValues(Goal cachedGoal){
        mCurrentMilli = cachedGoal.mCurrentMilli;
        mWeekBreakdown = cachedGoal.mWeekBreakdown;
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

    public String getSubCategory() {
        return mSubCategory;
    }

    public void setSubCategory(String subCategory) {
        mSubCategory = subCategory;
    }

}
