package com.example.ale.myapplicatio;

import android.Manifest;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.provider.CalendarContract;
import android.support.v4.app.ActivityCompat;
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

    public long addViaggioToCalendar(Date partenza, Date arrivo, String nome_viaggio, boolean needReminder, Activity Activity) {
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
                        CalendarContract.Calendars.CALENDAR_COLOR,
                        0xffff0000);
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
            Log.e("ciao", arrivo.toString());
            beginTime.setTime(partenza);
            endTime.setTime(arrivo);
            startMillis = beginTime.getTimeInMillis();
            endMillis = endTime.getTimeInMillis();
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
            if (needReminder) {

                String reminderUriString = "content://com.android.calendar/reminders";
                ContentValues reminderValues = new ContentValues();
                reminderValues.put("event_id", eventID);
                reminderValues.put("minutes", 5); // Default value of the
                // system. Minutes is a integer
                reminderValues.put("method", 1); // Alert Methods: Default(0),
                // Alert(1), Email(2),SMS(3)

                Uri reminderUri = Activity.getApplicationContext()
                        .getContentResolver()
                        .insert(Uri.parse(reminderUriString), reminderValues);
            }


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
}
