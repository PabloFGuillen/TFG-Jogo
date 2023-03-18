package com.example.jogo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Xml;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.xmlpull.v1.XmlSerializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;

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

    // Funcion que se ejecuta al pulsar sobre el bóton de iniciar sesion
    public void inicioS(View view){
        String nUsuario = usuario.getText().toString();
        String password = contraseña.getText().toString();
        // Recogemos el nombre de usuario y la contraseña.
        if(nUsuario.equals("") == false && contraseña.equals("")==false) {
            try {
                // Comprobamos si el nombre de usuario existe
                Conector con = new Conector();
                boolean encontrado = con.login(nUsuario, password);
                boolean validado = false;
                if (encontrado == false) {
                    Toast.makeText(Login.this, "Nombre de usuario o contraseña incorrectos", Toast.LENGTH_LONG).show();
                } else {
                    // Comprobamos si la cuenta de la aplicación a sido validada por medio del correo electrónico.
                    validado = con.validad(nUsuario, password);
                    if(validado == true){
                        Intent c = new Intent(Login.this, pantalla_p.class);
                        nUsuario = nUsuario.replaceAll(" ", "_");
                        startActivity(c);
                    }
                    else{
                        // Si no ha sido validada, se informa al usuario
                        Toast.makeText(Login.this, "Valida tu cuenta de Jogo. Comprueba tu correo.", Toast.LENGTH_LONG).show();
                    }
                }

            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
        }
        else{
            // Si no se ha introducido nombre de usuario y contraseña, se pide al usuario que lo introduzca
            Toast.makeText(this, "Por favor, introduzca usuario y contraseña", Toast.LENGTH_LONG).show();
        }
    }

    public void registrarse(View view){
        //Aquí lo que hacemos es llevar a la pantalla de registro de correo electronico.
        Intent t = new Intent(this, Correo.class);
        startActivity(t);
    }

}