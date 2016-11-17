package com.example.ale.myapplicatio;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
                    if(!NViaggio.equals("") && !p.equals("") && !a.equals("")){
                        DataBase db = new DataBase(CreaIlTuoViaggioActivity.this);
                        Viaggio viaggio = new Viaggio(NViaggio, p, a);
                        db.insertViaggio(viaggio);
                        //Intent intent = new Intent(CreaIlTuoViaggioActivity.this, RicercaActivity.class);
                        //startActivity(intent);
                    }
                    else{
                        Toast.makeText(getApplicationContext(),"Inserisci i dati del viaggio",Toast.LENGTH_LONG).show();
                    }
                    Intent intent = new Intent(CreaIlTuoViaggioActivity.this, ProfiloViaggiActivity.class);
                    intent.putExtra("viaggio_creato","viaggio creato");
                    startActivity(intent);
                    break;

                case R.id.button_partenza:
                    showDatePickerDialog(bottone_partenza, "partenza");
                    break;

                case R.id.button_arrivo:
                    showDatePickerDialog(bottone_arrivo, "arrivo");
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


}
