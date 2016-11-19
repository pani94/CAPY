package com.example.ale.myapplicatio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;

public class ITuoiPreferitiFragmentListItem extends Fragment {

    private TextView preferiti_titolo;
    private ImageView preferiti_foto;
    private TextView preferiti_orario;
    private TextView preferiti_link;
    private TextView preferiti_telefono;
    private TextView preferiti_indirizzo;
    private Button preferiti_bottoneaggiungi;

    private String preferiti_titolo_get;
    private String preferiti_orario_get;
    private String preferiti_foto_get;
    private String preferiti_link_get;
    private String preferiti_telefono_get;
    private String preferiti_indirizzo_get;

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

        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        Log.e("ciao", "ciao");
        View view = inflater.inflate(R.layout.fragment_ituoi_preferiti_fragment_list_item, container, false);
        preferiti_titolo = (TextView) view.findViewById(R.id.fragment_ituoi_preferiti_fragment_list_item_titolo);
        preferiti_foto = (ImageView) view.findViewById(R.id.fragment_ituoi_preferiti_fragment_list_item_immagine);
        preferiti_orario = (TextView) view.findViewById(R.id.fragment_ituoi_preferiti_fragment_list_item_orario);
        preferiti_link= (TextView) view.findViewById(R.id.fragment_ituoi_preferiti_fragment_list_item_link);
        preferiti_telefono = (TextView) view.findViewById(R.id.fragment_ituoi_preferiti_fragment_list_item_telefono);
        preferiti_indirizzo = (TextView) view.findViewById(R.id.fragment_ituoi_preferiti_fragment_list_item_indirizzo);
        preferiti_bottoneaggiungi = (Button) view.findViewById(R.id.fragment_ituoi_preferiti_fragment_list_item_bottoneaggiungi);
        preferiti_titolo.setText(preferiti_titolo_get);
        preferiti_orario.setText(preferiti_orario_get);
        preferiti_link.setText(preferiti_link_get);
        preferiti_telefono.setText(preferiti_telefono_get);
        preferiti_indirizzo.setText(preferiti_indirizzo_get);

        ButtonListener buttonListener = new ButtonListener();
        preferiti_bottoneaggiungi.setOnClickListener(buttonListener);

        String photo_reference_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=400&photoreference=" + preferiti_foto_get + "&sensor=false&key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
        new LoadImageTask().execute(photo_reference_url);

        return view;
    }

    public class ButtonListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {

        }
    }
    

    public class LoadImageTask extends AsyncTask<String, Void, Bitmap> {




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


            }
        }
    }

}
