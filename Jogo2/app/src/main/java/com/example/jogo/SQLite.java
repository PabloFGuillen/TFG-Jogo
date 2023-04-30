package com.example.jogo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class SQLite extends SQLiteOpenHelper {


    private static final String DATABASE_NAME = "jogo";
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_TABLA = "cuentas";

    public SQLite(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE cuenta (nombre VARCHAR(100) PRIMARY KEY, contraseña VARCHAR(100))";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public static void borrado(SQLiteDatabase db){
        String update = "DELETE FROM cuenta WHERE cuenta.nombre =  " + Persona.getNombreU();
        db.execSQL(update);
    }

    public static void introducir(SQLiteDatabase db){
        String insertar = "INSERT INTO cuenta VALUES ('"+ Persona.getNombreU()+"', '" + Persona.getContraseña() +"');";
        db.execSQL(insertar);
    }

    @SuppressLint("Range")
    public void setPersona(SQLiteDatabase db){
        String insertar = "SELECT * FROM cuenta";
        Cursor cursor = db.rawQuery(insertar, null);

        // Recorrer los resultados del Cursor y guardarlos en una variable.
        ArrayList<String> resultados = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                Persona.setNombreU(cursor.getString(cursor.getColumnIndex("nombre")));
                Persona.setContraseña(cursor.getString(cursor.getColumnIndex("contraseña")));
            } while (cursor.moveToNext());
        }

        // Cerrar el Cursor y la conexión a la base de datos.
        cursor.close();
    }

}
