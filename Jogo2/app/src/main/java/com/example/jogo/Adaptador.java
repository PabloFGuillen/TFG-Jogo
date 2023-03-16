package com.example.jogo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.sql.SQLException;
import java.util.List;
import java.util.StringTokenizer;

public class Adaptador extends ArrayAdapter<Evento> {
    private Context ctx;
    private int layoutTemplate;
    private List<Evento> ejemplo;

    public Adaptador(@NonNull Context context, int resource, @NonNull List<Evento> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.layoutTemplate = resource;
        this.ejemplo = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View v = LayoutInflater.from(ctx).inflate(layoutTemplate, parent, false);
        Evento evento = ejemplo.get(position);
        TextView nombreEE = (TextView) v.findViewById(R.id.nombreEE);
        TextView ubicacion = (TextView) v.findViewById(R.id.ubicacionEE);
        TextView descripcion = (TextView) v.findViewById(R.id.descripcionEE);
        TextView dia = (TextView) v.findViewById(R.id.diaEE);
        TextView hora = (TextView) v.findViewById(R.id.horaEE);
        TextView nombreU = (TextView) v.findViewById(R.id.nombreUE);
        ImageView qr = (ImageView) v.findViewById(R.id.qrDE2);
        boolean aumentado = false;
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aumentado == false) {
                    nombreEE.setMaxHeight(100);
                    nombreEE.setText(evento.getNombre());
                    descripcion.setMaxHeight(100);
                    descripcion.setText(evento.getDescripcion());
                    qr.setVisibility(View.VISIBLE);
                    qr.setImageBitmap(evento.getQr());
                }
                else{
                    nombreU.setMaxHeight(40);
                    descripcion.setMaxHeight(40);
                    if(evento.getDescripcion().length() > 55) {
                        descripcion.setText(evento.getDescripcion().substring(0, 55).trim()+"...");
                    }
                    else{
                        descripcion.setText(evento.getDescripcion());
                    }
                    if(evento.getNombre().length() > 13){
                        nombreEE.setText(evento.getNombre().substring(0, 10) + "...");
                    }
                    else{
                        nombreEE.setText(evento.getNombre());
                    }
                }

            }
        });
        Button asistir = (Button) v.findViewById(R.id.Asistir);
        asistir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    Conector con = new Conector();
                    con.asistirEvento(Persona.getNombreU(), evento.getId());
                    Toast.makeText(getContext(), "Asistencia Confirmada", Toast.LENGTH_LONG).show();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        });


        StringTokenizer token = new StringTokenizer(evento.getDia().toString(), "-");
        String diaE, mesE, anioE;
        anioE = token.nextToken().toString();
        mesE = token.nextToken().toString();
        diaE = token.nextToken().toString();
        ubicacion.setText(evento.getCalle() + ", " + evento.getCiudad() + ", " + evento.getLocalidad());
        dia.setText(diaE + "/" + mesE + "\n" + anioE);

        if(evento.getHora().getMinutes() < 10){
            hora.setText(evento.getHora().getHours() + ":" + "0" +evento.getHora().getMinutes());
        }
        else{
            hora.setText(evento.getHora().getHours() + ":" +evento.getHora().getMinutes());

        }
        nombreU.setText(evento.getNombreU());
        if(evento.getDescripcion().length() > 55) {
            descripcion.setText(evento.getDescripcion().substring(0, 55).trim()+"...");
        }
        else{
            descripcion.setText(evento.getDescripcion());
        }
        if(evento.getNombre().length() > 13){
            nombreEE.setText(evento.getNombre().substring(0, 10) + "...");
        }
        else{
            nombreEE.setText(evento.getNombre());
        }
        return v;
    }
}
