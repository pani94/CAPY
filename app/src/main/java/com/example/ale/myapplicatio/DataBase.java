package com.example.ale.myapplicatio;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import java.util.ArrayList;
import java.util.Collections;

public class DataBase {

    private static final String DB_NAME = "database.db";
    private static final int DB_VERSION = 1;

    private static final String VIAGGIO_TABLE = "viaggio";
    private static final String VIAGGIO_ID = "id_viaggio";
    private static final int VIAGGIO_ID_COL = 0;
    private static final String VIAGGIO_NOME = "nome_viaggio";
    private static final int VIAGGIO_NOME_COL = 1;
    private static final String VIAGGIO_PARTENZA = "partenza";
    private static final int VIAGGIO_PARTENZA_COL = 2;
    private static final String VIAGGIO_ARRIVO = "arrivo";
    private static final int VIAGGIO_ARRIVO_COL = 3;

    private static final String ATTIVITA_TABLE = "attivita";
    private static final String ATTIVITA_PLACE_ID = "place_id";
    private static final int ATTIVITA_PLACE_ID_COL = 0;
    private static final String ATTIVITA_NOME = "nome";
    private static final int ATTIVITA_NOME_COL = 1;
    private static final String ATTIVITA_INDIRIZZO = "indirizzo";
    private static final int ATTIVITA_INDIRIZZO_COL = 2;
    private static final String ATTIVITA_ORARIO = "orario";
    private static final int ATTIVITA_ORARIO_COL = 3;
    private static final String ATTIVITA_TELEFONO = "telefono";
    private static final int ATTIVITA_TELEFONO_COL = 4;
    private static final String ATTIVITA_LINK = "link";
    private static final int ATTIVITA_LINK_COL = 5;
    private static final String ATTIVITA_TIPOLOGIA = "tipologia";
    private static final int ATTIVITA_TIPOLOGIA_COL = 6;
    private static final String ATTIVITA_FOTO = "foto";
    private static final int ATTIVITA_FOTO_COL = 7;
    private static final String ATTIVITA_PREFERITO = "preferito";
    private static final int ATTIVITA_PREFERITO_COL = 8;

    private static final String GIORNO_TABLE = "giorno";
    private static final String GIORNO_DATA = "data";
    private static final int GIORNO_DATA_COL = 0;

    private static final String VIAGGIOATTIVITA_TABLE = "viaggioAttivita";
    private static final String VIAGGIOATTIVITA_ID_VIAGGIO = "id_viaggio";
    private static final int VIAGGIOATTIVITA_ID_VIAGGIO_COL = 0;
    private static final String VIAGGIOATTIVITA_PLACE_ID = "place_id";
    private static final int VIAGGIOATTIVITA_PLACE_ID_COL = 1;

    private static final String VIAGGIOGIORNO_TABLE = "viaggioGiorno";
    private static final String VIAGGIOGIORNO_ID_VIAGGIO = "id_viaggio";
    private static final int VIAGGIOGIORNO_ID_VIAGGIO_COL = 0;
    private static final String VIAGGIOGIORNO_DATA = "data";
    private static final int VIAGGIOGIORNO_DATA_COL = 1;

    private static final String ATTIVITAGIORNO_TABLE = "attivitaGiorno";
    private static final String ATTIVITAGIORNO_ID_VIAGGIO = "id_viaggio";
    private static final int ATTIVITAGIORNO_ID_VIAGGIO_COL = 2;
    private static final String ATTIVITAGIORNO_PLACE_ID = "place_id";
    private static final int ATTIVITAGIORNO_PLACE_ID_COL = 0;
    private static final String ATTIVITAGIORNO_DATA = "data";
    private static final int ATTIVITAGIORNO_DATA_COL = 1;
    private static final String ATTIVITAGIORNO_QUANDO = "quando";
    private static final int ATTIVITAGIORNO_QUANDO_COL = 3;
    private static final String ATTIVITAGIORNO_EVENTO_ID = "evento_id";
    private static final int ATTIVITAGIORNO_EVENTO_ID_COL = 4;

    private static final String FOTO_TABLE = "foto";
    private static final String FOTO_ID_VIAGGIO = "id_viaggio";
    private static final int  FOTO_ID_VIAGGIO_COL = 0;
    private static final String FOTO_PATH = "path";
    private static final int FOTO_PATH_COL = 1;


    private static final String CREATE_VIAGGIO_TABLE =
            "CREATE TABLE " + VIAGGIO_TABLE + " (" +
                    VIAGGIO_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    VIAGGIO_NOME + " TEXT NOT NULL UNIQUE, " +
                    VIAGGIO_PARTENZA + " TEXT NOT NULL, " +
                    VIAGGIO_ARRIVO + " TEXT NOT NULL);";
    private static final String DROP_VIAGGIO_TABLE =
            "DROP TABLE IF EXISTS " + VIAGGIO_TABLE;

    private static final String CREATE_ATTIVITA_TABLE =
            "CREATE TABLE " + ATTIVITA_TABLE + " (" +
                    ATTIVITA_PLACE_ID + " TEXT PRIMARY KEY, " +
                    ATTIVITA_NOME + " TEXT NOT NULL, " +
                    ATTIVITA_INDIRIZZO + " TEXT, " +
                    ATTIVITA_ORARIO + " TEXT, " +
                    ATTIVITA_TELEFONO + " TEXT, " +
                    ATTIVITA_LINK + " TEXT, " +
                    ATTIVITA_TIPOLOGIA + " TEXT, " +
                    ATTIVITA_FOTO + " TEXT, " +
                    ATTIVITA_PREFERITO + " TEXT NOT NULL);";
    public static final String DROP_ATTIVITA_TABLE =
            "DROP TABLE IF EXISTS " + ATTIVITA_TABLE;

    private static final String CREATE_GIORNO_TABLE =
            "CREATE TABLE " + GIORNO_TABLE + " (" +
                    GIORNO_DATA + " TEXT NOT NULL PRIMARY KEY);";

    private static final String CREATE_ATTIVITAGIORNO_TABLE =
            "CREATE TABLE " + ATTIVITAGIORNO_TABLE + " (" +
                    ATTIVITAGIORNO_PLACE_ID + " TEXT NOT NULL, " +
                    ATTIVITAGIORNO_DATA + " TEXT NOT NULL, " +
                    ATTIVITAGIORNO_ID_VIAGGIO + " INTEGER, " +
                    ATTIVITAGIORNO_QUANDO + " TEXT, "  +
                    ATTIVITAGIORNO_EVENTO_ID + " INTEGER, " +
                    "PRIMARY KEY (" + ATTIVITAGIORNO_PLACE_ID + "," + ATTIVITAGIORNO_DATA + "," + ATTIVITAGIORNO_ID_VIAGGIO +  "," + ATTIVITAGIORNO_QUANDO +  "," + ATTIVITAGIORNO_QUANDO+"))" +
                    ";";
    public static final String DROP_ATTIVITAGIORNO_TABLE =
            "DROP TABLE IF EXISTS " + ATTIVITAGIORNO_TABLE;

    private static final String CREATE_VIAGGIOATTIVITA_TABLE =
            "CREATE TABLE " + VIAGGIOATTIVITA_TABLE + " (" +
                    VIAGGIOATTIVITA_ID_VIAGGIO + " INTEGER, " +
                    VIAGGIOATTIVITA_PLACE_ID + " TEXT NOT NULL, " +
                    "PRIMARY KEY (" + VIAGGIOATTIVITA_PLACE_ID + "," + VIAGGIOATTIVITA_ID_VIAGGIO + "));";
    public static final String DROP_VIAGGIOATTIVITA_TABLE =
            "DROP TABLE IF EXISTS " + VIAGGIOATTIVITA_TABLE;

    private static final String CREATE_VIAGGIOGIORNO_TABLE =
            "CREATE TABLE " + VIAGGIOGIORNO_TABLE + " (" +
                    VIAGGIOGIORNO_ID_VIAGGIO + " INTEGER, " +
                    VIAGGIOGIORNO_DATA + " TEXT NOT NULL, " +
                    "PRIMARY KEY (" + VIAGGIOGIORNO_ID_VIAGGIO + "," + VIAGGIOGIORNO_DATA + "));";
    public static final String DROP_VIAGGIOGIORNO_TABLE =
            "DROP TABLE IF EXISTS " + VIAGGIOGIORNO_TABLE;

    private static final String CREATE_FOTO_TABLE =
            "CREATE TABLE " + FOTO_TABLE + " (" +
                    FOTO_ID_VIAGGIO + " INTEGER, " +
                    FOTO_PATH + " TEXT NOT NULL, " +
                    "PRIMARY KEY (" + FOTO_ID_VIAGGIO + "," + FOTO_PATH + "));";
    public static final String DROP_FOTO_TABLE =
            "DROP TABLE IF EXISTS " + FOTO_TABLE;

    private static class DBHelper extends SQLiteOpenHelper {
        private DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(CREATE_VIAGGIO_TABLE);
            db.execSQL(CREATE_ATTIVITA_TABLE);
            db.execSQL(CREATE_GIORNO_TABLE);
            db.execSQL(CREATE_ATTIVITAGIORNO_TABLE);
            db.execSQL(CREATE_VIAGGIOATTIVITA_TABLE);
            db.execSQL(CREATE_VIAGGIOGIORNO_TABLE);
            db.execSQL(CREATE_FOTO_TABLE);
        }

        @Override
        public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

        }

    }
    private SQLiteDatabase db;
    private DBHelper dbHelper;


    public DataBase (Context context){
        dbHelper = new DBHelper(context, DB_NAME, null, DB_VERSION);
    }
    private void openReadableDB() {
        db = dbHelper.getReadableDatabase();
    }

    private void openWriteableDB() {
        db = dbHelper.getWritableDatabase();
    }

    private void closeDB() {
        if (db != null)
            db.close();
    }

    private void closeCursor(Cursor cursor) {
        if (cursor != null) {
            cursor.close();
        }

    }

    public long insertViaggio(Viaggio viaggio) {
        // CHIEDERE SE è MEGLIO CREARE IL VIAGGIO OPPURE PASSARGLI DIRETTAMENTE I PARAMETRI
        ContentValues cv = new ContentValues();
        //cv.put(VIAGGIO_ID, 0);
        cv.put(VIAGGIO_NOME, viaggio.getNome_viaggio());
        cv.put(VIAGGIO_ARRIVO, viaggio.getArrivo());
        cv.put(VIAGGIO_PARTENZA, viaggio.getPartenza());

        this.openWriteableDB();
        long rowID = db.insert(VIAGGIO_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }
    public ArrayList<Viaggio> getViaggi() {


        this.openReadableDB();
        Cursor cursor = db.query(VIAGGIO_TABLE, null,
                null, null,
                null, null, null);
        ArrayList<Viaggio> viaggi = new ArrayList<Viaggio>();
        while (cursor.moveToNext()) {
            viaggi.add(getViaggioFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return viaggi;
    }
    public int getIdViaggio(String nomeViaggio){
        String where = VIAGGIO_NOME + " = ?" ;
        String [] whereargs = { nomeViaggio};
        this.openReadableDB();
        //new String[] { VIAGGIO_ID , VIAGGIO_NOME , VIAGGIO_PARTENZA , VIAGGIO_ARRIVO}
        Cursor cursor = db.query(VIAGGIO_TABLE,null,where,whereargs,null,null,null);
        cursor.moveToFirst();
        int id_viaggio = cursor.getInt(VIAGGIO_ID_COL);
        cursor.close();
        db.close();
        return id_viaggio;


    }
    private static Viaggio getViaggioFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                Viaggio viaggio = new Viaggio(
                        cursor.getInt(VIAGGIO_ID_COL),
                        cursor.getString(VIAGGIO_NOME_COL),
                        cursor.getString(VIAGGIO_PARTENZA_COL),
                        cursor.getString(VIAGGIO_ARRIVO_COL));
                return viaggio;
            }
            catch(Exception e) {
                return null;
            }
        }
    }
    public boolean getViaggiBool(){
        String where = "1 = 1";
        this.openReadableDB();
        Cursor cursor = db.query(VIAGGIO_TABLE, null, where, null, null, null, null);
        if(cursor != null && cursor.getCount()>0){
            cursor.close();
            this.closeDB();
            return true;
        }
        else
        {
            this.closeDB();
            return false;
        }
    }
    public String getDataPartenza(int id_viaggio){
        String where = VIAGGIO_ID + " = " + id_viaggio;
        this.openReadableDB();
        Cursor cursor = db.query(VIAGGIO_TABLE, null, where, null, null, null, null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getString(VIAGGIO_PARTENZA_COL);
        }else{
            return null;
        }
    }

    public String getDataArrivo(int id_viaggio){
        String where = VIAGGIO_ID + " = " + id_viaggio;
        this.openReadableDB();
        Cursor cursor = db.query(VIAGGIO_TABLE, null, where, null, null, null, null);
        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            return cursor.getString(VIAGGIO_ARRIVO_COL);
        }else{
            return null;
        }
    }

    public long UpdateViaggio(Viaggio viaggio) {
        ContentValues cv = new ContentValues();
        cv.put(VIAGGIO_ID, viaggio.getId_viaggio());
        cv.put(VIAGGIO_NOME, viaggio.getNome_viaggio());
        cv.put(VIAGGIO_PARTENZA, viaggio.getPartenza());
        cv.put(VIAGGIO_ARRIVO, viaggio.getArrivo());
        this.openReadableDB();
        long rowCount = db.replace(VIAGGIO_TABLE, null, cv);
        this.closeDB();
        return rowCount;
    }

    public int deleteViaggio(long id) {
        String where = VIAGGIO_ID + "= ?";
        String[] whereArgs = { String.valueOf(id) };

        this.openWriteableDB();
        int rowCount = db.delete(VIAGGIO_TABLE, where, whereArgs);
        this.closeDB();
        return rowCount;
    }

    public long insertAttivita(Attivita attivita) {
        ContentValues cv = new ContentValues();
        cv.put(ATTIVITA_PLACE_ID, attivita.getPlace_id());
        cv.put(ATTIVITA_NOME, attivita.getNome());
        cv.put(ATTIVITA_INDIRIZZO, attivita.getIndirizzo());
        cv.put(ATTIVITA_ORARIO, attivita.getOrario());
        cv.put(ATTIVITA_TELEFONO, attivita.getTelefono());
        cv.put(ATTIVITA_LINK, attivita.getLink());
        cv.put(ATTIVITA_TIPOLOGIA, attivita.getTipologia());
        cv.put(ATTIVITA_FOTO, attivita.getFoto());
        cv.put(ATTIVITA_PREFERITO, attivita.getPreferito());


        this.openWriteableDB();
        long rowID = db.insert(ATTIVITA_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public void UpdateAttivitaPreferita(Attivita attivita) {
        ContentValues cv = new ContentValues();
        cv.put(ATTIVITA_PLACE_ID, attivita.getPlace_id());
        cv.put(ATTIVITA_NOME, attivita.getNome());
        cv.put(ATTIVITA_INDIRIZZO, attivita.getIndirizzo());
        cv.put(ATTIVITA_ORARIO, attivita.getOrario());
        cv.put(ATTIVITA_TELEFONO, attivita.getTelefono());
        cv.put(ATTIVITA_LINK, attivita.getLink());
        cv.put(ATTIVITA_TIPOLOGIA, attivita.getTipologia());
        cv.put(ATTIVITA_FOTO, attivita.getFoto());
        cv.put(ATTIVITA_PREFERITO, attivita.getPreferito());
        this.openReadableDB();
        db.replace(ATTIVITA_TABLE, null, cv);
        this.closeDB();
    }
    public ArrayList<Attivita> getAttivita(String nomeViaggio,String tipologia) {
        ArrayList<ViaggioAttivita> viaggioAttivitas = getViaggiAttivita(getIdViaggio(nomeViaggio));
        this.openReadableDB();
        Cursor cursor1 = null;
        ArrayList<Attivita> attivitas = new ArrayList<Attivita>();
        for(int i=0; i<viaggioAttivitas.size(); i++){

            String[] whereArgs1;
            if(tipologia.equals("tutte")){
                String where = ATTIVITA_PLACE_ID + " = ? "  ;
                String[] whereArgs = { viaggioAttivitas.get(i).getPlace_id()};
                cursor1 = db.query(ATTIVITA_TABLE, null,
                        where, whereArgs,
                        null, null, null);
            }
            else
            {
                String where = ATTIVITA_PLACE_ID + " = ? " + " AND " + ATTIVITA_TIPOLOGIA + " = ? "  ;
                String[] whereArgs = { viaggioAttivitas.get(i).getPlace_id(),tipologia};
                cursor1 = db.query(ATTIVITA_TABLE, null,
                        where, whereArgs,
                        null, null, null);
            }

            while (cursor1.moveToNext()) {
               attivitas.add(getAttivitaFromCursor(cursor1));
               // Log.e("attivita", attivitas.get(j).getNome());

            }
        }

        if (cursor1 != null)
            cursor1.close();
        //this.closeDB();

        return attivitas;
    }
    public ArrayList<Attivita> getAttivita(int id_viaggio,String tipologia) {
        ArrayList<ViaggioAttivita> viaggioAttivitas = getViaggiAttivita(id_viaggio);
        this.openReadableDB();
        Cursor cursor1 = null;
        ArrayList<Attivita> attivitas = new ArrayList<Attivita>();
        for(int i=0; i<viaggioAttivitas.size(); i++){

            String[] whereArgs1;
            if(tipologia.equals("tutte")){
                String where = ATTIVITA_PLACE_ID + " = ? "  ;
                String[] whereArgs = { viaggioAttivitas.get(i).getPlace_id()};
                cursor1 = db.query(ATTIVITA_TABLE, null,
                        where, whereArgs,
                        null, null, null);
            }
            else
            {
                String where = ATTIVITA_PLACE_ID + " = ? " + " AND " + ATTIVITA_TIPOLOGIA + " = ? "  ;
                String[] whereArgs = { viaggioAttivitas.get(i).getPlace_id(),tipologia};
                cursor1 = db.query(ATTIVITA_TABLE, null,
                        where, whereArgs,
                        null, null, null);
            }

            while (cursor1.moveToNext()) {
                attivitas.add(getAttivitaFromCursor(cursor1));
                // Log.e("attivita", attivitas.get(j).getNome());

            }
        }

        if (cursor1 != null)
            cursor1.close();
        //this.closeDB();

        return attivitas;
    }
    public int getNumeroAttivita(ArrayList<ViaggioAttivita> viaggioAttivitas,String tipologia) {
        this.openReadableDB();
        Cursor cursor1 = null;
        int count = 0;
        for(int i=0; i<viaggioAttivitas.size(); i++) {
            if (tipologia.equals("tutte")) {
                String where = ATTIVITA_PLACE_ID + " = ? ";
                String[] whereArgs = {viaggioAttivitas.get(i).getPlace_id()};
                cursor1 = db.query(ATTIVITA_TABLE, null,
                        where, whereArgs,
                        null, null, null);
            } else {
                String where = ATTIVITA_PLACE_ID + " = ? " + " AND " + ATTIVITA_TIPOLOGIA + " = ? ";
                String[] whereArgs = {viaggioAttivitas.get(i).getPlace_id(), tipologia};
                cursor1 = db.query(ATTIVITA_TABLE, null,
                        where, whereArgs,
                        null, null, null);
            }
            while (cursor1.moveToNext()) {
                count++;
                // Log.e("attivita", attivitas.get(j).getNome());
            }
        }

        if (cursor1 != null)
            cursor1.close();
        this.closeDB();

        return count;
    }
    public Attivita getAttivita(String place_id) {
        this.openReadableDB();
        Cursor cursor1 = null;
       Attivita attivita = null;



                String where = ATTIVITA_PLACE_ID + " = ? "  ;
                String[] whereArgs = { place_id};
                cursor1 = db.query(ATTIVITA_TABLE, null,
                        where, whereArgs,
                        null, null, null);



        cursor1.moveToFirst();
        attivita = getAttivitaFromCursor(cursor1);
        if (cursor1 != null)
            cursor1.close();
        this.closeDB();

        return attivita;
    }
    private static Attivita getAttivitaFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            Log.e("attivita", "è null");
            return null;
        }
        else {
            try {
                Attivita attivita = new Attivita(
                        cursor.getString(ATTIVITA_PLACE_ID_COL),
                        cursor.getString(ATTIVITA_NOME_COL),
                        cursor.getString(ATTIVITA_INDIRIZZO_COL),
                        cursor.getString(ATTIVITA_ORARIO_COL),
                        cursor.getString(ATTIVITA_TELEFONO_COL),
                        cursor.getString(ATTIVITA_LINK_COL),
                        cursor.getString(ATTIVITA_TIPOLOGIA_COL),
                        cursor.getString(ATTIVITA_FOTO_COL),
                        cursor.getString(ATTIVITA_PREFERITO_COL));
                return attivita;
            }
            catch(Exception e) {
                return null;
            }
        }
    }
    public boolean getAttivitaPreferita(String place_id) {
        String where = ATTIVITA_PREFERITO + "= ? AND " + ATTIVITA_PLACE_ID + "= ?" ;
        String[] whereArgs = {"true", place_id};
        this.openReadableDB();
        Cursor cursor = db.query(ATTIVITA_TABLE, null,
                where, whereArgs,
                null, null, null);

        cursor.moveToFirst();


        if (cursor != null && cursor.getCount() > 0){
            cursor.close();
            this.closeDB();
            return true;
        }else{
            this.closeDB();
            return false;
        }
    }
    public ArrayList<Attivita> getAttivitaPreferite() {
        String where = ATTIVITA_PREFERITO + "= ?";
        String[] whereArgs = {"true"};
        this.openReadableDB();
        Cursor cursor = db.query(ATTIVITA_TABLE, null,
                where, whereArgs,
                null, null, null);
        ArrayList<Attivita> attivita_preferite = new ArrayList<Attivita>();
        while (cursor.moveToNext()) {
            attivita_preferite.add(getAttivitaPreferiteFromCursor(cursor));
        }
        if (cursor != null)
            cursor.close();
        this.closeDB();

        return attivita_preferite;
    }
    private static Attivita getAttivitaPreferiteFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                Attivita attivita_preferita = new Attivita(
                        cursor.getString(ATTIVITA_PLACE_ID_COL),
                        cursor.getString(ATTIVITA_NOME_COL),
                        cursor.getString(ATTIVITA_INDIRIZZO_COL),
                        cursor.getString(ATTIVITA_ORARIO_COL),
                        cursor.getString(ATTIVITA_TELEFONO_COL),
                        cursor.getString(ATTIVITA_LINK_COL),
                        cursor.getString(ATTIVITA_TIPOLOGIA_COL),
                        cursor.getString(ATTIVITA_FOTO_COL),
                        cursor.getString(ATTIVITA_PREFERITO_COL));
                return attivita_preferita;
            }
            catch(Exception e) {
                return null;
            }
        }
    }
    public boolean getPreferitiBool(){
        String where = ATTIVITA_PREFERITO + "= ?";
        String[] whereArgs = {"true"};
        this.openReadableDB();
        Cursor cursor = db.query(ATTIVITA_TABLE, null, where, whereArgs, null, null, null);
        if(cursor != null && cursor.getCount()>0){
            cursor.close();
            this.closeDB();

            return true;
        }
        else
        {
            this.closeDB();

            return false;
        }


    }
    public int deletePreferiti(String id){
        String where = ATTIVITA_PLACE_ID + "= ?";
        String[] whereArgs = { id };

        this.openWriteableDB();
        int rowCount = db.delete(ATTIVITA_TABLE, where, whereArgs);
        this.closeDB();

        return rowCount;
    }
    public long insertGiorno(Giorno giorno) {
        ContentValues cv = new ContentValues();
        cv.put(GIORNO_DATA, giorno.getData());
        this.openWriteableDB();
        long rowID = db.insert(GIORNO_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }
    public int deleteGiorno(String data) {
        String where = GIORNO_DATA + "= " + data;
        this.openWriteableDB();
        int rowCount = db.delete(GIORNO_TABLE, where, null);
        this.closeDB();
        return rowCount;
    }

    public long insertViaggioGiorno(ViaggioGiorno viaggioGiorno) {
        ContentValues cv = new ContentValues();
        cv.put(VIAGGIOGIORNO_ID_VIAGGIO, viaggioGiorno.getId_viaggio());
        cv.put(VIAGGIOGIORNO_DATA, viaggioGiorno.getData());
        this.openWriteableDB();
        long rowID = db.insert(VIAGGIOGIORNO_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }
    public int deleteViaggioGiorno(long id, String data) {
        String where = VIAGGIOGIORNO_ID_VIAGGIO + "= ? AND " + VIAGGIOGIORNO_DATA + "= " + data ;
        String[] whereArgs = { String.valueOf(id) };

        this.openWriteableDB();
        int rowCount = db.delete(VIAGGIOGIORNO_TABLE, where, whereArgs);
        this.closeDB();
        return rowCount;
    }
    public int deleteViaggioGiorni(long id) {
        String where = VIAGGIOGIORNO_ID_VIAGGIO + "= ?";
        String[] whereArgs = { String.valueOf(id) };
        this.openWriteableDB();
        int rowCount = db.delete(VIAGGIOGIORNO_TABLE, where, whereArgs);
        this.closeDB();
        return rowCount;
    }

    public ArrayList<ViaggioGiorno> getGiorni(int id) {
        String where = VIAGGIOGIORNO_ID_VIAGGIO + "= ?";
        String[] whereArgs = { Integer.toString(id) };
        this.openReadableDB();
        Cursor cursor = db.query(VIAGGIOGIORNO_TABLE, null,
                where, whereArgs,
                null, null, null);
        ArrayList<ViaggioGiorno> giorni_viaggio = new ArrayList<ViaggioGiorno>();
        while (cursor.moveToNext()) {
            giorni_viaggio.add(getViaggiGiornoFromCursor(cursor));

        }
        if (cursor != null)
            cursor.close();
        this.closeDB();
        Collections.sort(giorni_viaggio);
        return giorni_viaggio;
    }
    private static ViaggioGiorno getViaggiGiornoFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                ViaggioGiorno giorno = new ViaggioGiorno(
                        cursor.getInt(VIAGGIOGIORNO_ID_VIAGGIO_COL),
                        cursor.getString(VIAGGIOGIORNO_DATA_COL));
                return giorno;
            }
            catch(Exception e) {
                return null;
            }
        }
    }
    public int getNumeroDiGiorni(int id){
        String where = VIAGGIOGIORNO_ID_VIAGGIO+ "= ?";
        String[] whereArgs = { Integer.toString(id) };
        this.openReadableDB();
        Cursor cursor = db.query(VIAGGIOGIORNO_TABLE, null,
                where,whereArgs,
                null, null, null);
        if(cursor == null){
            return 0;
        }
       else{
            return cursor.getCount();
        }
    }
    public long insertViaggioAttivita(ViaggioAttivita viaggioAttivita) {
        ContentValues cv = new ContentValues();
        cv.put(VIAGGIOATTIVITA_ID_VIAGGIO, viaggioAttivita.getId_Viaggio());
        cv.put(VIAGGIOATTIVITA_PLACE_ID, viaggioAttivita.getPlace_id());
        this.openWriteableDB();
        long rowID = db.insert(VIAGGIOATTIVITA_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }
    public ArrayList<ViaggioAttivita> getViaggiAttivita(int id) {
        String where = VIAGGIOATTIVITA_ID_VIAGGIO + "= ?";
        String[] whereArgs = { Integer.toString(id) };
        this.openReadableDB();
        Cursor cursor = db.query(VIAGGIOATTIVITA_TABLE, null,
                where,whereArgs,
                null, null, null);
        if(cursor == null){
            return null;
        }
        ArrayList<ViaggioAttivita> viaggioAttivita = new ArrayList<ViaggioAttivita>();
        while(cursor.moveToNext()){
            viaggioAttivita.add(getViaggioAttivitaFromCursor(cursor));
        }

        if (cursor != null)
            cursor.close();
        this.closeDB();

        return viaggioAttivita;
    }
    public boolean getViaggiAttivitaBool(int id) {
        String where = VIAGGIOATTIVITA_ID_VIAGGIO + "= ?";
        String[] whereArgs = { Integer.toString(id) };
        this.openReadableDB();
        Cursor cursor = db.query(VIAGGIOATTIVITA_TABLE, null,
                where,whereArgs,
                null, null, null);
        cursor.moveToFirst();
        if(cursor != null && cursor.getCount()>0){
            cursor.close();
            this.closeDB();

            return true;
        }
        else
        {
            Log.e("bool", "false");
            this.closeDB();

            return false;
        }

    }

    private static ViaggioAttivita getViaggioAttivitaFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                int id = cursor.getInt(VIAGGIOATTIVITA_ID_VIAGGIO_COL);
                String placeid = cursor.getString(VIAGGIOATTIVITA_PLACE_ID_COL);
                ViaggioAttivita viaggioAttivita = new ViaggioAttivita(id,placeid);
                return viaggioAttivita;
            }
            catch(Exception e) {
                return null;
            }
        }
    }
    public int deleteViaggioAttivita(long id, String place_id) {
        String where = VIAGGIOATTIVITA_ID_VIAGGIO + "= ? " + "AND " + VIAGGIOATTIVITA_PLACE_ID + "= ?" ;
        String[] whereArgs = { String.valueOf(id), place_id };

        this.openWriteableDB();
        int rowCount = db.delete(VIAGGIOATTIVITA_TABLE, where, whereArgs);
        this.closeDB();
        return rowCount;
    }

    public long insertAttivitaGiorno(AttivitaGiorno attivitaGiorno) {
        ContentValues cv = new ContentValues();
        cv.put(ATTIVITAGIORNO_PLACE_ID,attivitaGiorno.getPlace_id());
        cv.put(ATTIVITAGIORNO_DATA, attivitaGiorno.getData());
        cv.put(ATTIVITAGIORNO_ID_VIAGGIO, attivitaGiorno.getId_viaggio());
        cv.put(ATTIVITAGIORNO_QUANDO, attivitaGiorno.getQuando());
        this.openWriteableDB();
        long rowID = db.insert(ATTIVITAGIORNO_TABLE, null, cv);
        this.closeDB();

        return rowID;
    }

    public int deleteAttivitaGiorno( String place_id, String data, long id, String quando) {
        String where = ATTIVITAGIORNO_PLACE_ID + "= ?"  + " AND "  + ATTIVITAGIORNO_DATA + " = ? "  + " AND "  + ATTIVITAGIORNO_ID_VIAGGIO + "=  ?" + " AND " + ATTIVITAGIORNO_QUANDO + " = ?" ;
        String[] whereArgs = {place_id,data, String.valueOf(id),quando};
        this.openWriteableDB();
        int rowCount = db.delete(ATTIVITAGIORNO_TABLE, where, whereArgs);
        this.closeDB();
        return rowCount;
    }

    public int deleteAttivitaGiorno(String place_id, long id) {
        String where = ATTIVITAGIORNO_PLACE_ID + "= ?"  + " AND " + ATTIVITAGIORNO_ID_VIAGGIO + "=  ?";
        String[] whereArgs = {place_id, String.valueOf(id)};
        this.openWriteableDB();
        int rowCount = db.delete(ATTIVITAGIORNO_TABLE, where, whereArgs);
        this.closeDB();
        return rowCount;
    }


    public ArrayList<AttivitaGiorno> getAttivitaGiorno(String data, int id, String quando) {
        String where = ATTIVITAGIORNO_DATA + " = ? " +  " AND " + ATTIVITAGIORNO_ID_VIAGGIO + " = ? " +  " AND " + ATTIVITAGIORNO_QUANDO + " =?";
        String[] whereArgs = { data,Integer.toString(id),quando };
        this.openReadableDB();
        Cursor cursor = db.query(ATTIVITAGIORNO_TABLE, null,
                where,whereArgs,
                null, null, null);
        if(cursor == null){
            return null;
        }
        ArrayList<AttivitaGiorno> attivitaGiornos = new ArrayList<AttivitaGiorno>();
        int j = 0;
        while(cursor.moveToNext()){

            attivitaGiornos.add(getAttivitaGiornoFromCursor(cursor));
            Log.e("attivitagiorno",attivitaGiornos.get(j).getData() + " " + attivitaGiornos.get(j).getPlace_id());
                    j++;
        }

        if (cursor != null)
            cursor.close();
        this.closeDB();

        return attivitaGiornos;
    }

    private static AttivitaGiorno getAttivitaGiornoFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                String place_id = cursor.getString(ATTIVITAGIORNO_PLACE_ID_COL);
                String data = cursor.getString(ATTIVITAGIORNO_DATA_COL);
                int id = cursor.getInt(ATTIVITAGIORNO_ID_VIAGGIO_COL);
                String quando = cursor.getString(ATTIVITAGIORNO_QUANDO_COL);
                int evento_id = cursor.getInt(ATTIVITAGIORNO_EVENTO_ID_COL);
                AttivitaGiorno attivitaGiorno = new AttivitaGiorno(place_id,data,id,quando,evento_id);
                return attivitaGiorno;
            }
            catch(Exception e) {
                return null;
            }
        }
    }
    public long insertFoto(Foto foto) {
        ContentValues cv = new ContentValues();
        cv.put(FOTO_ID_VIAGGIO, foto.getId_viaggio());
        cv.put(FOTO_PATH, foto.getPath());
        this.openWriteableDB();
        long rowID = db.insert(FOTO_TABLE, null, cv);
        this.closeDB();
        return rowID;
    }
    public ArrayList<Foto> getFoto(String nome_viaggio) {
        int id_viaggio = getIdViaggio(nome_viaggio);
        String where =  FOTO_ID_VIAGGIO + " = ? " ;
        String[] whereArgs = { Integer.toString(id_viaggio) };
        this.openReadableDB();
        Cursor cursor = db.query(FOTO_TABLE, null,
                where,whereArgs,
                null, null, null);
        if(cursor == null){
            return null;
        }
        ArrayList<Foto> fotos = new ArrayList<>();
        while(cursor.moveToNext()){
            fotos.add(getFotoFromCursor(cursor));
        }

        if (cursor != null)
            cursor.close();
        this.closeDB();

        return fotos;
    }

    private static Foto getFotoFromCursor(Cursor cursor) {
        if (cursor == null || cursor.getCount() == 0){
            return null;
        }
        else {
            try {
                int id = cursor.getInt(FOTO_ID_VIAGGIO_COL);
                String path = cursor.getString(FOTO_PATH_COL);
                Foto foto = new Foto(id,path);
                return foto;
            }
            catch(Exception e) {
                return null;
            }
        }
    }
    public int deleteFoto(String path,String nome_viaggio) {
        int id =  getIdViaggio(nome_viaggio);
        String where = FOTO_PATH + "= ?"  + " AND " + FOTO_ID_VIAGGIO + "=  ?";
        String[] whereArgs = {path, String.valueOf(id)};
        this.openWriteableDB();
        int rowCount = db.delete(FOTO_TABLE, where, whereArgs);
        this.closeDB();
        return rowCount;
    }




}
