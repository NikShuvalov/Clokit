package shuvalov.nikita.clokit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

import shuvalov.nikita.clokit.POJOs.Achievement;
import shuvalov.nikita.clokit.POJOs.Goal;
import shuvalov.nikita.clokit.POJOs.Week;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class GoalSQLHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "GOAL_DATABASE";
    public static final int DATABASE_VERSION = 1;

    //Table names
    public static final String LIFETIME_TABLE_NAME = "LIFETIME_TABLE";
    public static final String WEEKLY_TABLE_NAME = "WEEKLY_TABLE";
    public static final String WEEKS_REFERENCE_TABLE_NAME = "ACTIVE_WEEKS_TABLE";
    public static final String ACHIEVEMENTS_TABLE_NAME = "ACHIEVEMENTS_TABLE";

    //Non-table-specific(Shared) columns
    public static final String NAME_COLUMN = "NAME";
    public static final String TOTAL_TIME_COLUMN = "TOTAL_TIME";
    public static final String WEEK_NUM_COLUMN = "WEEK";


    //Weeks Reference columns
    public static final String START_DAY_COLUMN = "STARTED";
    public static final String END_DAY_COLUMN = "ENDED";


    //Weekly specific columns
    public static final String GOAL_TIME_COLUMN = "GOAL_TIME";
    public static final String MONDAY_TIME_COLUMN = "MONDAY";
    public static final String TUESDAY_TIME_COLUMN = "TUESDAY";
    public static final String WEDNESDAY_TIME_COLUMN = "WEDNESDAY";
    public static final String THURSDAY_TIME_COLUMN = "THURSDAY";
    public static final String FRIDAY_TIME_COLUMN = "FRIDAY";
    public static final String SATURDAY_TIME_COLUMN = "SATURDAY";
    public static final String SUNDAY_TIME_COLUMN = "SUNDAY";

    //Lifetime specific columns
    public static final String LAST_UPDATE = "LAST_UPDATED";

    //Achievement Specific columns
    public static final String ACHIEVEMENT_ID = "ACHIEVEMENT_ID";
    public static final String IMAGE_REFERENCE_COLUMN = "IMAGE_REFERENCE";

    //String for creating tables
    public static final String CREATE_LIFETIME_TABLE_EXE = "CREATE TABLE "+ LIFETIME_TABLE_NAME + " ("+
            NAME_COLUMN+ " TEXT PRIMARY KEY,"+
            TOTAL_TIME_COLUMN+ " INTEGER)";

    public static final String CREATE_WEEKLY_TABLE_EXE = "CREATE TABLE " + WEEKLY_TABLE_NAME + " ("+
            WEEK_NUM_COLUMN + " TEXT,"+
            NAME_COLUMN+ " TEXT,"+
            TOTAL_TIME_COLUMN + " INTEGER," +
            GOAL_TIME_COLUMN + " INTEGER," +
            MONDAY_TIME_COLUMN + " INTEGER," +
            TUESDAY_TIME_COLUMN + " INTEGER," +
            WEDNESDAY_TIME_COLUMN + " INTEGER," +
            THURSDAY_TIME_COLUMN + " INTEGER," +
            FRIDAY_TIME_COLUMN + " INTEGER," +
            SATURDAY_TIME_COLUMN + " INTEGER," +
            SUNDAY_TIME_COLUMN + " INTEGER, " +
            "PRIMARY KEY (" + WEEK_NUM_COLUMN+", "+NAME_COLUMN+"))";

    public static final String CREATE_ACHIEVEMENTS_TABLE_EXE = "CREATE TABLE "+ ACHIEVEMENTS_TABLE_NAME + " ("+
            ACHIEVEMENT_ID+ " INTEGER PRIMARY KEY," +
            IMAGE_REFERENCE_COLUMN + " INTEGER," +
            NAME_COLUMN + " TEXT)";

    public static final String CREATE_WEEKLY_REFERENCE_TABLE_EXE = "CREATE TABLE "+ WEEKS_REFERENCE_TABLE_NAME +" (" +
            WEEK_NUM_COLUMN + " TEXT PRIMARY KEY, " +
            START_DAY_COLUMN + " INTEGER, "+
            END_DAY_COLUMN + " INTEGER)";


    private static GoalSQLHelper sGoalSQLHelper;
    public static GoalSQLHelper getInstance(Context context) {
        if(sGoalSQLHelper==null){
            sGoalSQLHelper = new GoalSQLHelper(context,DATABASE_NAME,null,DATABASE_VERSION);
        }
        return sGoalSQLHelper;
    }


    public GoalSQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    public GoalSQLHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version, DatabaseErrorHandler errorHandler) {
        super(context, name, factory, version, errorHandler);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_LIFETIME_TABLE_EXE);
        sqLiteDatabase.execSQL(CREATE_WEEKLY_TABLE_EXE);
        sqLiteDatabase.execSQL(CREATE_WEEKLY_REFERENCE_TABLE_EXE);
        sqLiteDatabase.execSQL(CREATE_ACHIEVEMENTS_TABLE_EXE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ LIFETIME_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ WEEKLY_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ WEEKS_REFERENCE_TABLE_NAME);
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS "+ ACHIEVEMENTS_TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public ArrayList<Goal> getCurrentGoals(int weekNum){
        ArrayList<Goal> currentGoals = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String weekNumString = String.valueOf(weekNum);
        Cursor cursor = db.query(WEEKLY_TABLE_NAME, null, WEEK_NUM_COLUMN+ " = ?", new String[]{weekNumString}, null, null, null);
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(NAME_COLUMN));
                long totalTime = cursor.getLong(cursor.getColumnIndex(TOTAL_TIME_COLUMN));
                long goalTime = cursor.getLong(cursor.getColumnIndex(GOAL_TIME_COLUMN));
                long[] weekBreakdown = new long[7];
                weekBreakdown[0] = cursor.getLong(cursor.getColumnIndex(MONDAY_TIME_COLUMN));
                weekBreakdown[1] = cursor.getLong(cursor.getColumnIndex(TUESDAY_TIME_COLUMN));
                weekBreakdown[2] = cursor.getLong(cursor.getColumnIndex(WEDNESDAY_TIME_COLUMN));
                weekBreakdown[3] = cursor.getLong(cursor.getColumnIndex(THURSDAY_TIME_COLUMN));
                weekBreakdown[4] = cursor.getLong(cursor.getColumnIndex(FRIDAY_TIME_COLUMN));
                weekBreakdown[5] = cursor.getLong(cursor.getColumnIndex(SATURDAY_TIME_COLUMN));
                weekBreakdown[6] = cursor.getLong(cursor.getColumnIndex(SUNDAY_TIME_COLUMN));
                currentGoals.add(new Goal(name, totalTime,goalTime, weekBreakdown, weekNum));
                cursor.moveToNext();
            }
        }
        db.close();
        cursor.close();
        return currentGoals;
    }

    public Goal getCurrentGoalByName(String name){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(WEEKLY_TABLE_NAME, null, NAME_COLUMN + " = ?", new String[]{name},null, null, null);
        Goal goal= null;
        if(cursor.moveToFirst()){
            long currentSaved = cursor.getLong(cursor.getColumnIndex(TOTAL_TIME_COLUMN));
            long goalAmount = cursor.getLong(cursor.getColumnIndex(GOAL_TIME_COLUMN));
            long[] weekBreakdown = new long[7];
            weekBreakdown[0] = cursor.getLong(cursor.getColumnIndex(MONDAY_TIME_COLUMN));
            weekBreakdown[1] = cursor.getLong(cursor.getColumnIndex(TUESDAY_TIME_COLUMN));
            weekBreakdown[2] = cursor.getLong(cursor.getColumnIndex(WEDNESDAY_TIME_COLUMN));
            weekBreakdown[3] = cursor.getLong(cursor.getColumnIndex(THURSDAY_TIME_COLUMN));
            weekBreakdown[4] = cursor.getLong(cursor.getColumnIndex(FRIDAY_TIME_COLUMN));
            weekBreakdown[5] = cursor.getLong(cursor.getColumnIndex(SATURDAY_TIME_COLUMN));
            weekBreakdown[6] = cursor.getLong(cursor.getColumnIndex(SUNDAY_TIME_COLUMN));

            int weekNum = Integer.valueOf(cursor.getString(cursor.getColumnIndex(WEEK_NUM_COLUMN)));
            goal = new Goal(name, currentSaved,goalAmount,weekBreakdown, weekNum);
        }
        cursor.close();
        db.close();
        return goal;
    }

    public ArrayList<Week> getActiveWeeks(){
        ArrayList<Week> activeWeeks = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(WEEKS_REFERENCE_TABLE_NAME,null, null, null, null,  null, null, null);
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                int weekNum = Integer.valueOf(cursor.getString(cursor.getColumnIndex(WEEK_NUM_COLUMN)));
                long startTime = cursor.getLong(cursor.getColumnIndex(START_DAY_COLUMN));
                long endTime = cursor.getLong(cursor.getColumnIndex(END_DAY_COLUMN));
                activeWeeks.add(new Week(startTime,endTime,weekNum));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return activeWeeks;
    }

    public ArrayList<Goal> getLifetimeResults(){
        ArrayList<Goal> lifetimeResults = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(LIFETIME_TABLE_NAME, null, null, null, null, null, null);
        if(c.moveToFirst()){
            while(!c.isAfterLast()){
                String name = c.getString(c.getColumnIndex(NAME_COLUMN));
                long totalTime = c.getLong(c.getColumnIndex(TOTAL_TIME_COLUMN));
                lifetimeResults.add(new Goal(name,totalTime,-1,null,-1));
                c.moveToNext();
            }
        }
        c.close();
        db.close();
        return lifetimeResults;
    }

    public boolean updateLifetimeResults(ArrayList<Goal> weeklyResults){
        SQLiteDatabase db = getWritableDatabase();
        for(Goal result: weeklyResults){
            ContentValues content = new ContentValues();
            Cursor c = db.query(LIFETIME_TABLE_NAME, new String[]{TOTAL_TIME_COLUMN}, NAME_COLUMN+ " =  ?", new String[]{result.getGoalName()},null, null, null);
            long previous;
            long current = result.getCurrentMilli();
            content.put(NAME_COLUMN, result.getGoalName());

            if(c.moveToFirst()){
                previous = c.getLong(c.getColumnIndex(c.getColumnName(c.getColumnIndex(TOTAL_TIME_COLUMN))));
                long total = previous+ current;
                content.put(TOTAL_TIME_COLUMN, total);
                db.replace(LIFETIME_TABLE_NAME, null, content);
            }
            else {
                content.put(TOTAL_TIME_COLUMN, current);
                db.insert(LIFETIME_TABLE_NAME, null, content);
            }
            c.close();
        }
        db.close();
        return true;
    }

    public void addGoalToCurrentWeek(Goal goal){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_COLUMN,goal.getGoalName());
        contentValues.put(TOTAL_TIME_COLUMN, goal.getCurrentMilli());
        contentValues.put(GOAL_TIME_COLUMN, goal.getEndMilli());

        contentValues.put(MONDAY_TIME_COLUMN, goal.getMondayValue());
        contentValues.put(TUESDAY_TIME_COLUMN, goal.getTuesdayValue());
        contentValues.put(WEDNESDAY_TIME_COLUMN, goal.getWednesdayValue());
        contentValues.put(THURSDAY_TIME_COLUMN,goal.getThursdayValue());
        contentValues.put(FRIDAY_TIME_COLUMN, goal.getFridayValue());
        contentValues.put(SATURDAY_TIME_COLUMN, goal.getSaturdayValue());
        contentValues.put(SUNDAY_TIME_COLUMN, goal.getSundayValue());

        contentValues.put(WEEK_NUM_COLUMN, String.valueOf(goal.getWeekNum()));

        db.insert(WEEKLY_TABLE_NAME, null, contentValues);
        db.close();
    }

    public void updateTimeSpentCurrentWeek(Goal goal){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME_COLUMN,goal.getGoalName());
        contentValues.put(TOTAL_TIME_COLUMN, goal.getCurrentMilli());
        contentValues.put(GOAL_TIME_COLUMN, goal.getEndMilli());

        contentValues.put(MONDAY_TIME_COLUMN, goal.getMondayValue());
        contentValues.put(TUESDAY_TIME_COLUMN, goal.getTuesdayValue());
        contentValues.put(WEDNESDAY_TIME_COLUMN, goal.getWednesdayValue());
        contentValues.put(THURSDAY_TIME_COLUMN,goal.getThursdayValue());
        contentValues.put(FRIDAY_TIME_COLUMN, goal.getFridayValue());
        contentValues.put(SATURDAY_TIME_COLUMN, goal.getSaturdayValue());
        contentValues.put(SUNDAY_TIME_COLUMN, goal.getSundayValue());

        contentValues.put(WEEK_NUM_COLUMN, String.valueOf(goal.getWeekNum()));

        db.update(WEEKLY_TABLE_NAME,contentValues,
                NAME_COLUMN+ " = ? AND " + WEEK_NUM_COLUMN+ " = ?",
                new String[]{goal.getGoalName(), String.valueOf(goal.getWeekNum())});
        db.close();
    }

    public void addGoalsToCurrentWeek(ArrayList<Goal> massGoals){
        for(Goal goal: massGoals){
            addGoalToCurrentWeek(goal);
        }
    }

    public ArrayList<Achievement> getUnlockedAchievements(){
        ArrayList<Achievement> achievements = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.query(ACHIEVEMENTS_TABLE_NAME,null, null, null, null , null, null, null);
        if(c.moveToFirst()){
            while(!c.isAfterLast()){
                int id = c.getInt(c.getColumnIndex(ACHIEVEMENT_ID));
                int imageRes = c.getInt(c.getColumnIndex(IMAGE_REFERENCE_COLUMN));
                String name = c.getString(c.getColumnIndex(NAME_COLUMN));
                achievements.add(new Achievement(imageRes,id,name));
                c.moveToNext();
            }
        }
        c.close();
        db.close();
        return achievements;
    }

    public void unlockAchievement(Achievement achievement){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(ACHIEVEMENT_ID, achievement.getId());
        contentValues.put(IMAGE_REFERENCE_COLUMN, achievement.getImageReference());
        contentValues.put(NAME_COLUMN, achievement.getName());
        db.insertWithOnConflict(ACHIEVEMENTS_TABLE_NAME,null, contentValues,SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }

    public void addNewWeekReference(Week week){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEEK_NUM_COLUMN, week.getWeekNum());
        contentValues.put(START_DAY_COLUMN, week.getStartTime());
        contentValues.put(END_DAY_COLUMN, week.getEndTime());
        db.insertWithOnConflict(WEEKS_REFERENCE_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void addBulkWeekReferences(ArrayList<Week> importedWeeks){
        for(Week week: importedWeeks){
            addNewWeekReference(week);
        }
    }
}
