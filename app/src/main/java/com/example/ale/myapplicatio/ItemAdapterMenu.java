package com.example.ale.myapplicatio;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;

public class ItemAdapterMenu extends ArrayAdapter<ItemRicercaActivity>{
    private ArrayList<ItemRicercaActivity> arrayList;
    private ImageView icon;

    public ItemAdapterMenu(Context context, ArrayList<ItemRicercaActivity> Items) {
        super(context, 0, Items);
        arrayList = Items;
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        ItemRicercaActivity item = arrayList.get(position);
        // Check if an existing view is being reused, otherwise inflate the view
        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.list_ricerca_item, parent, false);
            ViewHolder viewHolder = new ViewHolder();
            viewHolder.name = (TextView) convertView.findViewById(R.id.name);
            viewHolder.vicinity = (TextView) convertView.findViewById(R.id.vicinity);
            viewHolder.icon = (ImageView) convertView.findViewById(R.id.icon);
            convertView.setTag(viewHolder);
        }
        ViewHolder holder = (ViewHolder) convertView.getTag();
        String nome = item.getName();
        String indirizzo = item.getVicinity();
        holder.name.setText(nome);
        holder.vicinity.setText(indirizzo);
        //String key = "key=AIzaSyDg0CUi5HwJsPRxlrR_8VFBxng3eY2aMXk";
        //String key ="key=AIzaSyBieTKI8Lmg7TuF2MgUUtK93bjpWylxLBM";
        //String key = "key=AIzaSyAD1xAMtZ0YaMSii5iDkTJrFv0jz9cEz2U";
        String key = "key=AIzaSyCG-pKhY5jLgcDTJZSaTUd3ufgvtcJ9NwQ";
        String photo_reference_url = "https://maps.googleapis.com/maps/api/place/photo?maxwidth=200&maxheight=200&photoreference="+item.getPhoto_reference()+"&sensor=false&" + key;
        holder.icon.setTag(photo_reference_url);
        //if(photo_reference_url != holder.icon.getTag()){
           new ImageDownloaderTask(holder.icon).execute(photo_reference_url);
        //Log.e("url", photo_reference_url);
        //}



        //holder.icon.setImageResource(R.drawable.no);
        return convertView;
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
                icon.setImageBitmap(bitmap);
            }
        }
    }

    public class ViewHolder{
        TextView name;
        TextView vicinity;
        ImageView icon;
    }

}