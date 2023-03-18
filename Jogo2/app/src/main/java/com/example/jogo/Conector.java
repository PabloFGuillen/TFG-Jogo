package com.example.jogo;

import android.os.StrictMode;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Time;
import java.util.ArrayList;

public class Conector {
    Connection con;

    public Conector() throws ClassNotFoundException, SQLException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://192.168.1.67:3306/jogo","Android","android");
    }

    public boolean login(String usuario, String contraseña) throws SQLException {
        boolean encontrado = false;
        PreparedStatement ps = con.prepareStatement("SELECT * FROM usuario WHERE (nombre = ? OR email = ?) AND contraseña = ?;");
        ps.setString(1, usuario);
        ps.setString(2, usuario);
        ps.setString(3, contraseña);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            encontrado = true;
        }
        return encontrado;
    }

    public boolean validad(String usuario, String contraseña) throws SQLException {
        boolean encontrado = false;
        PreparedStatement ps = con.prepareStatement("SELECT * FROM usuario WHERE (nombre = ? OR email = ?) AND validado = 1;");
        ps.setString(1, usuario);
        ps.setString(2, usuario);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){
            Persona.setNombreU(rs.getString(1));
            Persona.setCorreo(rs.getString(3));
            Persona.setFotoP(rs.getBytes(4));
            Persona.setDescripcion(rs.getString(5));
            encontrado = true;
        }
        return encontrado;
    }
    public boolean email(String email) throws SQLException {
        boolean encontrado = false;
        PreparedStatement ps = con.prepareStatement("SELECT email FROM usuario WHERE email = ?");
        ps.setString(1, email);
        ResultSet rs = ps.executeQuery();
        if(rs.next()){

            encontrado = true;
        }
        System.out.println("aaaa " + encontrado);
        return encontrado;
    }

    public boolean usuario(String usuario) throws SQLException {
        boolean encontrado = false;
        PreparedStatement ps = con.prepareStatement("SELECT * FROM usuario WHERE nombre = ?");
        ps.setString(1, usuario);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            encontrado = true;
        }
        return encontrado;
    }

    public void crearUsuario(String usuario, String contrasena, String email, byte[] foto) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO usuario(nombre, contraseña, email, validado, foto) VALUES (?,?,?, 0, ?)");
        ps.setString(1, usuario);
        ps.setString(2, contrasena);
        ps.setString(3, email);
        ps.setBytes(4, foto);
        ps.executeUpdate();
    }

    public void crearEvento(String ciudad, String calle, String comunidad, Time hora, Date dia, String nombre, String descripcion, int plazas, String nombreU, byte[] qr, double latitud, double longitud){
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO evento(ciudad, calle, localidad, hora, dia, nombre, descripcion, plazas, nombre_usuario, qr, latitud, longitud) VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)");
            ps.setString(1, ciudad);
            ps.setString(2, calle);
            ps.setString(3, comunidad);
            ps.setTime(4, hora);
            ps.setDate(5, dia);
            ps.setString(6, nombre);
            ps.setString(7, descripcion);
            ps.setInt(8, plazas);
            ps.setString(9, nombreU);
            ps.setBytes(10, qr);
            ps.setDouble(11, latitud);
            ps.setDouble(12, longitud);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public ArrayList<Evento> eventos(int distancia, double latitud, double longitud)  {
        try{
            PreparedStatement ps = con.prepareStatement("SELECT event.*, jogo.usuario.foto, event.plazas - (SELECT count(*) FROM jogo.asistir_evento WHERE jogo.asistir_evento.id = event.id)  AS plazas_disponibles FROM jogo.evento  AS `event` INNER JOIN jogo.usuario ON jogo.usuario.nombre = event.nombre_usuario");
            ResultSet rs = ps.executeQuery();
            ArrayList<Evento> eventos = new ArrayList<Evento>();
            while(rs.next()){
                double latitudE = rs.getDouble(12);
                double longitudE = rs.getDouble(13);
                if(calculateDistance(latitud, longitud, latitudE, longitudE) <= distancia) {
                    Evento evento = new Evento();
                    evento.setId(rs.getInt(1));
                    evento.setCiudad(rs.getString(2));
                    evento.setCalle(rs.getString(3));
                    evento.setLocalidad(rs.getString(4));
                    evento.setHora(rs.getTime(5));
                    evento.setDia(rs.getDate(6));
                    evento.setNombre(rs.getString(7));
                    evento.setDescripcion(rs.getString(8));
                    evento.setPlazas(rs.getInt(9));
                    evento.setNombreU(rs.getString(10));
                    evento.setQr(rs.getBytes(11));
                    evento.setLatitud(rs.getDouble(12));
                    evento.setLongitud(rs.getDouble(13));
                    evento.setFotoU(rs.getBytes(14));
                    evento.setPlazas_disponibles(rs.getInt(15));
                    System.out.println(evento.getCalle());
                    eventos.add(evento);
                }
            }
            return eventos;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    public double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        double RADIUS_OF_EARTH_KM = 6371.01;
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return RADIUS_OF_EARTH_KM * c;
    }

    public void asistirEvento(String usuario, int id){
        try {
            PreparedStatement ps = con.prepareStatement("INSERT INTO asistir_evento VALUES (?, ?)");
            ps.setString(1, usuario);
            ps.setInt(2, id);
            ps.executeUpdate();
        }catch(Exception e){

        }
    }
}
