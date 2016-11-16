package com.example.ale.myapplicatio;

/**
 * Created by Annalisa on 16/11/2016.
 */

public class DataBase {

    public static final String DB_NAME = "database.db";
    public static final int DB_VERSION = 1;

    public static final String VIAGGIO_TABLE = "viaggio";
    public static final String VIAGGIO_ID = "id_viaggio";
    public static final int VIAGGIO_ID_COL = 0;
    public static final String VIAGGIO_NOME = "nome_viaggio";
    public static final int VIAGGIO_NOME_COL = 1;
    public static final String VIAGGIO_PARTENZA = "partenza";
    public static final int VIAGGIO_PARTENZA_COL = 2;
    public static final String VIAGGIO_ARRIVO = "arrivo";
    public static final int VIAGGIO_ARRIVO_COL = 3;

    public static final String ATTIVITA_TABLE = "attivita";
    public static final String ATTIVITA_PLACE_ID = "place_id";
    public static final int ATTIVITA_PLACE_ID_COL = 0;
    public static final String ATTIVITA_NOME = "nome";
    public static final int ATTIVITA_NOME_COL = 1;
    public static final String ATTIVITA_INDIRIZZO = "indirizzo";
    public static final int ATTIVITA_INDIRIZZO_COL = 2;
    public static final String ATTIVITA_ORARIO = "orario";
    public static final int ATTIVITA_ORARIO_COL = 3;
    public static final String ATTIVITA_TELEFONO = "telefono";
    public static final int ATTIVITA_TELEFONO_COL = 4;
    public static final String ATTIVITA_LINK = "link";
    public static final int ATTIVITA_LINK_COL = 5;
    public static final String ATTIVITA_TIPOLOGIA = "tipologia";
    public static final int ATTIVITA_TIPOLOGIA_COL = 6;
    public static final String ATTIVITA_FOTO = "foto";
    public static final int ATTIVITA_FOTO_COL = 7;
    public static final String ATTIVITA_PREFERITO = "preferito";
    public static final int ATTIVITA_PREFERITO_COL = 8;

    public static final String GIORNO_TABLE = "giorno";
    public static final String GIORNO_DATA = "data";
    public static final int GIORNO_DATA_COL = 0;

    public static final String VIAGGIOATTIVITA_TABLE = "viaggioAttivita";
    public static final String VIAGGIOATTIVITA_ID_VIAGGIO = "id_viaggio";
    public static final int VIAGGIOATTIVITA_ID_VIAGGIO_COL = 0;
    public static final String VIAGGIOATTIVITA_PLACE_ID = "place_id";
    public static final int VIAGGIOATTIVITA_PLACE_ID_COL = 1;

    public static final String VIAGGIOGIORNO_TABLE = "viaggioGiorno";
    public static final String VIAGGIOGIORNO_ID_VIAGGIO = "id_viaggio";
    public static final int VIAGGIOGIORNO_ID_VIAGGIO_COL = 0;
    public static final String VIAGGIOGIORNO_DATA = "data";
    public static final int VIAGGIOGIORNO_DATA_COL = 1;

    public static final String ATTIVITAGIORNO_TABLE = "attivitaGiorno";
    public static final String ATTIVITAGIORNO_ID_VIAGGIO = "id_viaggio";
    public static final int ATTIVITAGIORNO_ID_VIAGGIO_COL = 0;
    public static final String ATTIVITAGIORNO_PLACE_ID = "place_id";
    public static final int ATTIVITAGIORNO_PLACE_ID_COL = 1;
    public static final String ATTIVITAGIORNO_DATA = "data";
    public static final int ATTIVITAGIORNO_DATA_COL = 2;

    public static final String CREATE_VIAGGIO_TABLE =
            "CREATE TABLE" + VIAGGIO_TABLE + " (" +
                    VIAGGIO_ID + "INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    VIAGGIO_NOME + "TEXT NOT NULL, " +
                    VIAGGIO_PARTENZA + "TEXT NOT NULL, " +
                    VIAGGIO_ARRIVO + "TEXT NOT NULL);";
    public static final String DROP_VIAGGIO_TABLE =
            "DROP TABLE IF EXISTS " + VIAGGIO_TABLE;

    public static final String CREATE_ATTIVITA_TABLE =
            "CREATE TABLE" + ATTIVITA_TABLE + " (" +
                    ATTIVITA_PLACE_ID + "TEXT PRIMARY KEY, " +
                    ATTIVITA_NOME + "TEXT NOT NULL, " +
                    ATTIVITA_INDIRIZZO + "TEXT, " +
                    ATTIVITA_ORARIO + "TEXT, " +
                    ATTIVITA_TELEFONO + "TEXT, " +
                    ATTIVITA_LINK + "TEXT, " +
                    ATTIVITA_TIPOLOGIA + "TEXT, " +
                    ATTIVITA_FOTO + "TEXT, " +
                    ATTIVITA_PREFERITO + "TEXT NOT NULL);";
    public static final String DROP_ATTIVITA_TABLE =
            "DROP TABLE IF EXISTS " + ATTIVITA_TABLE;

    public static final String CREATE_GIORNO_TABLE =
            "CREATE TABLE" + GIORNO_TABLE + " (" +
                    GIORNO_DATA + "TEXT NOT NULL PRIMARY KEY);";

    public static final String CREATE_ATTIVITAGIORNO_TABLE =
            "CREATE TABLE" + ATTIVITAGIORNO_TABLE + " (" +
                    ATTIVITAGIORNO_PLACE_ID + "TEXT NOT NULL, " +
                    ATTIVITAGIORNO_DATA + "TEXT NOT NULL, " +
                    ATTIVITAGIORNO_ID_VIAGGIO + "TEXT NOT NULL, " +
                    "PRIMARY KEY (" + ATTIVITAGIORNO_PLACE_ID + "," + ATTIVITAGIORNO_DATA + "," + ATTIVITAGIORNO_ID_VIAGGIO + ");";
    public static final String DROP_ATTIVITAGIORNO_TABLE =
            "DROP TABLE IF EXISTS " + ATTIVITAGIORNO_TABLE;

    public static final String CREATE_VIAGGIOATTIVITA_TABLE =
            "CREATE TABLE" + VIAGGIOATTIVITA_TABLE + " (" +
                    VIAGGIOATTIVITA_ID_VIAGGIO + "INTEGER, " +
                    VIAGGIOATTIVITA_PLACE_ID + "TEXT NOT NULL, " +
                    "PRIMARY KEY (" + VIAGGIOATTIVITA_PLACE_ID + "," + VIAGGIOATTIVITA_ID_VIAGGIO + ");";
    public static final String DROP_VIAGGIOATTIVITA_TABLE =
            "DROP TABLE IF EXISTS " + VIAGGIOATTIVITA_TABLE;

    public static final String CREATE_VIAGGIOGIORNO_TABLE =
            "CREATE TABLE" + VIAGGIOGIORNO_TABLE + " (" +
                    VIAGGIOGIORNO_ID_VIAGGIO + "INTEGER, " +
                    VIAGGIOGIORNO_DATA + "TEXT NOT NULL, " +
                    "PRIMARY KEY (" + VIAGGIOGIORNO_ID_VIAGGIO + "," + VIAGGIOGIORNO_DATA + ");";
    public static final String DROP_VIAGGIOGIORNO_TABLE =
            "DROP TABLE IF EXISTS " + VIAGGIOGIORNO_TABLE;
}
