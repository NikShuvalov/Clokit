package shuvalov.nikita.clokit.pojos;

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
