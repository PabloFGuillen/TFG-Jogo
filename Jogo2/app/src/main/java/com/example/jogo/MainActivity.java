package com.example.jogo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
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
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        /* Aquí buscamos el ficheroXML en el que gaurdamos el nombre de usuario y la contraseña
           para poder saltar directamente a la pantalla principal.*/

        // Si no existe saltamos a la pantalla de logeo
        File ficheroXML = new File(".","login.xml");
        if(ficheroXML.exists() == false){
            FileOutputStream fos = null;
            try {
                fos = new FileOutputStream(ficheroXML);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }

                Intent t = new Intent(this, Login.class);
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
            try {
                //Inicamos proceso de lectura.
                try {
                    Context context = getApplicationContext();
                    FileInputStream fis = null;
                    // Especificamos el nombre del fichero
                    fis = context.openFileInput("login.xml");
                    XmlPullParserFactory xmlPullParserFactory = XmlPullParserFactory.newInstance();
                    XmlPullParser parser = xmlPullParserFactory.newPullParser();
                    parser.setInput(fis, null);
                    String nUsuario = "";
                    String password = "";
                    // Recogemos los valores
                    int eventType = parser.getEventType();
                    while (eventType != XmlPullParser.END_DOCUMENT) {
                        switch (eventType) {
                            case XmlPullParser.START_TAG:
                                String tagname = parser.getName();
                                // Miramos el valor de la etiqueta nombre
                                if (tagname.equalsIgnoreCase("nombre")) {
                                    nUsuario = parser.nextText();
                                }

                                //Miramos el valor de la etiqueta contraseña
                                else if(tagname.equalsIgnoreCase("contrasena")){
                                    password = parser.nextText();
                                }
                                break;
                            default:
                                break;
                        }
                        eventType = parser.next();
                    }
                    // Aquí comprobamos que la contraseña y usuario coinciden para ir a la pantalla principal
                    Conector con = new Conector();
                    boolean encontrado = con.login(nUsuario, password);
                    Intent c = new Intent(this, pantalla_p.class);
                    startActivity(c);
                } catch (Exception e) {
                    e.printStackTrace();
                }                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}