package com.example.jogo;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLConnection;
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

    public double[] latitudL(String ubicacion){
        try {
            // Codificar la dirección a una URL válida para la API de OpenStreetMap.
            String direccion = URLEncoder.encode(ubicacion, "UTF-8");

            // Construir la URL de la solicitud a la API de OpenStreetMap.
            String url = "https://nominatim.openstreetmap.org/search?q=" + direccion + "&format=json&limit=1";

            // Realizar la solicitud HTTP a la API de OpenStreetMap.
            URLConnection conn = new URL(url).openConnection();
            conn.connect();

            // Leer la respuesta de la API de OpenStreetMap.
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder response = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Analizar la respuesta de la API de OpenStreetMap.
            JSONArray jsonArray = new JSONArray(response.toString());
            if (jsonArray.length() > 0) {
                // Obtener la latitud y longitud del primer resultado.
                JSONObject location = jsonArray.getJSONObject(0);
                double latitud = location.getDouble("lat");
                double longitud = location.getDouble("lon");

                // Imprimir la latitud y longitud del lugar especificado por el usuario.
                double[] coordenadas = {latitud, longitud};
                return coordenadas;
            }
        } catch (IOException | JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
