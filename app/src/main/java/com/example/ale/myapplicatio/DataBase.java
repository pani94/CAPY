package com.example.ale.myapplicatio;

import java.net.PortUnreachableException;

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

    public static final String CREATE_VIAGGIO_TABLE
}
