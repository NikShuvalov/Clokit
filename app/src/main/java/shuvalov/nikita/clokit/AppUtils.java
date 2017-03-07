package shuvalov.nikita.clokit;

import java.lang.annotation.Documented;
import java.util.Calendar;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class AppUtils {

    /**
     * Use this to get the currentWeekNum in the format that the rest of this program uses.
     * @return The current Week Num as YYYYW or YYYYWW
     */
    public static int getCurrentWeekNum(){
        Calendar cal = Calendar.getInstance();
        String weekNumCoded = String.valueOf(cal.get(Calendar.YEAR))+String.valueOf(cal.get(Calendar.WEEK_OF_YEAR));
        return Integer.valueOf(weekNumCoded);
    }

    /**
     * Use this Method to get the Monday start of the week entered.
     *
     * @param weekNum Pass weekNum in YYYYW or YYYYWW format. First 4 digits are year, and week of the year is concatenated.
     * @return Returns the most recent Monday 12:00:00:001am in millis.
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
        cal.set(Calendar.DAY_OF_WEEK, 2);
        cal.set(Calendar.MILLISECOND, 1);
        return cal.getTimeInMillis();
    }

    /**
     * Pass the weekNum to find the end of that week. This method is more reliable than the method that takes millis as parameter, and doesn't require a week start time.
     *
     * @param weekNum Pass WeekNum in YYYYW or YYYYWW format. First 4 digits are year, and week of the year is concatenated.
     * @return Returns Sunday 11:59:59:999pm in millis of the weekNum passed.
     */
    public static long getWeekEndMillis(int weekNum){//More reliable
        String weekNumAsString = String.valueOf(weekNum);
        int yearNum = Integer.parseInt(weekNumAsString.substring(0,4));
        weekNum = Integer.parseInt(weekNumAsString.substring(4));
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.WEEK_OF_YEAR, weekNum+1);
        cal.set(Calendar.YEAR, yearNum);
        cal.set(Calendar.HOUR_OF_DAY, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.DAY_OF_WEEK, 2);
        cal.set(Calendar.MILLISECOND, 0);
        return (cal.getTimeInMillis()-1);
    }


    /**
     * This method is less reliable, because it's just hardcoded to add the number of milliseconds in a week - 2 to the millis passed.
     * That being said, though it's less reliable, it's also more flexible if user sets there own week start time, instead of midnight Monday.
     *
     * @param weekStartTimeInMillis Pass the week start time in millis.
     * @return Returns time in millis of 1 week after the milli time entered minus 2 milliseconds.
     */
    public static long getWeekEndMillis(long weekStartTimeInMillis){
        return (weekStartTimeInMillis + 604799998); //Should return Sunday 11:59:59:999am if Monday 12:00:00:001am is passed.
        //604799998 = milliseconds/week - 2;
    }

    public static String getHoursAndMinutes(long timeInMillis){
        String timeAsString = "";
        long millisPerMinute = 1000*60;
        long millisperHour = millisPerMinute*60;

        long hours = timeInMillis/millisperHour;
        long minutes = (timeInMillis%millisperHour)/millisPerMinute;

        if(hours<10){
            timeAsString+="0";
        }
        timeAsString+=hours+":";
        if(minutes<10){
            timeAsString+="0";
        }
        timeAsString+= String.valueOf(minutes);
        return timeAsString;
    }

    public static String getLifetimeSummaryText(long timeInMillis){
        String timeAsString = "";
        long millisPerMinute = 1000*60;
        long millisperHour = millisPerMinute*60;

        long hours = timeInMillis/millisperHour;
        long minutes = (timeInMillis%millisperHour)/millisPerMinute;

        timeAsString+=hours+"Hour(s) ";
        timeAsString+= minutes+ "Minute(s)";
        return timeAsString;
    }

    public static String getDate(long timeInMillis){
        String date = "";
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(timeInMillis);

        int month = calendar.get(Calendar.MONTH)+1;
        int day = calendar.get(Calendar.DAY_OF_MONTH);
        int year = calendar.get(Calendar.YEAR);
        if(month<10){
            date+="0";
        }
        date+=month+"/";
        if(day<10){
            date+="0";
        }
        date+=day+"/"+year;
        return date;
    }
}
