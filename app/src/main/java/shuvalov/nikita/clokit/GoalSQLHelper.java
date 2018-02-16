package shuvalov.nikita.clokit;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.DatabaseErrorHandler;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import shuvalov.nikita.clokit.pojos.Achievement;
import shuvalov.nikita.clokit.pojos.Goal;
import shuvalov.nikita.clokit.pojos.Week;
import shuvalov.nikita.clokit.todo_list.ToDoItem;

/**
 * Created by NikitaShuvalov on 3/3/17.
 */

public class GoalSQLHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "GOAL_DATABASE";
    public static final int DATABASE_VERSION = 4;


    //Table names
    public static final String LIFETIME_TABLE_NAME = "LIFETIME_TABLE";
    public static final String WEEKLY_TABLE_NAME = "WEEKLY_TABLE";
    public static final String WEEKS_REFERENCE_TABLE_NAME = "ACTIVE_WEEKS_TABLE";
    public static final String ACHIEVEMENTS_TABLE_NAME = "ACHIEVEMENTS_TABLE";
    private static final String TO_DO_TABLE_NAME = "TO_DO_TABLE";

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
    public static final String SUBCATEGORY_COLUMN = "SUBCATEGORY";


    //Lifetime specific columns
    public static final String LAST_UPDATE = "LAST_UPDATED";

    //Achievement Specific columns
    public static final String ACHIEVEMENT_ID = "ACHIEVEMENT_ID";
    public static final String IMAGE_REFERENCE_COLUMN = "IMAGE_REFERENCE";

    //To-Do List columns
    private static final String DETAILS_COLUMN = "DETAILS";
    private static final String CREATED_TIME_COLUMN = "START";
    private static final String DUE_TIME_COLUMN = "DUE";
    private static final String COMPLETED_COLUMN = "COMPLETED";

    //String for creating tables
    public static final String CREATE_LIFETIME_TABLE_EXE = "CREATE TABLE "+ LIFETIME_TABLE_NAME + " ("+
            NAME_COLUMN+ " TEXT PRIMARY KEY,"+
            TOTAL_TIME_COLUMN+ " INTEGER)";

    public static final String CREATE_WEEKLY_TABLE_EXE = "CREATE TABLE " + WEEKLY_TABLE_NAME + " ("+
            WEEK_NUM_COLUMN + " TEXT,"+
            NAME_COLUMN+ " TEXT,"+
            SUBCATEGORY_COLUMN + " TEXT," +
            TOTAL_TIME_COLUMN + " INTEGER," +
            GOAL_TIME_COLUMN + " INTEGER," +
            MONDAY_TIME_COLUMN + " INTEGER," +
            TUESDAY_TIME_COLUMN + " INTEGER," +
            WEDNESDAY_TIME_COLUMN + " INTEGER," +
            THURSDAY_TIME_COLUMN + " INTEGER," +
            FRIDAY_TIME_COLUMN + " INTEGER," +
            SATURDAY_TIME_COLUMN + " INTEGER," +
            SUNDAY_TIME_COLUMN + " INTEGER, " +
            "PRIMARY KEY (" + WEEK_NUM_COLUMN+", "+NAME_COLUMN+", "+SUBCATEGORY_COLUMN+"))";

    public static final String CREATE_ACHIEVEMENTS_TABLE_EXE = "CREATE TABLE "+ ACHIEVEMENTS_TABLE_NAME + " ("+
            ACHIEVEMENT_ID+ " INTEGER, " +
            IMAGE_REFERENCE_COLUMN + " INTEGER, " +
            TOTAL_TIME_COLUMN + " INTEGER, " +
            NAME_COLUMN + " TEXT, "+
            "PRIMARY KEY (" + NAME_COLUMN + ", " + ACHIEVEMENT_ID+ "))";


    public static final String CREATE_WEEKLY_REFERENCE_TABLE_EXE = "CREATE TABLE "+ WEEKS_REFERENCE_TABLE_NAME +" (" +
            WEEK_NUM_COLUMN + " TEXT PRIMARY KEY, " +
            START_DAY_COLUMN + " INTEGER, "+
            END_DAY_COLUMN + " INTEGER)";

    private static final String CREATE_TODO_TABLE_EXE = "CREATE TABLE "+ TO_DO_TABLE_NAME + " (" +
            NAME_COLUMN + " TEXT, " +
            DETAILS_COLUMN + " TEXT, " +
            CREATED_TIME_COLUMN + " INTEGER PRIMARY KEY, " +
            DUE_TIME_COLUMN + " INTEGER, " +
            COMPLETED_COLUMN + " INTEGER)";

    private static final String DATABASE_UPGRADE_TO_2_SCRIPT = "ALTER TABLE "+  ACHIEVEMENTS_TABLE_NAME + " ADD COLUMN " + TOTAL_TIME_COLUMN + " INTEGER;";


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
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        //instead of dropping tables on upgrade: https://thebhwgroup.com/blog/how-android-sqlite-onupgrade
        if(oldVersion<2){
            sqLiteDatabase.execSQL(DATABASE_UPGRADE_TO_2_SCRIPT);
        }
        if(oldVersion<3){
            sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + ACHIEVEMENTS_TABLE_NAME);
            sqLiteDatabase.execSQL(CREATE_ACHIEVEMENTS_TABLE_EXE);
        }
        if(oldVersion<4){
            sqLiteDatabase.execSQL(CREATE_TODO_TABLE_EXE);
        }


    }

    public ArrayList<Goal> getGoalsByWeek(int weekNum){
        ArrayList<Goal> currentGoals = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        String weekNumString = String.valueOf(weekNum);
        Cursor cursor = db.query(WEEKLY_TABLE_NAME, null, WEEK_NUM_COLUMN+ " = ?", new String[]{weekNumString}, null, null, null);
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(NAME_COLUMN));
                String subCat = cursor.getString(cursor.getColumnIndex(SUBCATEGORY_COLUMN));
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
                currentGoals.add(new Goal(name, totalTime,goalTime, weekBreakdown, weekNum, subCat));
                cursor.moveToNext();
            }
        }
        db.close();
        cursor.close();
        return currentGoals;
    }

    /**
     * Retrieves ALL of the entries saved in the weekly table. Used this for migrating entries.
     * @return All entries in weekly table
     */
    //ToDo: Remove if I'm not going to be using FireBase, otherwise use this to sync data with Firebase.
    public ArrayList<Goal> getAllWeeklyGoals(){
        ArrayList<Goal> allGoals = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(WEEKLY_TABLE_NAME, null, null, null, null, null, null);
        if(cursor.moveToFirst()) {
            while (!cursor.isAfterLast()) {
                String name = cursor.getString(cursor.getColumnIndex(NAME_COLUMN));
                int weekNum = Integer.valueOf(cursor.getString(cursor.getColumnIndex(WEEK_NUM_COLUMN)));

                String subCat = cursor.getString(cursor.getColumnIndex(SUBCATEGORY_COLUMN));
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
                allGoals.add(new Goal(name, totalTime,goalTime, weekBreakdown, weekNum, subCat));

                cursor.moveToNext();
            }
        }
        db.close();
        cursor.close();
        return allGoals;

    }

    //Returns the amount of removed rows.
    public int removeCurrentGoal(String name, String subCategory, String weekNum){
        SQLiteDatabase db= getWritableDatabase();
        int removed = db.delete(WEEKLY_TABLE_NAME, NAME_COLUMN + " = ? AND "+ SUBCATEGORY_COLUMN + " = ? AND " + WEEK_NUM_COLUMN + " = ?", new String[]{name, subCategory, weekNum});
        db.close();
        return removed;
    }

    public int removeCurrentGoal(Goal goal){
        SQLiteDatabase db = getWritableDatabase();
        String weekNum = String.valueOf(goal.getWeekNum());
        int removed = db.delete(WEEKLY_TABLE_NAME, NAME_COLUMN + " = ? AND "+ SUBCATEGORY_COLUMN + " = ? AND " + WEEK_NUM_COLUMN + " = ?", new String[]{goal.getGoalName(), goal.getSubCategory(), weekNum});
        db.close();
        return removed;
    }

    public Goal getSpecificCurrentGoal(String name, String subcategory,String weekNum){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(WEEKLY_TABLE_NAME, null, NAME_COLUMN + " = ? AND " + SUBCATEGORY_COLUMN + " = ? AND " + WEEK_NUM_COLUMN + " = ?", new String[]{name, subcategory, weekNum},null, null, null);
        Goal goal= null;
        if(cursor.moveToFirst()){
            String subCat = cursor.getString(cursor.getColumnIndex(SUBCATEGORY_COLUMN));
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

            int weekNumber = Integer.valueOf(weekNum);
            goal = new Goal(name, currentSaved,goalAmount,weekBreakdown, weekNumber, subCat);

        }
        cursor.close();
        db.close();
        return goal;
    }

    public ArrayList<Goal> getLifetimeListForGoal(String goalName){
        ArrayList<Goal> goals = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(WEEKLY_TABLE_NAME, null, NAME_COLUMN + " = ?", new String[]{goalName},null, null, WEEK_NUM_COLUMN  + " ASC");
        if(cursor.moveToFirst()){
            while(!cursor.isAfterLast()){
                String subcat = cursor.getString(cursor.getColumnIndex(SUBCATEGORY_COLUMN));
                int weekNum = cursor.getInt(cursor.getColumnIndex(WEEK_NUM_COLUMN));
                long progressMilli = cursor.getLong(cursor.getColumnIndex(TOTAL_TIME_COLUMN));
                long goalTimeMilli = cursor.getLong(cursor.getColumnIndex(GOAL_TIME_COLUMN));

                long mondayMilli = cursor.getLong(cursor.getColumnIndex(MONDAY_TIME_COLUMN));
                long tuesdayMilli = cursor.getLong(cursor.getColumnIndex(TUESDAY_TIME_COLUMN));
                long wednesdayMilli = cursor.getLong(cursor.getColumnIndex(WEDNESDAY_TIME_COLUMN));
                long thursdayMilli = cursor.getLong(cursor.getColumnIndex(THURSDAY_TIME_COLUMN));
                long fridayMilli = cursor.getLong(cursor.getColumnIndex(FRIDAY_TIME_COLUMN));
                long saturdayMilli = cursor.getLong(cursor.getColumnIndex(SATURDAY_TIME_COLUMN));
                long sundayMilli = cursor.getLong(cursor.getColumnIndex(SUNDAY_TIME_COLUMN));

                goals.add(new Goal(goalName, progressMilli, goalTimeMilli,
                        new long[]{mondayMilli, tuesdayMilli, wednesdayMilli, thursdayMilli, fridayMilli, saturdayMilli, sundayMilli},
                        weekNum, subcat));
                cursor.moveToNext();
            }
        }
        cursor.close();
        db.close();
        return goals;
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

    /**
     * This method adds the amount of time spent on a specific goal to the lifetime total for that goal using the goal's name
     *
     * @param goalName The goal in question, just uses the name of it.
     * @param additionalTimeSpent The amount of time to append to the lifetime time spent.
     * @return The new lifetime time spent on the goal.
     */
    public long updateLifetimeByGoalName(String goalName, long additionalTimeSpent){
        SQLiteDatabase db = getWritableDatabase();
        long oldTimeSpent = 0;
        ContentValues contentValues = new ContentValues();
        Cursor c = db.query(LIFETIME_TABLE_NAME, new String[]{TOTAL_TIME_COLUMN}, NAME_COLUMN + " = ?", new String[]{goalName}, null, null, null);
        if(c.moveToFirst()){ //There should be only one row because goalNames are unique, so the first result is exactly what we're looking for, if it exists.
            Log.d("SQLHELPER", goalName+" was found");
            oldTimeSpent = c.getLong(c.getColumnIndex(TOTAL_TIME_COLUMN));
            contentValues.put(TOTAL_TIME_COLUMN, additionalTimeSpent+oldTimeSpent);
            db.update(LIFETIME_TABLE_NAME, contentValues,
                    NAME_COLUMN + " = ?",
                    new String[]{goalName});
        }else{ //If the goal doesn't already exist in the lifetime table then insert it into it with the timespent.
            Log.d("SQLHELPER", goalName + " not found");
            contentValues.put(TOTAL_TIME_COLUMN, additionalTimeSpent);
            contentValues.put(NAME_COLUMN, goalName);
            db.insert(LIFETIME_TABLE_NAME, null, contentValues);
        }
        c.close();
        db.close();
        return oldTimeSpent+additionalTimeSpent;
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


    public void addGoalToWeeklyTable(Goal goal){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(NAME_COLUMN,goal.getGoalName());
        contentValues.put(SUBCATEGORY_COLUMN, goal.getSubCategory());
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

        //In case the user creates a task a week prior, it's difficult to account for that if a task crosses into a second week, therefore, I'll just replace it. The time spent on the task should remain correct.
        db.insertWithOnConflict(WEEKLY_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public void updateTimeSpentOnGoal(Goal goal){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();

        contentValues.put(NAME_COLUMN,goal.getGoalName());
        contentValues.put(SUBCATEGORY_COLUMN, goal.getSubCategory());
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
                NAME_COLUMN+ " = ? AND " + WEEK_NUM_COLUMN+ " = ? AND " + SUBCATEGORY_COLUMN + " = ?",
                new String[]{goal.getGoalName(), String.valueOf(goal.getWeekNum()),goal.getSubCategory()});
        db.close();
    }

    public void addGoalsToCurrentWeek(ArrayList<Goal> massGoals){
        for(Goal goal: massGoals){
            addGoalToWeeklyTable(goal);
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
        contentValues.put(TOTAL_TIME_COLUMN, achievement.getTimeOrRepeats());
        db.insertWithOnConflict(ACHIEVEMENTS_TABLE_NAME,null, contentValues,SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }

    public void migrateUnlockedAchievements(ArrayList<Achievement> unlockedAchievemnts){
        for(Achievement achievement: unlockedAchievemnts){
            unlockAchievement(achievement);
        }
    }

    public void addNewWeekReference(Week week){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(WEEK_NUM_COLUMN, week.getWeekNum());
        contentValues.put(START_DAY_COLUMN, week.getStartTime());
        contentValues.put(END_DAY_COLUMN, week.getEndTime());
        db.insertWithOnConflict(WEEKS_REFERENCE_TABLE_NAME, null, contentValues, SQLiteDatabase.CONFLICT_IGNORE);
        db.close();
    }

    public int removeWeekReference(int weekNum){
        String weekNumString = String.valueOf(weekNum);
        SQLiteDatabase db = getWritableDatabase();
        int deletedRows = db.delete(WEEKS_REFERENCE_TABLE_NAME, WEEK_NUM_COLUMN + " = ? ", new String[]{weekNumString});
        db.close();
        return deletedRows;
    }

    public void addBulkWeekReferences(ArrayList<Week> importedWeeks){
        for(Week week: importedWeeks){
            addNewWeekReference(week);
        }
    }

    public String getLastActiveWeekNum(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c =db.query(WEEKS_REFERENCE_TABLE_NAME,null, null, null, null ,null, null, null);
        if(c.moveToLast()){//Move cursor to last, as that will be the most recent active week.
            String lastActiveWeek = c.getString(c.getColumnIndex(WEEK_NUM_COLUMN));
            if(lastActiveWeek.equals(String.valueOf(AppUtils.getCurrentWeekNum()))){ //Is the recent week the same as the current week? Then import from the row before this one.
                if(c.moveToPrevious()){
                    lastActiveWeek = c.getString(c.getColumnIndex(WEEK_NUM_COLUMN));
                }else{ //There's no row before the most recent? Gosh darn it! Let's return null.
                    //This runs if there's a single row, which happens to be this week.
                    db.close();
                    c.close();
                    return null;
                }
            }
            //This runs if the last week on file is not this week, in other words no tracking was logged this week.
            db.close();
            c.close();
            return lastActiveWeek;
        }
        //This runs if there's no weeks at all found in history.
        db.close();
        c.close();
        return null;
    }

    //ToDo: Implement this somewhere so that user can change goalNames
    public void refactorGoalName(String goalNameToChange, String newGoalName){
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(WEEKLY_TABLE_NAME, null, NAME_COLUMN + " = ?", new String[]{goalNameToChange}, null, null, null);
        if(c.moveToFirst()){
            while(!c.isAfterLast()){
                long[] weekBreakdown = new long[7];
                weekBreakdown[0] = c.getLong(c.getColumnIndex(MONDAY_TIME_COLUMN));
                weekBreakdown[1] = c.getLong(c.getColumnIndex(TUESDAY_TIME_COLUMN));
                weekBreakdown[2] = c.getLong(c.getColumnIndex(WEDNESDAY_TIME_COLUMN));
                weekBreakdown[3] = c.getLong(c.getColumnIndex(THURSDAY_TIME_COLUMN));
                weekBreakdown[4] = c.getLong(c.getColumnIndex(FRIDAY_TIME_COLUMN));
                weekBreakdown[5] = c.getLong(c.getColumnIndex(SATURDAY_TIME_COLUMN));
                weekBreakdown[6] = c.getLong(c.getColumnIndex(SUNDAY_TIME_COLUMN));

                long timeSpent = c.getLong(c.getColumnIndex(TOTAL_TIME_COLUMN));
                long goalTime = c.getLong(c.getColumnIndex(GOAL_TIME_COLUMN));
                int weekNum = c.getInt(c.getColumnIndex(WEEK_NUM_COLUMN));
                String subCat = c.getString(c.getColumnIndex(SUBCATEGORY_COLUMN));

                Goal refactoredGoal = new Goal(
                        newGoalName,timeSpent
                        ,goalTime
                        ,weekBreakdown
                        ,weekNum,
                        subCat
                );
                mergeGoalIfExists(refactoredGoal);
                updateLifetimeByGoalName(newGoalName,timeSpent);
                if(removeLifetimeEntry(goalNameToChange)==0){
                    Log.w("SQL Error", "Attempt to delete an entry that doesn't exist");
                }
                removeCurrentGoal(goalNameToChange, subCat,String.valueOf(weekNum));
                c.moveToNext();
            }
        }
        db.close();
        c.close();
    }

    public void refactorGoalSubCat(String goalName, String oldSubcat, String newSubCat){
        if(newSubCat.equals(oldSubcat)){
            return;
        }
        SQLiteDatabase db = getWritableDatabase();
        Cursor c = db.query(WEEKLY_TABLE_NAME, null, NAME_COLUMN + " = ? AND " + SUBCATEGORY_COLUMN + " = ?", new String[]{goalName, oldSubcat}, null, null, null);
        if(c.moveToFirst()){
            while(!c.isAfterLast()){
                long[] weekBreakdown = new long[7];
                weekBreakdown[0] = c.getLong(c.getColumnIndex(MONDAY_TIME_COLUMN));
                weekBreakdown[1] = c.getLong(c.getColumnIndex(TUESDAY_TIME_COLUMN));
                weekBreakdown[2] = c.getLong(c.getColumnIndex(WEDNESDAY_TIME_COLUMN));
                weekBreakdown[3] = c.getLong(c.getColumnIndex(THURSDAY_TIME_COLUMN));
                weekBreakdown[4] = c.getLong(c.getColumnIndex(FRIDAY_TIME_COLUMN));
                weekBreakdown[5] = c.getLong(c.getColumnIndex(SATURDAY_TIME_COLUMN));
                weekBreakdown[6] = c.getLong(c.getColumnIndex(SUNDAY_TIME_COLUMN));

                long timeSpent = c.getLong(c.getColumnIndex(TOTAL_TIME_COLUMN));
                long goalTime = c.getLong(c.getColumnIndex(GOAL_TIME_COLUMN));
                int weekNum = c.getInt(c.getColumnIndex(WEEK_NUM_COLUMN));
                Goal refactoredGoal = new Goal(
                        goalName,timeSpent
                        ,goalTime
                        ,weekBreakdown
                        ,weekNum,
                        newSubCat
                );
                mergeGoalIfExists(refactoredGoal);
                if(!oldSubcat.equals(newSubCat)) {
                    removeCurrentGoal(goalName, oldSubcat, String.valueOf(weekNum));
                }
                c.moveToNext();
            }
        }
        db.close();
        c.close();
    }

    private int removeLifetimeEntry(String goalName){
        SQLiteDatabase db = getWritableDatabase();
        int i = db.delete(LIFETIME_TABLE_NAME, NAME_COLUMN + " = ? ", new String[]{goalName});
        db.close();
        return i;

    }

    private void mergeGoalIfExists(Goal goal){
        Goal existingEntry = getSpecificCurrentGoal(goal.getGoalName(), goal.getSubCategory(), String.valueOf(goal.getWeekNum()));
        if(existingEntry==null){
            addGoalToWeeklyTable(goal);
            return;
        }
        long newCurrentMilli = existingEntry.getCurrentMilli() + goal.getCurrentMilli();
        long newGoalMilli = existingEntry.getEndMilli() + goal.getEndMilli();
        existingEntry.setCurrentMilli(newCurrentMilli);
        existingEntry.setEndMilli(newGoalMilli);
        for(int i = 0; i<7; i++){
            existingEntry.getWeekBreakdown()[i] += goal.getWeekBreakdown()[i];
        }
        addGoalToWeeklyTable(existingEntry);
    }

    @NonNull
    private ToDoItem parseToDoItemCursor(Cursor c){
        String name = c.getString(c.getColumnIndex(NAME_COLUMN));
        String details = c.getString(c.getColumnIndex(DETAILS_COLUMN));
        long start = c.getLong(c.getColumnIndex(CREATED_TIME_COLUMN));
        long due = c.getLong(c.getColumnIndex(DUE_TIME_COLUMN));
        boolean isComplete = c.getInt(c.getColumnIndex(COMPLETED_COLUMN)) == 1;
        return new ToDoItem(name, details, start, due, isComplete);
    }

    private ContentValues parseToDoItemContentValues(ToDoItem item){
        ContentValues cv = new ContentValues();
        cv.put(NAME_COLUMN, item.getName());
        cv.put(DETAILS_COLUMN, item.getDescription());
        cv.put(CREATED_TIME_COLUMN, item.getCreated());
        cv.put(DUE_TIME_COLUMN, item.getDue());
        cv.put(COMPLETED_COLUMN, item.isComplete() ? 1 : 0);
        return cv;
    }

    public void addToDoItem(ToDoItem item){
        SQLiteDatabase db = getWritableDatabase();
        db.insertWithOnConflict(TO_DO_TABLE_NAME, null, parseToDoItemContentValues(item), SQLiteDatabase.CONFLICT_REPLACE);
        db.close();
    }

    public List<ToDoItem> getToDoList(){
        SQLiteDatabase db = getReadableDatabase();
        List<ToDoItem> toDoList = new ArrayList<>();
        Cursor c = db.query(TO_DO_TABLE_NAME, null, null, null, null, null, null);
        if(c.moveToFirst()){
            while(!c.isAfterLast()){
                toDoList.add(parseToDoItemCursor(c));
                c.moveToNext();
            }
        }
        c.close();
        db.close();
        return toDoList;
    }

    public boolean updateToDoItem(ToDoItem item){
        SQLiteDatabase db = getWritableDatabase();
        boolean updated = db.update(TO_DO_TABLE_NAME, parseToDoItemContentValues(item), CREATED_TIME_COLUMN + " = ? ", new String[]{String.valueOf(item.getCreated())}) == 1;
        db.close();
        return updated;
    }

    public void removeCompletedToDo(){
        SQLiteDatabase db = getWritableDatabase();
        db.delete(TO_DO_TABLE_NAME, COMPLETED_COLUMN + " = ?", new String[]{"1"});
        db.close();
    }
}
