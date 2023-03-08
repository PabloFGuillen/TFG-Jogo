package com.example.jogo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    /*Pantalla principal. Aquí comprobamos si existe un fichero que contiene en un xml la información del usuario y
    la contraseña. En el caso de que no exista, lo que hacemos es dirigirnos a la pantalla de logín. En caso contrario,
    iniciamos sesión cin la información del usuario y la contraseña guardada en dicho documento
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        File ficheroXML = new File("login.xml");
        if(ficheroXML.exists() == false){

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
        else{
            try {
                // Intent a pantalla principal
                Thread.sleep(3000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}