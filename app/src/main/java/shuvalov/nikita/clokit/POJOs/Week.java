package shuvalov.nikita.clokit.pojos;

import java.util.Calendar;

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
        mStartTime = getWeekStartMillis(weekNum);
        mEndTime = getWeekEndMillis(weekNum);
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

    /**
     * Use this Method to get the Sunday start of the week entered.
     *
     * @param weekNum Pass weekNum in YYYYW or YYYYWW format. First 4 digits are year, and week of the year is concatenated.
     * @return Returns the most recent Sunday 12:00:00:001am in millis.
     */
    public static long getWeekStartMillis(int weekNum){
        String weekNumAsString = String.valueOf(weekNum);
        int yearNum = Integer.parseInt(weekNumAsString.substring(0,4));
        weekNum = Integer.parseInt(weekNumAsString.substring(4));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.WEEK_OF_YEAR, weekNum);
        cal.set(Calendar.YEAR, yearNum);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.DAY_OF_WEEK, 1);
        cal.set(Calendar.MILLISECOND, 1);
        return cal.getTimeInMillis();
    }

    /**
     * Pass the weekNum to find the end of that week. This method is more reliable than the method that takes millis as parameter, and doesn't require a week start time.
     *
     * @param weekNum Pass WeekNum in YYYYW or YYYYWW format. First 4 digits are year, and week of the year is concatenated.
     * @return Returns Saturday 11:59:59:999pm in millis of the weekNum passed.
     */
    public static long getWeekEndMillis(int weekNum){//More reliable
        String weekNumAsString = String.valueOf(weekNum);
        int yearNum = Integer.parseInt(weekNumAsString.substring(0,4));
        weekNum = Integer.parseInt(weekNumAsString.substring(4));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.YEAR, yearNum);
        cal.set(Calendar.WEEK_OF_YEAR, weekNum);
        cal.set(Calendar.DAY_OF_WEEK, 7);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTimeInMillis();
    }
}
