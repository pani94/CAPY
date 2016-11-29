package com.example.ale.myapplicatio;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.RingtoneManager;
import android.support.v4.app.NotificationCompat;
import android.support.v4.app.NotificationManagerCompat;

/**
 * Created by ale on 29/11/2016.
 */

public class ReminderBroadcastReceiver extends BroadcastReceiver {
        //private final String SOMEACTION = "com.example.ale.alarm.ACTION";
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction().equals("one_day_before")){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_business_center_white_24dp)
                        .setContentTitle(intent.getStringExtra("nomeviaggio"))
                        .setContentText("Sei pronto per la partenza?")
                        .setSound(RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION));
                Intent intentToFire = new Intent(context, ProfiloViaggiActivity.class);
                intentToFire.putExtra("notifica", intent.getStringExtra("nomeviaggio"));
                intentToFire.putExtra("partenza", intent.getStringExtra("daquando"));
                intentToFire.putExtra("arrivo", intent.getStringExtra("aquando"));
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToFire, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
            }else if(intent.getAction().equals("one_week_before")){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_business_center_white_24dp)
                        .setContentTitle(intent.getStringExtra("nomeviaggio"))
                        .setContentText("Manca una sola settimana alla partenza, hai preparato tutto?");
                Intent intentToFire = new Intent(context, ProfiloViaggiActivity.class);
                intentToFire.putExtra("notifica", intent.getStringExtra("nomeviaggio"));
                intentToFire.putExtra("partenza", intent.getStringExtra("daquando"));
                intentToFire.putExtra("arrivo", intent.getStringExtra("aquando"));
                PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intentToFire, PendingIntent.FLAG_UPDATE_CURRENT);
                builder.setContentIntent(pendingIntent);
                NotificationManagerCompat.from(context).notify((int) System.currentTimeMillis(), builder.build());
            }else if(intent.getAction().equals("one_day_after")){
                NotificationCompat.Builder builder = new NotificationCompat.Builder(context)
                        .setSmallIcon(R.drawable.ic_business_center_white_24dp)
                        .setContentTitle(intent.getStringExtra("nomeviaggio"))
                        .setContentText("Com'è andato il viaggio? Salva le foto che hai scattato nella galleria del tuo viaggio");
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
