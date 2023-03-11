package com.example.jogo;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.zxing.BarcodeFormat;

import java.io.ByteArrayOutputStream;

public class Contrasena extends AppCompatActivity {

    /*Pantalla usada para introducir una contraseña para la cuenta que se est
    a creando. Al aceptar, se enviara un correo de validación al usuario para confirmar la creación de la cuenta.
    Hay un patter (regEx) establecido para asegurarnos de que la contraseña es realmente segura.
     */
    private EditText contrasena;
    private EditText contrasenaR;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contrasena);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
        contrasena = (EditText) findViewById(R.id.contrasenaR);
        contrasenaR = (EditText) findViewById(R.id.contrasenaR2);

    }

    public void siguiente(View view){
        Bundle extras = getIntent().getExtras();
        String correo = extras.getString("correo");
        String usuario = extras.getString("nombre");
        String pattern = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%^&+=.])(?=\\S+$).{8,}$";
        String c = contrasena.getText().toString();
        String cR = contrasenaR.getText().toString();
        if(c.equals("") == false && cR.equals("") == false){
            if(c.equals(cR) == true){
                if(c.matches(pattern)) {
                    try {
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.usuario);
                        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                        byte[] bytes = byteArrayOutputStream.toByteArray();

                        Conector con = new Conector();
                        con.crearUsuario(usuario, c, correo, bytes);
                        Mail mail = new Mail();
                        mail.enviarCorreo(correo, usuario);
                        Toast.makeText(this, "Revista tu correo para confirmar la cuenta", Toast.LENGTH_LONG).show();
                        Intent t = new Intent(this, Login.class);
                        startActivity(t);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }else{
                    Toast.makeText(Contrasena.this, "La contraseña no es segura", Toast.LENGTH_LONG).show();
                }
            }
            else{
                Toast.makeText(this, "Las contraseñas no coinciden", Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(this, "Introduce unas contraseña", Toast.LENGTH_LONG).show();
        }
    }

    public void cancelar(View view){
        Intent t = new Intent(this, Login.class);
        startActivity(t);
    }
}