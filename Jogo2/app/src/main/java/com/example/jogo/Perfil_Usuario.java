package com.example.jogo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
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
            TextView textViewNumEventos = (TextView) findViewById(R.id.textViewEventos);
            textViewNumEventos.setText(con.getEventos().size()+"");
            TextView nombrePUA = (TextView) findViewById(R.id.nombrePUA);
            nombrePUA.setText(Perfil.getNombreU());
            ArrayList<Evento> lista_eventos = con.eventoPerfil(Perfil.getNombreU());
            if(lista_eventos != null){
                MiAdaptadorEventos adaptadorEventos = new MiAdaptadorEventos(this, R.layout.evento_item2, lista_eventos);
                listView.setAdapter(adaptadorEventos);
            }

        } catch(Exception e){

        }

    }
}