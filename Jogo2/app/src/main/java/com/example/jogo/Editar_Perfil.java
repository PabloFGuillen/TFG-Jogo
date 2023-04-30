package com.example.jogo;

import static com.google.zxing.integration.android.IntentIntegrator.REQUEST_CODE;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class Editar_Perfil extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_perfil);
        ActionBar actionBar = getSupportActionBar();
        actionBar.hide();
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
                Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                intent.setType("image/*");

                // Inicia la actividad de selección de archivo de la galería
                startActivityForResult(Intent.createChooser(intent, "Selecciona una imagen"), REQUEST_CODE);
                imagen.setImageBitmap(Persona.getFotoP());
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == REQUEST_CODE) {
                try {
                    Uri selectedImageUri = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(selectedImageUri);
                    byte[] imageBytes = getBytes(inputStream);
                    Conector con = new Conector();
                    con.cambiarFoto(imageBytes);
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    e.printStackTrace();
                }
            }
        }
    }
    private byte[] getBytes(InputStream inputStream) throws IOException {
        ByteArrayOutputStream byteBuffer = new ByteArrayOutputStream();
        int bufferSize = 1024;
        byte[] buffer = new byte[bufferSize];

        int len;
        while ((len = inputStream.read(buffer)) != -1) {
            byteBuffer.write(buffer, 0, len);
        }

        return byteBuffer.toByteArray();
    }
}