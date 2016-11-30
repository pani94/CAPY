package com.example.ale.myapplicatio;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ale on 22/11/2016.
 */

public class ExpandableListViewAttivitaGiornoAdapter extends BaseExpandableListAdapter {
   private LayoutInflater inflater;
    private List <String> header_titles;
    private Context context;
    private int id_viaggio;
    private String data;
    private ArrayList<ArrayList <AttivitaGiorno> >attivitaGiornos ;
    private TextView textView;
    private ImageButton bottone_aggiungi;
    private ImageView freccia;
    public ExpandableListViewAttivitaGiornoAdapter(Context context,List<String> header_titles,ArrayList<ArrayList <AttivitaGiorno> > attivitaGiornos,int id_viaggio,String data){
        this.context = context;
        this.header_titles = header_titles;
        this.attivitaGiornos = attivitaGiornos;
        this.id_viaggio = id_viaggio;
        this.data = data;
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
    public View getGroupView(final int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        String title = (String) this.getGroup(groupPosition);
        if(convertView == null){

            LayoutInflater inflater = (LayoutInflater) this.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = inflater.inflate(R.layout.item_exp_listview,null);
        }
        final DataBase db = new DataBase(context);
        textView = (TextView) convertView.findViewById(R.id.item_exp_listview_head);
        bottone_aggiungi = (ImageButton) convertView.findViewById(R.id.item_exp_listview_bottone);
        freccia =(ImageView) convertView.findViewById(R.id.item_exp_listview_frecciagiu);
        if(getChildrenCount(groupPosition)>0){
            freccia.setVisibility(View.VISIBLE);
        }else{
            freccia.setVisibility(View.INVISIBLE);
        }

        bottone_aggiungi.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                final ArrayList<ViaggioAttivita> viaggioAttivitas = db.getViaggiAttivita(id_viaggio);
                ArrayList<Attivita> attivitas = new ArrayList<Attivita>();
                final ArrayList <Integer> selectedItems = new ArrayList<Integer>();
                View parentRow = (View) v.getParentForAccessibility();
                final ExpandableListView expandableListView = (ExpandableListView) parentRow.getParentForAccessibility();

                String [] nomi_attivita ;
                boolean [] booleen ;
                int count ;
                if (groupPosition == 1 || groupPosition==3){
                    count = db.getNumeroAttivita(viaggioAttivitas,"mangiare");
                    attivitas = db.getAttivita(id_viaggio,"mangiare");
                    nomi_attivita = new String[count];
                    booleen = new boolean[count];
                    for (int j = 0; j < count; j++){
                        nomi_attivita [j] = db.getAttivita(attivitas.get(j).getPlace_id()).getNome();
                        booleen [j] = false;
                    }
                }
                else if(groupPosition == 5){
                    count = db.getNumeroAttivita(viaggioAttivitas,"dormire");
                    attivitas = db.getAttivita(id_viaggio,"dormire");
                    nomi_attivita = new String[count];
                    booleen = new boolean[count];
                    for (int j = 0; j < count; j++){
                        nomi_attivita [j] = db.getAttivita(attivitas.get(j).getPlace_id()).getNome();
                        booleen [j] = false;
                    }
                }
                else{
                    count = db.getNumeroAttivita(viaggioAttivitas,"vedere");
                    attivitas = db.getAttivita(id_viaggio,"vedere");
                    nomi_attivita = new String[count];
                    booleen = new boolean[count];
                    for (int j = 0; j < count; j++){
                        nomi_attivita [j] = db.getAttivita(attivitas.get(j).getPlace_id()).getNome();
                        booleen [j] = false;
                    }
                }
                if(count == 0){
                    Toast.makeText(context, "NON CI SONO ATTIVITA' COLLEGATO A QUESTO VIAGGIO",
                            Toast.LENGTH_SHORT).show();
                }else{
                    final ArrayList<Attivita> attivitasFinal =attivitas;
                    final AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Quale attività vuoi inserire?");
                    builder.setMultiChoiceItems(nomi_attivita, booleen, new DialogInterface.OnMultiChoiceClickListener () {
                        @Override
                        public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                            if(isChecked){
                                selectedItems.add(which);
                            }else if(selectedItems.contains(which))
                            {
                                selectedItems.remove(Integer.valueOf(which));
                            }

                        }
                    });
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            if (selectedItems.size() == 0){
                                dialog.dismiss();
                                Toast.makeText(context, "DEVI SELEZIONARE ALMENO UN'ATTIVITA'",
                                        Toast.LENGTH_LONG).show();
                            }
                            else{
                                String[] giornata = {"Mattina", "Pranzo", "Pomeriggio", "Cena", "Sera", "Notte"};
                                long insert = 0;
                                for (int i = 0; i < selectedItems.size();i++){
                                    // DEVO CREARE L'EVENTO E PRENDERE L'ID
                                    int evento_id = 0;
                                    AttivitaGiorno attivitaGiorno = new AttivitaGiorno(attivitasFinal.get(selectedItems.get(i)).getPlace_id(),data,id_viaggio, giornata[groupPosition],evento_id);
                                    insert = db.insertAttivitaGiorno(attivitaGiorno);
                                }
                                attivitaGiornos.remove(groupPosition);
                                attivitaGiornos.add(groupPosition,db.getAttivitaGiorno(data,id_viaggio,giornata[groupPosition]));
                                ExpandableListViewAttivitaGiornoAdapter adapter = new ExpandableListViewAttivitaGiornoAdapter(context,header_titles,attivitaGiornos,id_viaggio,data);
                                expandableListView.setAdapter(adapter);
                            }

                        }
                        // PRENDERE LE ATTIVITA SELEZIONATE ED ANDARLE AD AGGIUNGERE AL GIORNO CHE MI SONO FATTO PASSARE PRIMA E CON L'ORA CHE OTTENGO DAL GROUP POSITION


                    });
                    builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener()  {
                        public void onClick(DialogInterface dialog, int which)  {

                        }
                    });
                    builder.show();
                }



            }

        });
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
