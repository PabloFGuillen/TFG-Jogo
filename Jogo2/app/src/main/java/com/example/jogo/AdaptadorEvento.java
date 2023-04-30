package com.example.jogo;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.time.LocalDate;
import java.util.List;
import java.util.StringTokenizer;

public class AdaptadorEvento extends ArrayAdapter<Evento> {
    private Context ctx;
    private int layoutTemplate;
    private List<Evento> listaEventos;
    public AdaptadorEvento(@NonNull Context context, int resource, @NonNull List<Evento> objects) {
        super(context, resource, objects);
        this.ctx = context;
        this.layoutTemplate = resource;
        this.listaEventos = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        // Inicializamos variables.
        View v = LayoutInflater.from(ctx).inflate(layoutTemplate, parent, false);

        //Obtener informacion del elemento de la lista que estamos recorriendo en este momento
        Evento eventoActual = listaEventos.get(position);

        //Rescatar los elementos de la interfaz de usuario de la plantilla
        TextView nombreEE = (TextView) v.findViewById(R.id.nombreEvento);
        TextView ubicacion = (TextView) v.findViewById(R.id.ubicacionEvento);
        TextView descripcion = (TextView) v.findViewById(R.id.descripcionEvento);
        TextView dia = (TextView) v.findViewById(R.id.fechaEvento);
        TextView hora = (TextView) v.findViewById(R.id.horaEvento);
        Button botonBorrar = (Button) v.findViewById(R.id.asistirPU);
        Button botonFinalizado = (Button)v.findViewById(R.id.finalizado);

        nombreEE.setText(eventoActual.getNombre());
        ubicacion.setText(eventoActual.getUbicacion());

        if(eventoActual.getDescripcion().length()>55){
            descripcion.setText(eventoActual.getDescripcion().substring(0,55)+"...");
        }else{
            descripcion.setText(eventoActual.getDescripcion());
        }
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String cadenaFechaEvento = eventoActual.getDia().toString();


            LocalDate fechaHoy = LocalDate.now();
            LocalDate fechaEvento = LocalDate.parse(cadenaFechaEvento);

            if(fechaHoy.isBefore(fechaEvento)){
                //La fecha del evento no ha caducado

                botonBorrar.setVisibility(View.VISIBLE);
                botonFinalizado.setVisibility(View.GONE);


            }else{
                //La fecha del evento si ha caducado
                botonBorrar.setVisibility(View.GONE);
                botonFinalizado.setVisibility(View.VISIBLE);
            }

        }
        StringTokenizer token = new StringTokenizer(eventoActual.getDia().toString(), "-");
        String diaE, mesE, anioE;
        anioE = token.nextToken().toString();
        mesE = token.nextToken().toString();
        diaE = token.nextToken().toString();
        dia.setText(diaE + "/" + mesE + "\n" + anioE);
        dia.setText(eventoActual.getDia().toString());

        if(eventoActual.getHora().getMinutes() < 10){
            hora.setText(eventoActual.getHora().getHours() + ":" + "0" +eventoActual.getHora().getMinutes());
        }
        else{
            hora.setText(eventoActual.getHora().getHours() + ":" +eventoActual.getHora().getMinutes());

        }
        botonBorrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Conector con;
                try{
                    con = new Conector();
                    con.eliminarEvento(eventoActual);

                }catch(Exception e){

                }
            }
        });
        return v;
    }
}
