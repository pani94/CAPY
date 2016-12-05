package com.example.ale.myapplicatio;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class DatePickerFragment extends DialogFragment
        implements DatePickerDialog.OnDateSetListener {
    String selezione;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        if(getArguments().getString("selezionato").equals("partenza")) {
            // Use the current date as the default date in the picker
            final Calendar c_partenza = Calendar.getInstance();
            int year = c_partenza.get(Calendar.YEAR);
            int month = c_partenza.get(Calendar.MONTH);
            int day = c_partenza.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), R.style.DialogTheme, this, year, month, day);
        }else{
            final String data_partenza = getArguments().getString("data");
            DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
            Date p = null;
            try {
                p = formatter.parse(data_partenza);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            final Calendar c_arrivo = Calendar.getInstance();
            c_arrivo.setTime(p);
            int year = c_arrivo.get(Calendar.YEAR);
            int month = c_arrivo.get(Calendar.MONTH);
            int day = c_arrivo.get(Calendar.DAY_OF_MONTH);
            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), R.style.DialogTheme, this, year, month, day);
        }
    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        if(getArguments().getString("selezionato").equals("partenza")){
            TextView tv1= (TextView) getActivity().findViewById(R.id.partenza);
            tv1.setText(view.getDayOfMonth() + "/" + (view.getMonth()+1) + "/" + view.getYear());
            Button b = (Button) getActivity().findViewById(R.id.button_arrivo);
            b.setBackgroundResource(R.drawable.bottone_crea_viaggio);
            b.setEnabled(true);
        }else{
            TextView tv2 = (TextView) getActivity().findViewById(R.id.arrivo);
            tv2.setText(view.getDayOfMonth() + "/" + (view.getMonth()+1) + "/" + view.getYear());
        }

    }
}