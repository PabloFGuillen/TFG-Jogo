package com.example.jogo;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.sql.SQLException;

public class Editar_Perfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        EditText nNU = findViewById(R.id.NNombreUsuario);
        EditText desc = findViewById(R.id.NNombreUsuario2);
        Button guardar = findViewById(R.id.button2);
        ImageView imagen = findViewById(R.id.fotoCP);
        guardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String n = nNU.getText().toString();
                String d = desc.getText().toString();
                try {
                    Conector conector = new Conector();
                    if(n != null){
                        if(conector.usuario(n) == false){
                            conector.cambiarNombre(n);
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Nombre ya existente", Toast.LENGTH_LONG).show();
                        }
                    }

                    if(d != null){
                        if(n.length() > 33){
                            Toast.makeText(getApplicationContext(), "Descripción muy larga", Toast.LENGTH_LONG).show();
                        }
                        else{
                            conector.cambiarDescripcion(d);
                        }
                    }
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                }

            }
        });
        imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }


}