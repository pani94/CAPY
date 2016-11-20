package com.example.ale.myapplicatio;

import android.content.Intent;
import android.net.ParseException;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class CreaIlTuoViaggioActivity extends AppCompatActivity {
    private EditText nomeViaggio;
    private TextView partenza;
    private TextView arrivo;
    private Button bottone_partenza;
    private Button bottone_arrivo;
    private Button bottone_fatto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_il_tuo_viaggio);
        ButtonListener buttonListener = new ButtonListener();
        nomeViaggio = (EditText) findViewById(R.id.editTextNomeViaggio);
        partenza = (TextView) findViewById(R.id.partenza);
        arrivo = (TextView) findViewById(R.id.arrivo);
        bottone_partenza = (Button) findViewById(R.id.button_partenza);
        bottone_arrivo = (Button) findViewById(R.id.button_arrivo);
        bottone_fatto = (Button) findViewById(R.id.buttonFatto);
        bottone_fatto.setOnClickListener(buttonListener);
        bottone_arrivo.setEnabled(false);
        bottone_arrivo.setOnClickListener(buttonListener);
        bottone_partenza.setOnClickListener(buttonListener);
    }

    public class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.buttonFatto:
                    String NViaggio = nomeViaggio.getText().toString();
                    String p = partenza.getText().toString();
                    String a = arrivo.getText().toString();
                    DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
                    Date part = null;
                    Date arr = null;
                    if(!NViaggio.equals("") && !p.equals("gg/mm/aaaa") && !a.equals("gg/mm/aaaa")){
                        try {
                            part = formatter.parse(p);
                            arr = formatter.parse(a);
                        } catch (java.text.ParseException e) {
                            e.printStackTrace();
                        }
                        if(part != null & arr != null){
                            if(CheckDates(part, arr)){
                                DataBase db = new DataBase(CreaIlTuoViaggioActivity.this);
                                Viaggio viaggio = new Viaggio(NViaggio, p, a);
                                db.insertViaggio(viaggio);
                                salvaGiorni(part, arr, NViaggio);
                                Intent intent = new Intent(CreaIlTuoViaggioActivity.this, ProfiloViaggiActivity.class);
                                intent.putExtra("viaggio_creato","viaggio creato");
                                startActivity(intent);
                                /*int i = p.indexOf("/");
                                String giorno = p.substring(0, i);
                                int day = Integer.parseInt(giorno);
                                String culo = String.valueOf(day);
                                Log.e("giorno", culo);*/
                            }else{
                                Toast.makeText(getApplicationContext(),"Date non valide",Toast.LENGTH_LONG).show();
                            }
                        }
                    }else{
                        Toast.makeText(getApplicationContext(),"Inserisci i dati del viaggio",Toast.LENGTH_LONG).show();
                    }
                    break;

                case R.id.button_partenza:
                    showDatePickerDialog(bottone_partenza, "partenza");
                    break;

                case R.id.button_arrivo:
                    showDatePickerDialog(bottone_arrivo, "arrivo");
                    break;
            }
        }
    }

    public void showDatePickerDialog(View v, String selezione) {
        DialogFragment newFragment = new DatePickerFragment();
        Bundle arg = new Bundle();
        arg.putString("selezionato", selezione);
        newFragment.setArguments(arg);
        newFragment.show(getSupportFragmentManager(), "datePicker");
    }

    public static boolean CheckDates(Date partenza, Date arrivo)    {
        Log.e("messaggini", "checkdates function");
        Calendar c_partenza = Calendar.getInstance();
        Calendar c_arrivo = Calendar.getInstance();
        c_partenza.setTime(partenza);
        c_arrivo.setTime(arrivo);
        boolean b = false;
        try {
            if(c_partenza.before(c_arrivo))
            {
                b = true;
            }
            else if(c_partenza.equals(c_arrivo))
            {
                b = true;//If two dates are equal
            }
            else
            {
                b = false; //If start date is after the end date
            }
        } catch (ParseException e) {
            // TODO Auto-generated catch block
            //e.printStackTrace();
        }
        return b;
    }

    public void salvaGiorni(Date startDate, Date endDate, String NViaggio){
        long different = endDate.getTime() - startDate.getTime();

        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        int elapsedDays = (int) (different / daysInMilli);
        //elapsedDays = elapsedDays+1;
        Log.e("messaggini", String.valueOf(elapsedDays));
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(startDate);
        int day = calendar.get(calendar.DAY_OF_MONTH);
        int month = calendar.get(calendar.MONTH);
        int year = calendar.get(calendar.YEAR);
        String p = day + "/" + (month+1) + "/" + year;
        DataBase db = new DataBase(CreaIlTuoViaggioActivity.this);
        Giorno g = new Giorno(p);
        db.insertGiorno(g);
        long id_viaggio = db.getIdViaggio(NViaggio);
        ViaggioGiorno va = new ViaggioGiorno(id_viaggio, p);
        db.insertViaggioGiorno(va);
        //Log.e("messaggini", p);
        for(int k=0; k<elapsedDays; k++){
            if(month == 0 || month == 2 || month == 4 || month == 6 || month == 7 || month == 9){
                if(day == 31){
                    month = month+1;
                    day = 1;
                }else{
                    day = day+1;
                }
            }else if(month == 1){
                if(day == 28){
                    month = month+1;
                    day = 1;
                }else{
                    day = day+1;
                }
            }else if(month == 11){
                if(day == 31){
                    year = year+1;
                    month = 0;
                    day = 1;
                }else{
                    day = day+1;
                }
            }else{
                if(day == 30){
                    month = month+1;
                    day = 1;
                }else{
                    day = day+1;
                }
            }

            calendar.set(calendar.DAY_OF_MONTH, day);
            calendar.set(calendar.MONTH, month);
            calendar.set(calendar.YEAR, year);

            int day_iesimo = calendar.get(calendar.DAY_OF_MONTH);
            int month_iesimo = calendar.get(calendar.MONTH);
            int year_iesimo = calendar.get(calendar.YEAR);
            String p_iesimo = day_iesimo + "/" + (month_iesimo+1) + "/" + year_iesimo;
            g = new Giorno(p_iesimo);
            db.insertGiorno(g);
            va = new ViaggioGiorno(id_viaggio, p_iesimo);
            db.insertViaggioGiorno(va);
            //Log.e("messaggini", p_iesimo);
        }
    }
}
