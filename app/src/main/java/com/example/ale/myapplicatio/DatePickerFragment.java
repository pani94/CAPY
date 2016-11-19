package com.example.ale.myapplicatio;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.widget.Button;
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
            //int giornopartenza = view.getDayOfMonth();
            //int mesepartenza = view.getMonth()+1;
            //int annopartenza = view.getYear();
            TextView tv1= (TextView) getActivity().findViewById(R.id.partenza);
            tv1.setText(view.getDayOfMonth() + "/" + (view.getMonth()+1) + "/" + view.getYear());
            Button b = (Button) getActivity().findViewById(R.id.button_arrivo);
            b.setEnabled(true);
        }else{
            //int giornoarrivo = view.getDayOfMonth();
            //int mesearrivo = view.getMonth()+1;
            //int annoarrivo = view.getYear();
            TextView tv2 = (TextView) getActivity().findViewById(R.id.arrivo);
            tv2.setText(view.getDayOfMonth() + "/" + (view.getMonth()+1) + "/" + view.getYear());
        }

    }
}