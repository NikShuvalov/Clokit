package shuvalov.nikita.clokit.goaltracker;

import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v7.app.NotificationCompat;

import shuvalov.nikita.clokit.AppConstants;
import shuvalov.nikita.clokit.AppUtils;
import shuvalov.nikita.clokit.MainActivity;

public class GoalTrackerService extends Service {
    private static final String TAG = "GOAL_TRACKER_INTENT";
    public static final int NOTIFICATION_PERIOD = 2;
    private Runnable mRunnable;
    private Handler mHandler;
    public Context mContext = this;


    @Override
    public void onCreate() {
        super.onCreate();
        mHandler = new Handler();

    }

    @Override
    public int onStartCommand(final Intent intent, int flags, int startId) {
        mRunnable = new Runnable() {
            @Override
            public void run() {
                int lastMinuteNotified = intent.getIntExtra(AppConstants.INTENT_MINUTES_LAST_SOUND, 0);
                NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(getApplicationContext());
                SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.PREFERENCES_NAME,MODE_PRIVATE);
                String goalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
                long startTime = sharedPreferences.getLong(AppConstants.PREFERENCES_START_TIME,-1);
                if ((!goalName.equals(AppConstants.PREFERENCES_NO_GOAL)) && startTime != -1) {
                    int workedMin = AppUtils.getMinutesOfWork(startTime,System.currentTimeMillis());
                    NotificationCompat.BigTextStyle bigTextStyle = new android.support.v4.app.NotificationCompat.BigTextStyle();
                    String fullText = "You've selected to work on " + goalName+".\nSo far you've clocked "+workedMin+ " minutes.";
                    String sumText = workedMin + " minutes worked on "+ goalName;

                    bigTextStyle.setBigContentTitle("You're on the clock")
                            .setSummaryText(sumText)
                            .bigText(fullText);

                    Intent noteIntent = new Intent(mContext, MainActivity.class);
                    PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 0, noteIntent,0);
                    noteBuilder.setSmallIcon(android.R.drawable.ic_dialog_info)
                            .setContentTitle("You're on the clock")
                            .setContentText(sumText)
                            .setStyle(bigTextStyle)
                            .setContentIntent(pendingIntent)
                            .setOngoing(true);


                    int nextNotification = lastMinuteNotified+NOTIFICATION_PERIOD;
                    if(nextNotification<=workedMin){
                        Uri notifyUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                        noteBuilder.setSound(notifyUri);
                        intent.putExtra(AppConstants.INTENT_MINUTES_LAST_SOUND,workedMin);
                    }
                    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
                    notificationManager.notify(AppConstants.NOTIFICATION_ID,noteBuilder.build());

                    mHandler.postDelayed(mRunnable, 60000);
                }
            }
        };

        int lastMinuteNotified = intent.getIntExtra(AppConstants.INTENT_MINUTES_LAST_SOUND, 0);
        NotificationCompat.Builder noteBuilder = new NotificationCompat.Builder(getApplicationContext());
        SharedPreferences sharedPreferences = getSharedPreferences(AppConstants.PREFERENCES_NAME,MODE_PRIVATE);
        String goalName = sharedPreferences.getString(AppConstants.PREFERENCES_CURRENT_GOAL, AppConstants.PREFERENCES_NO_GOAL);
        long startTime = sharedPreferences.getLong(AppConstants.PREFERENCES_START_TIME,-1);

        if((!goalName.equals(AppConstants.PREFERENCES_NO_GOAL)) && startTime!=-1){
            mRunnable.run();
            int workedMin = AppUtils.getMinutesOfWork(startTime,System.currentTimeMillis());
            NotificationCompat.BigTextStyle bigTextStyle = new android.support.v4.app.NotificationCompat.BigTextStyle();
            String fullText = "You've selected to work on " + goalName+".\nSo far you've clocked "+workedMin+ " minutes.";
            String sumText = workedMin + " minutes worked on "+ goalName;

            bigTextStyle.setBigContentTitle("You're on the clock")
                    .setSummaryText(sumText)
                    .bigText(fullText);

            Intent noteIntent = new Intent(this, MainActivity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, noteIntent,0);
            noteBuilder.setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("You're on the clock")
                    .setContentText(sumText)
                    .setStyle(bigTextStyle)
                    .setContentIntent(pendingIntent)
                    .setOngoing(true);
            //.addAction(android.R.drawable.ic_media_pause, "Stop", new PendingIntent())

            int nextNotification = lastMinuteNotified+NOTIFICATION_PERIOD;
            if(nextNotification<=workedMin){
                Uri notifyUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
                noteBuilder.setSound(notifyUri);
                intent.putExtra(AppConstants.INTENT_MINUTES_LAST_SOUND,workedMin);
            }
            NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
            notificationManager.notify(AppConstants.NOTIFICATION_ID,noteBuilder.build());

            return Service.START_REDELIVER_INTENT;
        }
        return Service.START_NOT_STICKY;
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
