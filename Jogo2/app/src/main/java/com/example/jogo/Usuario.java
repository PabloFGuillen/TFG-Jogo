package com.example.jogo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

public class Usuario extends AppCompatActivity {

    /*
    Es la pantalla para introducir el nombre de la cuenta creada. Se comprueba si el nombre ya esta siendo utilizado.
     */
    private EditText usuario;
    private String correo;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        usuario = (EditText) findViewById(R.id.nUsuarioR);
        Bundle extras = getIntent().getExtras();
        correo = extras.getString("correo");
    }

    public void siguiente(View view){
        String nUsuario = usuario.getText().toString();
        // Comprobamos que el nombre de usuario introducido njo se a un string vacio

        // Comprobamos que el string no este vacio
        if(nUsuario.equals("") == false){
            try{
                // Comprobamos que el nombre de usuario no exsita.
                Conector con = new Conector();
                if(con.usuario(nUsuario) == true){
                    Toast.makeText(Usuario.this, "El nombre de usuario ya existe", Toast.LENGTH_LONG).show();
                }
                else{
                    // Si no existe, remplazamos los espacion en blanco por barras-bajas
                    Intent t = new Intent(Usuario.this, Contrasena.class);
                    nUsuario = nUsuario.replaceAll(" ", "_");
                    // Mandamos dicha información al siguiente activity.
                    t.putExtra("nombre", nUsuario);
                    t.putExtra("correo", correo);
                    startActivity(t);
                }
            }catch(Exception e){
                System.out.println(e.getMessage());
            }
        }
    }
    public void cancelar(View view){
        // Función para volver a la pantalla de logeo e caso de que no quieras registrarte.
        Intent t = new Intent(this, Login.class);
        startActivity(t);
    }
}