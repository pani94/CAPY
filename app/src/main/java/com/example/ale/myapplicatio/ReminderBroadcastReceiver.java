package com.example.ale.myapplicatio;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by ale on 29/11/2016.
 */

public class ReminderBroadcastReceiver extends BroadcastReceiver {

        @Override
        public void onReceive(Context context, Intent intent) {
            NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                    .setSmallIcon(R.drawable.common_google_signin_btn_icon_dark)
            .setContentTitle("")
            .setContentText("");
            Intent intentToFire = new Intent(context, Activity.class);
            PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToFire, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(pendingIntent);
            NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
        }
}
