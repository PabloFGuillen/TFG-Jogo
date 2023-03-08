package com.example.jogo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.regex.Pattern;

public class Correo extends AppCompatActivity {
    /*
    Aquí introducimos un correo para registarnos en la base de datos. Comporbamos si ya existe  en la base de datos
    y si cumple con el patrón establecido (contener un arroba). Hay que implementar la comprobación
    de que el correo introducido existe.
     */
    private EditText email;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_correo);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        email = (EditText) findViewById(R.id.correoR);
    }

    public void siguiente(View view){
        String correo = email.getText().toString();
        boolean encontrado = false;
        if(validarEmail(correo) == true) {
            try {
                Conector con = new Conector();
                encontrado = con.email(correo);
                if (encontrado == true) {
                    Toast.makeText(Correo.this, "Este correo ya existe", Toast.LENGTH_LONG).show();
                } else {
                    Intent t = new Intent(Correo.this, Usuario.class);
                    t.putExtra("correo", correo);
                    startActivity(t);

                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else{
            Toast.makeText(this, "Por favor, introduzca un correo válido", Toast.LENGTH_LONG).show();
        }
    }

    public void cancelar(View view){
        Intent t = new Intent(this, Login.class);
        startActivity(t);
    }

    public boolean validarEmail(String email){
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}