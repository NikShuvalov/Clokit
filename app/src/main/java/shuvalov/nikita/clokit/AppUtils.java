package shuvalov.nikita.clokit;

import android.content.SharedPreferences;
import android.util.Log;

import java.util.Calendar;

import shuvalov.nikita.clokit.pojos.Goal;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class AppUtils {

    /**
     * Use this to get the currentWeekNum in the format that the rest of this program uses.
     * @return The current Week Num as YYYYWW
     */
    public static int getCurrentWeekNum(){
        Calendar cal = Calendar.getInstance();
        int weekOfYear = cal.get(Calendar.WEEK_OF_YEAR);
        String weekOfYearString = weekOfYear<10 ? "0"+String.valueOf(weekOfYear) : String.valueOf(weekOfYear);
        String weekNumCoded = String.valueOf(cal.get(Calendar.YEAR))+weekOfYearString;
        return Integer.valueOf(weekNumCoded);
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


//    /**
//     * This method is less reliable, because it's just hardcoded to add the number of milliseconds in a week - 2 to the millis passed.
//     * That being said, though it's less reliable, it's also more flexible if user sets there own week start time, instead of midnight Monday.
//     *
//     * @param weekStartTimeInMillis Pass the week start time in millis.
//     * @return Returns time in millis of 1 week after the milli time entered minus 2 milliseconds.
//     */
//    public static long getWeekEndMillis(long weekStartTimeInMillis){
//        return (weekStartTimeInMillis + 604799998); //Should return Saturday 11:59:59:999am if Sunday 12:00:00:001am is passed.
//        //604799998 = milliseconds/week - 2;
//    }

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

    public static int getMinutesOfWork(long startTime, long currentTime){
        long timeSpent = currentTime - startTime;
        String timeSpentMinutes = String.valueOf(timeSpent/60000);
        return Integer.valueOf(timeSpentMinutes);
    }

    public static long getHoursOfWork(long currentMillis){
        return currentMillis/(60000*60);
    }

    public static String getDisplayForTimeLeft(){
        int currentWeek = getCurrentWeekNum();
        long weekEndMillis = getWeekEndMillis(currentWeek);
        long timeLeft = weekEndMillis - Calendar.getInstance().getTimeInMillis();
        return getHoursAndMinutes(timeLeft);
    }

    public static boolean isGoalCurrentlyActive(Goal goal, String activeName, String activeSubCat){
        if (activeName.equals(AppConstants.PREFERENCES_NO_GOAL)) {
            return false;
        }else if((goal.getGoalName().equals(activeName)) &&
                ((goal.getSubCategory()==null && activeSubCat==null) || (goal.getSubCategory()!=null && goal.getSubCategory().equals(activeSubCat)))){
            return true;
        }
        return false;
    }

    public static boolean isGoalCurrentlyActive(SharedPreferences sharedPreferences, String goalName, String subCat){
        String activeName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
        if (activeName.equals(AppConstants.PREFERENCES_NO_GOAL)) {
            return false;
        }
        String activeSubCat = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, AppConstants.PREFERENCES_NO_GOAL);
        if (!activeName.equals(goalName)){
            return false;
        }else if(activeSubCat.equals(AppConstants.PREFERENCES_NO_GOAL)){
            if(subCat.isEmpty()) {
                return true;
            }
        }else if(activeSubCat.equals(subCat)){
                return true;
            }
        return false;
    }

    //Consider using boolean to return with the changes made were succesful or not.
    public static void setActiveGoalToPreferences(SharedPreferences sharedPreferences, Goal goal){
        sharedPreferences.edit().putString(AppConstants.PREFERENCES_CURRENT_GOAL, goal.getGoalName()).apply(); //Save name of current goal.
        sharedPreferences.edit().putLong(AppConstants.PREFERENCES_START_TIME, System.currentTimeMillis()).apply(); //Save the current time of the selected goal.
        sharedPreferences.edit().putInt(AppConstants.PREFERENCES_CURRENT_GOAL_WEEK_NUM, getCurrentWeekNum()).apply(); //Adds the weeknum of when the goal was started
        sharedPreferences.edit().putString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, goal.getSubCategory()).apply(); //Saves the subcategory of current goal.
    }

    public static void resetActiveGoalPreferences(SharedPreferences sharedPreferences){
        sharedPreferences.edit().putString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL).apply();
        sharedPreferences.edit().putString(AppConstants.PREFERENCES_CURRENT_SUB_CAT, null).apply();
    }

    public static void addLifeTimeTotalTrackedTime(SharedPreferences sharedPreferences, long addedTime){
        long previousTotalTime = sharedPreferences.getLong(AppConstants.PREFERENCES_TOTAL_TRACKED_TIME, 0);
        long newTotal = previousTotalTime + addedTime;
        sharedPreferences.edit().putLong(AppConstants.PREFERENCES_TOTAL_TRACKED_TIME, newTotal).apply();
    }

    public static int getWeekOfYear(long timeInMillis){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(timeInMillis);
        return cal.get(Calendar.WEEK_OF_YEAR);
    }
}
