package shuvalov.nikita.clokit.pojos;

import shuvalov.nikita.clokit.AppUtils;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class Week {
    private long mStartTime, mEndTime;
    private int mWeekNum;

    public Week(long startTime, long endTime, int weekNum) {
        mStartTime = startTime;
        mEndTime = endTime;
        mWeekNum = weekNum;
    }

    public Week(int weekNum){
        mWeekNum = weekNum;
        mStartTime = AppUtils.getWeekStartMillis(weekNum);//ToDo: Move these functions from appUtils to here probably.
        mEndTime = AppUtils.getWeekEndMillis(weekNum);
    }

    public long getStartTime() {
        return mStartTime;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public int getWeekNum() {
        return mWeekNum;
    }
}
