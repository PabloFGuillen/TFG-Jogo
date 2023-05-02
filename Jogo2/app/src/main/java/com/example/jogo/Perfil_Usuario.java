package com.example.jogo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

public class Perfil_Usuario extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil_usuario);
        ListView listView = (ListView) findViewById(R.id.ListViewMisEventos);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        Conector con;
        try {
            con = new Conector();
            TextView textViewNumEventos = (TextView) findViewById(R.id.textView7);
            textViewNumEventos.setText(con.getEventos().size()+"");
            TextView nombrePUA = (TextView) findViewById(R.id.nombrePUA);
            nombrePUA.setText(Perfil.getNombreU());
            Button seguir = (Button) findViewById(R.id.seguir);
            Button borrar = (Button) findViewById(R.id.dejar);
            boolean sigue = con.comprobarSeguidor(Perfil.getNombreU());
            if(sigue == false){
                seguir.setVisibility(View.GONE);
                borrar.setVisibility(View.VISIBLE);
            }

            seguir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Conector con = new Conector();
                        con.seguir(Perfil.getNombreU());
                        borrar.setVisibility(View.VISIBLE);
                        seguir.setVisibility(View.GONE);
                    }catch(Exception e){

                    }
                }
            });
            borrar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try{
                        Conector con = new Conector();
                        con.eliminarSiguiendo(Perfil.getNombreU());
                        seguir.setVisibility(View.VISIBLE);
                        borrar.setVisibility(View.GONE);
                    }catch(Exception e){

                    }
                }
            });
            ArrayList<Evento> lista_eventos = con.eventoPerfil(Perfil.getNombreU());
            if(lista_eventos != null){
                MiAdaptadorEventos adaptadorEventos = new MiAdaptadorEventos(this, R.layout.evento_item2, lista_eventos);
                listView.setAdapter(adaptadorEventos);
            }

        } catch(Exception e){

        }

    }
}