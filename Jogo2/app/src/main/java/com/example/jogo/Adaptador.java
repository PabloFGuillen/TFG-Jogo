package com.example.jogo;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;
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
import androidx.cardview.widget.CardView;

import java.util.List;
import java.util.StringTokenizer;
import java.util.TimeZone;

public class Adaptador extends ArrayAdapter<Evento> {
    private Context ctx;
    private int layoutTemplate;
    private List<Evento> ejemplo;
    boolean aumentado = false;
    public Adaptador(@NonNull Context context, int resource, @NonNull List<Evento> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.layoutTemplate = resource;
        this.ejemplo = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inicializamos variables.
        View v = LayoutInflater.from(ctx).inflate(layoutTemplate, parent, false);
        Evento evento = ejemplo.get(position);
        TextView nombreEE = (TextView) v.findViewById(R.id.nombreEE);
        TextView ubicacion = (TextView) v.findViewById(R.id.ubicacionEE);
        TextView descripcion = (TextView) v.findViewById(R.id.descripcionEE);
        TextView dia = (TextView) v.findViewById(R.id.diaEE);
        TextView hora = (TextView) v.findViewById(R.id.horaEE);
        TextView nombreU = (TextView) v.findViewById(R.id.nombreUE);
        ImageView qr = (ImageView) v.findViewById(R.id.qrDE2);
        TextView disponibles = (TextView) v.findViewById(R.id.disponibles);
        ImageView fotoU = (ImageView) v.findViewById(R.id.imageView7);
        Button cancelar = (Button) v.findViewById(R.id.cancelarEE);
        Button asistir = (Button) v.findViewById(R.id.Asistir);
        // Lo que hacemos es mostrar el nombre de usuario, evento, localidad, dia, hora así como nombre y qr
        fotoU.setImageBitmap(evento.getFotoU());
        RelativeLayout layout = (RelativeLayout) v.findViewById(R.id.layout);
        layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(aumentado == false) {
                    // Si el usuario le click, se expande con toda la información del evento
                    nombreEE.setMaxHeight(1000);
                    nombreEE.setText(evento.getNombre());
                    descripcion.setMaxHeight(1000);
                    descripcion.setText(evento.getDescripcion());
                    qr.setVisibility(View.VISIBLE);
                    qr.setImageBitmap(evento.getQr());
                    disponibles.setVisibility(View.VISIBLE);
                    disponibles.setText("Plazas Disponibles: " + String.valueOf(evento.getPlazas_disponibles()));
                    aumentado = true;
                }
                else{
                    // En caso contrario, lo que hacemos es mostrar una versión reducido.

                    //(No mostramos ni qr ni plazas disponibles)
                    nombreU.setMaxHeight(80);
                    descripcion.setMaxHeight(100);
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
                    qr.setVisibility(View.GONE);
                    disponibles.setVisibility(View.GONE);
                    aumentado = false;

                }

            }
        });
        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Conector con = new Conector();
                    con.canceleraE(evento.getId(), Persona.getNombreU());
                    Toast.makeText(getContext(), "Asistencia Cancelada", Toast.LENGTH_LONG).show();
                    asistir.setVisibility(View.VISIBLE);
                    cancelar.setVisibility(View.GONE);
                }catch(Exception e){

                }
            }
        });

        asistir.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                try {
                    Conector con = new Conector();
                    con.asistirEvento(Persona.getNombreU(), evento.getId());
                    Toast.makeText(getContext(), "Asistencia Confirmada", Toast.LENGTH_LONG).show();
                    cancelar.setVisibility(View.VISIBLE);
                    asistir.setVisibility(View.GONE);
                    ContentValues event = new ContentValues();
                    event.put(CalendarContract.Events.TITLE, "Mi evento");
                    event.put(CalendarContract.Events.DESCRIPTION, "Descripción del evento");
                    event.put(CalendarContract.Events.EVENT_LOCATION, "Ubicación del evento");
                    event.put(CalendarContract.Events.CALENDAR_ID, 1); // ID del calendario a utilizar
                    event.put(CalendarContract.Events.DTSTART, evento.getDia().toString()); // fecha y hora de inicio en formato milisegundos
                    event.put(CalendarContract.Events.EVENT_TIMEZONE, TimeZone.getDefault().getID());

                    ContentResolver cr = getContext().getContentResolver();
                    Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, event);
                    long eventID = Long.parseLong(uri.getLastPathSegment());
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
