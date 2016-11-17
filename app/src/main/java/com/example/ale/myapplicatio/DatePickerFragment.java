package com.example.ale.myapplicatio;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.DatePicker;
import android.widget.TextView;

import java.util.Calendar;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    String selezione;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        // Use the current date as the default date in the picker
        final Calendar c_partenza = Calendar.getInstance();
        int year = c_partenza.get(Calendar.YEAR);
        int month = c_partenza.get(Calendar.MONTH);
        int day = c_partenza.get(Calendar.DAY_OF_MONTH);

        // Create a new instance of DatePickerDialog and return it
        return new DatePickerDialog(getActivity(), this, year, month, day);
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if(getArguments().getString("selezionato").equals("partenza")){
            TextView tv1= (TextView) getActivity().findViewById(R.id.partenza);
            tv1.setText(view.getDayOfMonth() + "/" + (view.getMonth()+1) + "/" + view.getYear());
        }else{
            TextView tv2 = (TextView) getActivity().findViewById(R.id.arrivo);
            tv2.setText(view.getDayOfMonth() + "/" + (view.getMonth()+1) + "/" + view.getYear());
        }

    }
}