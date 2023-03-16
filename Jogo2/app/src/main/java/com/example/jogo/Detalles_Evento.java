package com.example.jogo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class Detalles_Evento extends AppCompatActivity {

    private static Evento evento;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalles_evento);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        TextView nombreE = (TextView) findViewById(R.id.nombreDE);
        TextView ubicacionDE = (TextView) findViewById(R.id.ubicacionDE);
        TextView descripcionDE = (TextView) findViewById(R.id.descripcionDE);
        TextView diaDE = (TextView) findViewById(R.id.diaDE);
        TextView horaDE = (TextView) findViewById(R.id.horaDE);
        TextView nombreUDE = (TextView) findViewById(R.id.nombreUDE);
        Button asistir = (Button) findViewById(R.id.asistirDE);

        ImageView qr = (ImageView) findViewById(R.id.qrDE);
        ImageView fotoU = (ImageView) findViewById(R.id.fotoUDE);
        nombreE.setText(evento.getNombre());
        nombreUDE.setText(evento.getNombreU());
        descripcionDE.setText(evento.getDescripcion());
        ubicacionDE.setText(evento.getCalle() + ", " + evento.getCiudad() + ", " + evento.getLocalidad());
        qr.setImageBitmap(evento.getQr());
        fotoU.setImageBitmap(evento.getFotoU());
        diaDE.setText(evento.getDia().getDay()+"/"+evento.getDia().getMonth()+"\n"+evento.getDia().getYear());
        horaDE.setText(evento.getHora().getHours()+":"+evento.getHora().getMinutes());
        asistir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try{
                    Conector con = new Conector();
                    con.asistirEvento(Persona.getNombreU(), evento.id);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }
            }
        });


    }

    public static void setEvento(Evento evento){
        Detalles_Evento.evento = evento;
    }
}