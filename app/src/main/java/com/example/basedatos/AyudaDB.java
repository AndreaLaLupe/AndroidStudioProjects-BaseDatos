package com.example.basedatos;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import androidx.annotation.Nullable;

public class AyudaDB extends SQLiteOpenHelper {

    /* Inner class that defines the table contents */
    public static class DatosTabla implements BaseColumns {
        public static final String TABLE_NAME = "Lecturas";
        public static final String COLUMNA_OPCIONES = "opciones";
        public static final String COLUMNA_FECHA= "fecha";
        public static final String COLUMNA_HORA = "hora";
        public static final String COLUMNA_DATO = "dato";

        private static final String CREAR_TABLA =
                "CREATE TABLE " + DatosTabla.TABLE_NAME + " (" +
                        DatosTabla.COLUMNA_OPCIONES + " TEXT," +
                        DatosTabla.COLUMNA_FECHA + " TEXT," +
                        DatosTabla.COLUMNA_HORA + " TEXT,"+
                        DatosTabla.COLUMNA_DATO + " TEXT)";;

        private static final String SQL_DELETE_ENTRIES =
                "DROP TABLE IF EXISTS " + DatosTabla.TABLE_NAME;
    }

    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "BaseContadores.db";

    public AyudaDB(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DatosTabla.CREAR_TABLA);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(DatosTabla.SQL_DELETE_ENTRIES);
        onCreate(db);
    }
}
