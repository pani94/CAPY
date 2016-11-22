package com.example.ale.myapplicatio;

import android.content.Context;
import android.graphics.Typeface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ale on 22/11/2016.
 */

public class ExpandableListViewAttivitaGiornoAdapter extends BaseExpandableListAdapter {
   private LayoutInflater inflater;
    private List <String> header_titles;
    private Context context;
    private ArrayList<ArrayList <AttivitaGiorno> >attivitaGiornos ;
    public ExpandableListViewAttivitaGiornoAdapter(Context context,List<String> header_titles,ArrayList<ArrayList <AttivitaGiorno> > attivitaGiornos){
        this.context = context;
        this.header_titles = header_titles;
        this.attivitaGiornos = attivitaGiornos;
    }
    @Override
    public int getGroupCount() {
        return header_titles.size();
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        return attivitaGiornos.get(groupPosition).size();
        //FARE SWITCH A SECONDA DEL GROUP POSITION
    }

    @Override
    public Object getGroup(int groupPosition) {
        return header_titles.get(groupPosition);
    }

    @Override
    public AttivitaGiorno getChild(int groupPosition, int childPosition) {
        return attivitaGiornos.get(groupPosition).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) this.getGroup(groupPosition);
        if(convertView == null){

            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_exp_listview,null);
        }
        TextView textView = (TextView) convertView.findViewById(R.id.item_exp_listview_head);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(title);
        return convertView;

    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        DataBase dataBase = new DataBase(context);
        //Log.e("GCV",getChild(groupPosition,childPosition).);
        String title =  dataBase.getAttivita(getChild(groupPosition,childPosition).getPlace_id()).getNome();
        if(convertView == null){
            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_exp_listview_child,null);

        }
        TextView textView = (TextView) convertView.findViewById(R.id.child_item);
        textView.setText(title);
        return convertView;

    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
