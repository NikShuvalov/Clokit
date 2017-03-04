package shuvalov.nikita.clokit;

import java.util.Calendar;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class AppUtils {
    public static int getCurrentWeekNum(){
        Calendar cal = Calendar.getInstance();
        String weekNumCoded = String.valueOf(cal.get(Calendar.YEAR))+String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));
        return Integer.valueOf(weekNumCoded);
    }

    /**
     * Use this Method to get the Monday start of the week entered. Returns the most recent Monday 12:00:01am
     *
     * @param weekNum Pass weekNum in YYYYW(W) format. First 4 digits is year, and week of the year is concatenated.
     * @return
     */
    public static long getWeekStartMillis(int weekNum){
        String weekNumAsString = String.valueOf(weekNum);
        int yearNum = Integer.parseInt(weekNumAsString.substring(0,4));
        weekNum = Integer.parseInt(weekNumAsString.substring(4));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.WEEK_OF_YEAR, weekNum);
        cal.set(Calendar.YEAR, yearNum);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 1);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        cal.set(Calendar.MILLISECOND, 0);
        return cal.getTimeInMillis();
    }
}
