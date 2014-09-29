package com.simarc86.mystocks;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * <br></br>
 * Gestiona la creacion/actualizacion de las bbdd de la aplicacion.<br></br>
 * <br></br>
 * Created by Miguel angel Rossi Sanahuja on 13/09/13.
 */
public class SQLHelper extends SQLiteOpenHelper {

    // BBDD
    private static final String nombre= "datos.db"; // Nombre de la bbdd
    private static final SQLiteDatabase.CursorFactory factory = null; // to use for creating cursor objects, or null for the default
    private static final int version = 1; // Version de la bbdd

    // Tablas
    public static final String STOCKS = "revistas"; // Catalogo de revistas

    // Columnas Revistas
    //nombre, precio, variación neta, variación, volumen, hora

    public static final String STOCK_ID = "ide"; // Identificador
    public static final float PRICE = 0; // Posicion de la columna en la tabla
    public static final float VAR_NET = 0; // Titulo de la revista
    public static final float VAR = 0; // Posicion de la columna en la tabla
    public static final float VOLUME = 0; // URL del pdf
    public static final String DATE = "HH:mm:ss"; // Posicion de la columna en la tabla


    /**
     * <br></br>
     * Constructor.<br></br>
     * <br></br>
     * @param contexto Contexto.
     */
    public SQLHelper (Context contexto) {
        super(contexto, nombre, factory, version);
    } // Fin SQLHelper (Context contexto)

    /**
     * <br></br>
     * Crea la bbdd solo si no existe previamente.<br></br>
     * <br></br>
     * @param sqLiteDatabase La BBDD.
     */
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        final String
                creaCatalogo = // Catalogo de revistas
                "CREATE TABLE " + STOCKS + " (" +
                        STOCK_ID + " TEXT PRIMARY KEY, " + // Id de la revista
                        PRICE + " TEXT, " + // Titulo de la revista
                        VAR_NET + " TEXT, " + // URL del pdf
                        VAR + " TEXT, " + // URL del pdf
                        VOLUME + " TEXT, " + // URL del pdf
                        DATE + " TEXT, " + // URL del pdf
        ")";

        // crea las tablas
        sqLiteDatabase.execSQL(creaCatalogo);
    } // Fin onCreate(SQLiteDatabase sqLiteDatabase)

    /**
     * <br></br>
     * En el caso de que la version de la bbdd existente != a la actual se ejecuta este metodo para la actualizacion de la bbdd.<br></br>
     * <br></br>
     * @param sqLiteDatabase La BBDD.
     * @param i Version antigua de la BBDD.
     * @param i2 Version actual de la BBDD.
     */
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2) {

    } // Fin onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i2)

}
