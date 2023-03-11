package com.example.jogo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import java.io.File;
import java.io.FileOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

public class Login extends AppCompatActivity {

    /*Pantalla del login. Aqui o bien iinciamos sesión o bien nos dirigimos a la pantalla de registro. En cas de iniciar
    * sesión, lo que hacemoso es crear el fichero XML donde guardaremos la información de nombre de usuario y contraseña*/
    public EditText usuario, contraseña;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        usuario = (EditText) findViewById(R.id.nombre);
        contraseña = (EditText) findViewById(R.id.contraseña);
    }

    public void inicioS(View view){
        String nUsuario = usuario.getText().toString();
        String password = contraseña.getText().toString();
        if(nUsuario.equals("") == false && contraseña.equals("")==false) {
            try {
                Conector con = new Conector();
                boolean encontrado = con.login(nUsuario, password);
                boolean validado = false;
                if (encontrado == false) {
                    Toast.makeText(Login.this, "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                } else {
                    File ficheroXML = new File("/storage/emulated/0/users.xml");
                    validado = con.validad(nUsuario, password);
                    if(validado == true){
                        Intent c = new Intent(Login.this, pantalla_p.class);
                        c.putExtra("usuario", nUsuario);
                        c.putExtra("contraseña", password);
                        startActivity(c);
                    }
                    else{
                        Toast.makeText(Login.this, "Valida tu cuenta de Jogo. Comprueba tu correo.", Toast.LENGTH_LONG).show();
                    }
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else{
            Toast.makeText(this, "Por favor, introduzca usuario y contraseña", Toast.LENGTH_LONG).show();
        }
    }

    public void registrarse(View view){
        Intent t = new Intent(this, Correo.class);
        startActivity(t);
    }

    public void crearEscribir(String nUsuario, String password){
        //
    }
}