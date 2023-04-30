package com.example.jogo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.util.Xml;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.sql.SQLException;
import java.time.LocalDate;

public class MainActivity extends AppCompatActivity {
    /*Pantalla principal. Aquí comprobamos si existe un fichero que contiene en un xml la información del usuario y
    la contraseña. En el caso de que no exista, lo que hacemos es dirigirnos a la pantalla de logín. En caso contrario,
    iniciamos sesión cin la información del usuario y la contraseña guardada en dicho documento
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Ocultamos la barra que sale con el nombre de la aplicación.
        boolean existe = false;

        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();

        Context context = getApplicationContext();
        String[] databases = context.databaseList();

        // Buscar si la base de datos que estamos buscando existe en la lista.
        for (String database : databases) {
            if (database.equals("jogo")) {
                context.getApplicationContext().deleteDatabase("jogo");
                System.out.println(existe);
                break;
            }
        }
        if(existe == false){
            System.out.println("Aquí");
            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent t = new Intent(MainActivity.this, Login.class);
                    startActivity(t);
                }
            }, 3000);
        }
        /* En caso de que exista, recogemos los datoss del fichero para poder iniciar sesión.*/
        else{
            SQLite dbHelper = new SQLite(this);
            SQLiteDatabase db = dbHelper.getWritableDatabase();
            dbHelper.setPersona(db);
            try {
                Conector con = new Conector();
                boolean encontrado = con.validad(Persona.getNombreU(), Persona.getNombreU());
                Intent c = new Intent(this, pantalla_p.class);
                startActivity(c);
            } catch (ClassNotFoundException e) {
                Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent t = new Intent(MainActivity.this, Login.class);
                        startActivity(t);
                    }
                }, 3000);
                e.printStackTrace();
            } catch (SQLException e) {
                System.out.println(e.getMessage());
                e.printStackTrace();
            }

        }
    }
}