package com.example.user.sesilmovieapi.alarm;

import android.app.AlarmManager;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;

import com.example.user.sesilmovieapi.R;
import com.example.user.sesilmovieapi.activity.BottomNavActivity;
import com.example.user.sesilmovieapi.model.Movie;

import java.util.Calendar;
import java.util.List;

public class ReleaseAlarmRemainder extends BroadcastReceiver {
    private static int getNotification = 3;
    private static final String INTENT_ID = "intent_id";
    private static final String INTENT_TITLE = "intent_title";
    private static final int NOTIFICATION = 102;
    public static final String NOTIFICATION_CHANNEL = "102";

    @Override
    public void onReceive(Context context, Intent intent) {
        int notificationId = intent.getIntExtra(INTENT_ID, 1);
        Log.e("NotificationId", String.valueOf(notificationId));
        String title = intent.getStringExtra(INTENT_TITLE);
        Log.e("notificationId", title);
        showAlarmNotification(context, title, notificationId);
    }

    private void showAlarmNotification(Context context, String title, int notification){
        String message = "Movie is Release Today";
        NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

        Intent intent = new Intent(context,BottomNavActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, notification, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        Uri alarmRingtone = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                .setSmallIcon(R.drawable.ic_notifications_black_24dp)
                .setContentTitle(title)
                .setContentText(message)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setColor(ContextCompat.getColor(context, android.R.color.transparent))
                .setVibrate(new long[]{1000,1000,1000,1000,1000})
                .setSound(alarmRingtone);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel notificationChannel = new NotificationChannel(NOTIFICATION_CHANNEL, "NOTOFICATION+CHANNEL_NAME", importance);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.GREEN);
            notificationChannel.enableVibration(true);
            notificationChannel.setVibrationPattern(new long[]{100,200,300,400,500,400,300,200,400});

            builder.setChannelId(NOTIFICATION_CHANNEL);
            notificationManager.createNotificationChannel(notificationChannel);
        }

        notificationManager.notify(notification, builder.build());
    }

    public void setRepeatingAlarm(Context context, List<Movie> movies){
        int delay = 0;
        for (Movie movie: movies){
            cancelAlarm(context);
            AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
            Intent intent = new Intent(context, ReleaseAlarmRemainder.class);
            intent.putExtra(INTENT_TITLE, movie.getName());
            intent.putExtra("id", "1000");
            Log.e("title", movie.getName());
            intent.putExtra(INTENT_ID, getNotification);

            PendingIntent pendingIntent = PendingIntent.getBroadcast(context,100, intent, PendingIntent.FLAG_UPDATE_CURRENT);

            Calendar calendar = Calendar.getInstance();
            calendar.set(Calendar.HOUR_OF_DAY, 2);
            calendar.set(Calendar.MINUTE, 0);
            calendar.set(Calendar.SECOND, 0);

            int SDK_INT = Build.VERSION.SDK_INT;
            if (SDK_INT < Build.VERSION_CODES.KITKAT){
                alarmManager.set(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis() + delay, pendingIntent);
            }
            else if (SDK_INT > Build.VERSION_CODES.KITKAT && SDK_INT < Build.VERSION_CODES.M){
                alarmManager.setInexactRepeating(
                        AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis()+delay,
                        AlarmManager.INTERVAL_DAY,
                        pendingIntent
                );
            }
            else if (SDK_INT >= Build.VERSION_CODES.M){
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP,
                        calendar.getTimeInMillis() + delay, pendingIntent);
            }

            getNotification += 1;
            delay +=5000;
        }
    }

    public void cancelAlarm(Context context){
        AlarmManager alarmManager = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        alarmManager.cancel(getPendingIntent(context));
    }

    private static PendingIntent getPendingIntent(Context context){
        Intent alarmIntent = new Intent(context, DailyAlarmReceiver.class);
        return PendingIntent.getBroadcast(context, NOTIFICATION, alarmIntent,
                PendingIntent.FLAG_CANCEL_CURRENT);
    }
}
