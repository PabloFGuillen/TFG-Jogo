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
        // Recogemos el correo y validamos si tiene una estructura correcta a.k.a si tiene arroba y otras caracteristicas.

        // Si las cumple, se busca si el correo tiene una cuenta asociada.
        if(validarEmail(correo) == true) {
            try {
                Conector con = new Conector();
                encontrado = con.email(correo);
                // Si tiene una cuenta asociada, se informa al usuario de dicha situación.
                if (encontrado == true) {
                    Toast.makeText(Correo.this, "Este correo ya existe", Toast.LENGTH_LONG).show();
                }
                // En caso de que no tenga cuenta asociada, se manda dicha información al activity del nombre de usuario.
                else {

                    Intent t = new Intent(Correo.this, Usuario.class);
                    t.putExtra("correo", correo);
                    startActivity(t);

                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        // Si el correo introducido no tiene una estructura adecuada, se informa al usuario.
        else{
            Toast.makeText(this, "Por favor, introduzca un correo válido", Toast.LENGTH_LONG).show();
        }
    }

    public void cancelar(View view){
        //Esta función sirve para volver a la pantalla de logeo por si no quieres registarte.
        Intent t = new Intent(this, Login.class);
        startActivity(t);
    }

    public boolean validarEmail(String email){
        // Aqui validamos el email.
        Pattern pattern = Patterns.EMAIL_ADDRESS;
        return pattern.matcher(email).matches();
    }
}