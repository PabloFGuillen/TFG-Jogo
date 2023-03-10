package com.example.jogo;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;

public class ComprobacionNominatum {
    private String ubicacion;

    public ComprobacionNominatum(String calle, String ciudad){
        this.ubicacion = calle + " ," + ciudad;
    }
    public boolean  comprobar() throws IOException {

        // Hacer una solicitud HTTP a la API de búsqueda inversa de Nominatim
        URL url = new URL("https://nominatim.openstreetmap.org/search?q=" + URLEncoder.encode(ubicacion, "UTF-8") + "&format=json");
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");

        int responseCode = con.getResponseCode();
        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(
                    con.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Verifica si se encontró una ubicación
            if (response.toString().contains("\"lat\"")) {
                System.out.println("Aquí: " + response.toString());
                return true;
            } else {
                System.out.println("Aquí: " + response.toString());
                return false;
            }
        } else {
            return false;
        }
    }
}
