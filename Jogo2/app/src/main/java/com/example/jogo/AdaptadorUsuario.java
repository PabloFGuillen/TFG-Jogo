package com.example.jogo;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import java.util.List;


public class AdaptadorUsuario extends ArrayAdapter<Cuenta> {
    private Context ctx;
    private int layoutTemplate;
    private List<Cuenta> ejemplo;
    boolean aumentado = false;
    public AdaptadorUsuario(@NonNull Context context, int resource, @NonNull List<Cuenta> objects) {
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
        TextView nombreC = (TextView) v.findViewById(R.id.nombreCuenta);
        ImageView fotoC = (ImageView) v.findViewById(R.id.fotoCuenta);
        Cuenta cuenta = ejemplo.get(position);
        nombreC.setText(cuenta.getNombreU());
        fotoC.setImageBitmap(cuenta.getFotoP());
        fotoC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Perfil.setNombreU(cuenta.getNombreU());
                Perfil.setFotoP(cuenta.getBytes());
                Intent t = new Intent(getContext(), Perfil_Usuario.class);
                getContext().startActivity(t);
            }
        });
        return v;
    }
}
