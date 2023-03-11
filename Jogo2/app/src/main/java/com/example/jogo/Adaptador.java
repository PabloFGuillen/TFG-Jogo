package com.example.jogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

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

        nombreEE.setText(evento.getNombre());
        ubicacion.setText(evento.getCalle() + ", " + evento.getCiudad() + ", " + evento.getLocalidad());
        dia.setText(evento.getDia().toString());
        hora.setText(evento.getHora().toString());
        return v;
    }
}
