package shuvalov.nikita.clokit.POJOs;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class Week {
    long mStartTime, mEndTime, mWeekNum;

    public Week(long startTime, long endTime, long weekNum) {
        mStartTime = startTime;
        mEndTime = endTime;
        mWeekNum = weekNum;
    }

    public long getStartTime() {
        return mStartTime;
    }

    public long getEndTime() {
        return mEndTime;
    }

    public long getWeekNum() {
        return mWeekNum;
    }
}
