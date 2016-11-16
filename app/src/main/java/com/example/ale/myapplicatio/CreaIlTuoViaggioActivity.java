package com.example.ale.myapplicatio;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class CreaIlTuoViaggioActivity extends AppCompatActivity {
    private EditText nomeViaggio;
    private EditText partenza;
    private EditText arrivo;
    private Button fatto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_crea_il_tuo_viaggio);
        ButtonListener buttonListener = new ButtonListener();
        nomeViaggio = (EditText) findViewById(R.id.editTextNomeViaggio);
        partenza = (EditText) findViewById(R.id.editTextPartenza);
        arrivo = (EditText) findViewById(R.id.editTextArrivo);
        fatto = (Button) findViewById(R.id.buttonFatto);
        fatto.setOnClickListener(buttonListener);
    }

    public class ButtonListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
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


        }


    }
}
