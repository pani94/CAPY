package com.example.ale.myapplicatio;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.RingtoneManager;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

public class ReminderBroadcastReceiver extends BroadcastReceiver {
        //private final String SOMEACTION = "com.example.ale.alarm.ACTION";
        @Override
        public void onReceive(Context context, Intent intent) {
            SharedPreferences prefs;
            // set the default values for the preferences
            PreferenceManager.setDefaultValues(context, R.xml.preferences, false);

            // get default SharedPreferences object
            prefs = PreferenceManager.getDefaultSharedPreferences(context);

            Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(),R.drawable.logo_pani_piccolo);

            if(prefs.getBoolean("preference_notification", true)){
                if(intent.getAction().equals("one_day_before")){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context);
                        builder.setSmallIcon(R.drawable.logo_pani_piccolo)
                                .setLargeIcon(bitmap)
                            .setContentTitle("Let's go")
                            .setContentText(intent.getStringExtra("nomeviaggio"))
                            .setSubText("Sei pronto per la partenza?")
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setAutoCancel(true);
                    Intent intentToFire = new Intent(context, ProfiloViaggiActivity.class);
                    intentToFire.putExtra("notifica", intent.getStringExtra("nomeviaggio"));
                    intentToFire.putExtra("partenza", intent.getStringExtra("daquando"));
                    intentToFire.putExtra("arrivo", intent.getStringExtra("aquando"));
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToFire, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
                }else if(intent.getAction().equals("one_week_before")){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.logo_pani_piccolo)
                            .setLargeIcon(bitmap)
                            .setContentTitle("Let's go")
                            .setContentText(intent.getStringExtra("nomeviaggio"))
                            .setSubText("Manca una sola settimana alla partenza, hai preparato tutto?")
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setAutoCancel(true);
                    Intent intentToFire = new Intent(context, ProfiloViaggiActivity.class);
                    intentToFire.putExtra("notifica", intent.getStringExtra("nomeviaggio"));
                    intentToFire.putExtra("partenza", intent.getStringExtra("daquando"));
                    intentToFire.putExtra("arrivo", intent.getStringExtra("aquando"));
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToFire, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
                }else if(intent.getAction().equals("one_day_after")){
                    NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                            .setSmallIcon(R.drawable.logo_pani_piccolo)
                            .setLargeIcon(bitmap)
                            .setContentTitle("Let's go")
                            .setContentText(intent.getStringExtra("nomeviaggio"))
                            .setSubText("Com'è andato il viaggio? Salva le foto che hai scattato nella galleria del tuo viaggio")
                            .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION))
                            .setAutoCancel(true);
                    Intent intentToFire = new Intent(context, GestioneViaggioGalleriaActivity.class);
                    intentToFire.putExtra("attivita_nomeviaggio", intent.getStringExtra("nomeviaggio"));
                    //intentToFire.putExtra("partenza", intent.getStringExtra("daquando"));
                    //intentToFire.putExtra("arrivo", intent.getStringExtra("aquando"));
                    PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToFire, PendingIntent.FLAG_UPDATE_CURRENT);
                    builder.setContentIntent(pendingIntent);
                    NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
                }
            }
        }
}
