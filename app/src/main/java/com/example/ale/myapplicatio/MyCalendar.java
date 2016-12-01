package com.example.ale.myapplicatio;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import java.util.Calendar;
import java.util.Date;

/**
 * Created by ale on 28/11/2016.
 */
public class MyCalendar {
    Activity activity;
    public MyCalendar(Activity activity) {
        this.activity = activity;
    }

    public long addViaggioToCalendar(Date partenza, Date arrivo, String nome_viaggio, Activity Activity) {
        long calId = getCalendarId("My calendar");
        if (calId == -1){
            calId = getCalendarId("my applicatio calendar");
            if(calId == -1){
                ContentValues values = new ContentValues();
                values.put(
                        CalendarContract.Calendars.ACCOUNT_NAME, "my applicatio calendar");
                values.put(
                        CalendarContract.Calendars.ACCOUNT_TYPE,
                        CalendarContract.ACCOUNT_TYPE_LOCAL);
                values.put(
                        CalendarContract.Calendars.NAME,
                        "my applicatio calendar");
                values.put(
                        CalendarContract.Calendars.CALENDAR_DISPLAY_NAME,
                        "my applicatio calendar");
                values.put(
                        CalendarContract.Calendars.CALENDAR_COLOR, Color.parseColor("#E51B37"));
                values.put(
                        CalendarContract.Calendars.CALENDAR_ACCESS_LEVEL,
                        CalendarContract.Calendars.CAL_ACCESS_OWNER);
                values.put(
                        CalendarContract.Calendars.SYNC_EVENTS,
                        1);
                Uri.Builder builder =
                        CalendarContract.Calendars.CONTENT_URI.buildUpon();
                builder.appendQueryParameter(
                        CalendarContract.Calendars.ACCOUNT_NAME,
                        "com.MyApplicatio");
                builder.appendQueryParameter(
                        CalendarContract.Calendars.ACCOUNT_TYPE,
                        CalendarContract.ACCOUNT_TYPE_LOCAL);
                builder.appendQueryParameter(
                        CalendarContract.CALLER_IS_SYNCADAPTER,
                        "true");
                Uri uri = activity.getContentResolver().insert(builder.build(), values);
                calId = getCalendarId("my applicatio calendar");
            }

        }
        long eventID = -1;
        try{
            long startMillis = 0;
            long endMillis = 0;
            Calendar beginTime = Calendar.getInstance();
            Calendar endTime = Calendar.getInstance();
            beginTime.setTime(partenza);
            endTime.setTime(arrivo);
            startMillis = beginTime.getTimeInMillis();
            endMillis = endTime.getTimeInMillis() + 86400000;
            ContentResolver cr = activity.getContentResolver();
            ContentValues values = new ContentValues();
            values.put(CalendarContract.Events.DTSTART, startMillis);
            values.put(CalendarContract.Events.DTEND, endMillis);
            values.put(CalendarContract.Events.TITLE,nome_viaggio);
            values.put(CalendarContract.Events.CALENDAR_ID, calId);
            values.put(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Rome");
            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            }
            Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);
            eventID = Long.parseLong(uri.getLastPathSegment());


        }catch (Exception ex) {
            // log.error("Error in adding event on calendar" + ex.getMessage());
        }

        return eventID;


    }

    private long getCalendarId(String nome) {
        String[] projection = new String[]{CalendarContract.Calendars._ID};
        String selection =
                CalendarContract.Calendars.ACCOUNT_NAME +
                        " = ? " ;
        // use the same values as above:
        String[] selArgs =
                new String[]{ nome};
        if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {

        }
        Cursor cursor = activity.getContentResolver().
                        query(
                                CalendarContract.Calendars.CONTENT_URI,
                                projection,
                                selection,
                                selArgs,
                                null);
        if (cursor.moveToFirst()) {
            return cursor.getLong(0);
        }
        return -1;
    }

    public void addNotify(Context ctx, Date partenza, Date arrivo, String NomeViaggio, String action){
        Intent intent = new Intent(ctx, ReminderBroadcastReceiver.class);
        intent.setAction(action);
        intent.putExtra("nomeviaggio", NomeViaggio);
        Calendar calendar_partenza = Calendar.getInstance();
        calendar_partenza.setTime(partenza);
        int day_partenza = calendar_partenza.get(Calendar.DAY_OF_MONTH);
        int month_partenza = calendar_partenza.get(Calendar.MONTH);
        int year_partenza = calendar_partenza.get(Calendar.YEAR);
        String string_partenza = day_partenza + "/" + (month_partenza + 1) + "/" + year_partenza;
        intent.putExtra("daquando", string_partenza);
        Calendar calendar_arrivo = Calendar.getInstance();
        calendar_arrivo.setTime(arrivo);
        int day_arrivo = calendar_arrivo.get(Calendar.DAY_OF_MONTH);
        int month_arrivo = calendar_arrivo.get(Calendar.MONTH);
        int year_arrivo = calendar_arrivo.get(Calendar.YEAR);
        String string_arrivo = day_arrivo + "/" + (month_arrivo + 1) + "/" + year_arrivo;
        intent.putExtra("aquando", string_arrivo);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(ctx, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager am = (AlarmManager) ctx.getSystemService(Activity.ALARM_SERVICE);

        if(action.equals("one_day_before")){
            long timeMsOneDayBefore = partenza.getTime() - 86400000 + 43200000;
            if (Build.VERSION.SDK_INT < 19) {
                am.set(AlarmManager.RTC_WAKEUP, timeMsOneDayBefore, pendingIntent);
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, timeMsOneDayBefore, pendingIntent);
            }
        }else if(action.equals("one_week_before")){
            long timeMsOneWeekBefore = partenza.getTime() - 604800000 + 43200000;
            if (Build.VERSION.SDK_INT < 19) {
                am.set(AlarmManager.RTC_WAKEUP, timeMsOneWeekBefore, pendingIntent);
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, timeMsOneWeekBefore, pendingIntent);
            }
        }else if(action.equals("one_day_after")){
            long timeMsOneDayAfter = arrivo.getTime() + 43200000;
            if (Build.VERSION.SDK_INT < 19) {
                am.set(AlarmManager.RTC_WAKEUP, timeMsOneDayAfter, pendingIntent);
            } else {
                am.setExact(AlarmManager.RTC_WAKEUP, timeMsOneDayAfter, pendingIntent);
            }
        }

    }

    public void deleteEvent(long event_id, Context context) {
        String[] selArgs =
                new String[]{Long.toString(event_id)};
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.WRITE_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        int deleted = context.getContentResolver().delete(CalendarContract.Events.CONTENT_URI,CalendarContract.Events._ID + " =? ", selArgs);
    }
}
