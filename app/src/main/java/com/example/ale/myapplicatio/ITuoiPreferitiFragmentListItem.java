package com.example.ale.myapplicatio;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ITuoiPreferitiFragmentListItem extends Fragment {

    private TextView preferiti_titolo;
    private ImageView preferiti_foto;
    private TextView preferiti_orario;
    private TextView preferiti_link;
    private TextView preferiti_telefono;
    private TextView preferiti_indirizzo;
    private ImageButton preferiti_bottoneaggiungi;

    private String preferiti_titolo_get;
    private String preferiti_orario_get;
    private String preferiti_foto_get;
    private String preferiti_link_get;
    private String preferiti_telefono_get;
    private String preferiti_indirizzo_get;
    private String preferiti_placeid_get;
    private String preferiti_latitudine_get;
    private String preferiti_longitudine_get;
    private ArrayList<Viaggio> arrayListViaggi;
    private String nomeViaggio;
    private long id;
    DataBase database;
    public ITuoiPreferitiFragmentListItem() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            preferiti_titolo_get = getArguments().getString("preferiti_titolo");
            preferiti_orario_get = getArguments().getString("preferiti_orario");
            preferiti_foto_get = getArguments().getString("preferiti_foto");
            preferiti_link_get = getArguments().getString("preferiti_link");
            preferiti_telefono_get = getArguments().getString("preferiti_telefono");
            preferiti_indirizzo_get = getArguments().getString("preferiti_indirizzo");
            preferiti_placeid_get = getArguments().getString("preferiti_placeid");
            preferiti_latitudine_get = getArguments().getString("preferiti_latitudine");
            preferiti_longitudine_get = getArguments().getString("preferiti_longitudine");

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("ciao", "ciao");
        View view = inflater.inflate(R.layout.fragment_ituoi_preferiti_fragment_list_item, container, false);
        preferiti_titolo = (TextView) view.findViewById(R.id.fragment_ituoi_preferiti_fragment_list_item_titolo);
        preferiti_foto = (ImageView) view.findViewById(R.id.fragment_ituoi_preferiti_fragment_list_item_immagine);
        preferiti_orario = (TextView) view.findViewById(R.id.fragment_ituoi_preferiti_list_item_orario);
        preferiti_link= (TextView) view.findViewById(R.id.fragment_ituoi_preferiti_fragment_list_item_link);
        preferiti_telefono = (TextView) view.findViewById(R.id.fragment_ituoi_preferiti_fragment_list_item_telefono);
        preferiti_indirizzo = (TextView) view.findViewById(R.id.fragment_ituoi_preferiti_fragment_list_item_indirizzo);
        preferiti_bottoneaggiungi = (ImageButton) view.findViewById(R.id.fragment_ituoi_preferiti_fragment_list_item_bottoneaggiungi);
        preferiti_titolo.setText(preferiti_titolo_get);
        preferiti_orario.setText(preferiti_orario_get);
        preferiti_link.setText(preferiti_link_get);
        preferiti_telefono.setText(preferiti_telefono_get);
        preferiti_indirizzo.setText(preferiti_indirizzo_get);

        ButtonListener buttonListener = new ButtonListener();
        database = new DataBase(getActivity());
        arrayListViaggi = database.getViaggi();
        preferiti_bottoneaggiungi.setOnClickListener(buttonListener);

        preferiti_titolo.setTypeface(Typeface.DEFAULT_BOLD);
        preferiti_titolo.setShadowLayer(5,0,0, Color.BLACK);

        String photo_reference_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + preferiti_foto_get + "&sensor=false&key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
        new LoadImageTask().execute(photo_reference_url);

        return view;
    }

    public class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            if(database.getViaggiBool()) {
                Attivita attivita = new Attivita(preferiti_placeid_get, preferiti_titolo_get, preferiti_indirizzo_get, preferiti_orario_get, preferiti_telefono_get, preferiti_link_get, "true", preferiti_foto_get, "false",preferiti_latitudine_get,preferiti_longitudine_get);
                database.insertAttivita(attivita);
                String[] nomeViaggi = new String[arrayListViaggi.size()];
                for (int k = 0; k < arrayListViaggi.size(); k++) {
                    nomeViaggi[k] = arrayListViaggi.get(k).getNome_viaggio();
                }
                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("I TUOI VIAGGI");
                builder.setSingleChoiceItems(nomeViaggi, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        nomeViaggio = arrayListViaggi.get(i).getNome_viaggio();
                        id = arrayListViaggi.get(i).getId_viaggio();
                    }

                });
                builder.setPositiveButton("Confirm", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        ViaggioAttivita viaggioattivita = new ViaggioAttivita(id, preferiti_placeid_get);
                        long insert =database.insertViaggioAttivita(viaggioattivita);
                        if(insert > 0){
                            Toast.makeText(getActivity().getApplicationContext(),
                                    "L'attività è stata aggiunta al viaggio",
                                    Toast.LENGTH_SHORT).show();
                        }


                    }
                });
                builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });
                builder.show();
            }else{
                Toast.makeText(getActivity().getApplicationContext(),
                        "Non hai viaggi",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }
    

    public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {
        ProgressDialog pd;
        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            pd = ProgressDialog.show(getActivity(), "", "Caricamento in corso...", true, false);
        }



        @Override
        protected Bitmap doInBackground(String... args) {

            try {

                return BitmapFactory.decodeStream((InputStream)new URL(args[0]).getContent());

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {

            if (bitmap != null) {
                preferiti_foto.setImageBitmap(bitmap);
                pd.dismiss();

            }
        }
    }

}
