package shuvalov.nikita.clokit.goaltracker;

import android.app.IntentService;
import android.app.NotificationManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.NotificationCompat;
import android.util.Log;

import shuvalov.nikita.clokit.AppConstants;
import shuvalov.nikita.clokit.AppUtils;

public class GoalTrackerIntentService extends IntentService {
    private static final String TAG = "GOAL_TRACKER_INTENT";

    public GoalTrackerIntentService(){
        super(TAG);
    }


    public GoalTrackerIntentService(String name) {
        super(name);
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.d(TAG, "Started handleIntent");
        NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(getApplicationContext());
        SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.PREFERENCES_NAME,MODE_PRIVATE);
        String goalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
        long startTime = sharedPreferences.getLong(AppConstants.PREFERENCES_START_TIME,-1);
        while((!goalName.equals(AppConstants.PREFERENCES_NO_GOAL)) && startTime!=-1){
            int workedMin = AppUtils.getMinutesOfWork(startTime,System.currentTimeMillis());

            NotificationCompat.BigTextStyle bigTextStyle = new android.support.v4.app.NotificationCompat.BigTextStyle();

            String fullText = "You've selected to work on " + goalName+".\nSo far you've clocked "+workedMin+ " minutes.";
            String sumText = workedMin + " minutes worked on "+ goalName;

            bigTextStyle.setBigContentTitle("You're on the clock")
                    .setSummaryText(sumText)
                    .bigText(fullText);

            noteBuilder.setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("You're on the clock")
                    .setContentText(sumText)
                    .setStyle(bigTextStyle)
                    .setOngoing(true);
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(AppConstants.NOTIFICATION_ID,noteBuilder.build());

            try {
                Thread.sleep(60000);
                goalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }
}
