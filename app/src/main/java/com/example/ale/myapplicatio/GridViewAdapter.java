package com.example.ale.myapplicatio;

/**
 * Created by ale on 23/11/2016.
 */


import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class GridViewAdapter extends ArrayAdapter<Foto> {

    private Context context;
    private int layoutResourceId;
    private ArrayList<Foto> data = new ArrayList<>();

    public GridViewAdapter(Context context, int layoutResourceId, ArrayList<Foto> data) {
        super(context, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.context = context;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        ViewHolder holder;

        if (row == null) {
            LayoutInflater inflater = ((Activity) context).getLayoutInflater();
            row = inflater.inflate(layoutResourceId, parent, false);
            holder = new ViewHolder();
            holder.image = (ImageView) row.findViewById(R.id.imageGalleryItem);
            row.setTag(holder);
        } else {
            holder = (ViewHolder) row.getTag();
        }

        for(int i = 0; i < data.size(); i++){
            Bitmap bitmap = loadImageFromStorage(data.get(position).getPath());
            if(bitmap != null){
                holder.image.setImageBitmap(bitmap);
            }

        }
        return row;
    }

    static class ViewHolder {
        ImageView image;
    }
    private Bitmap loadImageFromStorage(String path)
    {
        Bitmap b = null;
        try {
            File f=new File(path, "");
            b = BitmapFactory.decodeStream(new FileInputStream(f));

        }
        catch (FileNotFoundException e)
        {
            e.printStackTrace();
        }
        return b;
    }
    /*class OnImageClickListener implements View.OnClickListener {

        int _postion;

        // constructor
        public OnImageClickListener(int position) {
            this._postion = position;
        }

        @Override
        public void onClick(View v) {
            // on selecting grid view image
            // launch full screen activity
            Intent i = new Intent(context, FullScreenViewActivity.class);
            i.putExtra("position", _postion);
            context.startActivity(i);
        }

    }*/

}
