package com.example.jogo;

import android.content.Context;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.widget.Toast;

import org.checkerframework.checker.units.qual.A;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.util.ArrayList;

public class Conector{
    Connection con;

    public Conector() throws ClassNotFoundException, SQLException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://192.168.1.51:3306/jogo","Android","android");
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
            Persona.setContraseña(rs.getString(2));
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
            PreparedStatement ps = con.prepareStatement("SELECT event.*, jogo.usuario.foto, event.plazas - (SELECT count(*) FROM jogo.asistir_evento WHERE jogo.asistir_evento.id = event.id)  AS plazas_disponibles FROM jogo.evento  AS `event` INNER JOIN jogo.usuario ON jogo.usuario.nombre = event.nombre_usuario LEFT OUTER JOIN jogo.asistir_evento ON jogo.asistir_evento.id = event.id WHERE jogo.asistir_evento.id IS NULL  AND event.nombre_usuario != ?;");
            ps.setString(1, Persona.getNombreU());
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
            Date fechas = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                fechas = Date.valueOf(LocalDate.now().toString());
            }
            PreparedStatement ps = con.prepareStatement("INSERT INTO asistir_evento VALUES (?, ?, ?)");
            ps.setString(1, usuario);
            ps.setInt(2, id);
            ps.setDate(3,fechas );
            ps.executeUpdate();
        }catch(Exception e){

        }
    }

    public void qrEvento(int id){
        try{
            PreparedStatement ps = con.prepareStatement("INSERT INTO asistir_evento VALUES (?, ?)");
            ps.setInt(1, id);
            ps.setString(2, Persona.getNombreU());
            ps.executeUpdate();
        }catch(Exception e){

        }
    }

    public void canceleraE(int id, String nombreU){
        try{
            PreparedStatement ps = con.prepareStatement("DELETE FROM jogo.asistir_evento WHERE id = ? AND nombre = ?");
            ps.setInt(1, id);
            ps.setString(2, nombreU);
            ps.executeUpdate();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public ArrayList<Evento> asistencias(String nombreU){
        try{
            PreparedStatement ps = con.prepareStatement("SELECT `event`.*, jogo.usuario.foto, event.plazas - (SELECT count(*) FROM jogo.asistir_evento WHERE jogo.asistir_evento.id = event.id) \n" +
                    "\tFROM jogo.evento AS `event`\n" +
                    "\t\tINNER JOIN jogo.asistir_evento\n" +
                    "\t\t\tON `event`.id = jogo.asistir_evento.id\n" +
                    "\t\tINNER JOIN jogo.usuario\n" +
                    "\t\t\tON jogo.usuario.nombre = `event`.nombre_usuario\n" +
                    "\t\tWHERE jogo.asistir_evento.nombre = ? ORDER BY fecha_insercion ASC");
            ps.setString(1, nombreU);
            ResultSet rs = ps.executeQuery();
            ArrayList<Evento> eventos = new ArrayList<Evento>();
            while(rs.next()){
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
                eventos.add(evento);
            }
            return eventos;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public String ultimoID(){
        String id;
        try{
            Statement st = con.createStatement();
            ResultSet rs = st.executeQuery("SELECT LAST_INSERT_ID()");
            while(rs.next()){
                id = String.valueOf(rs.getInt(1)+1);
                return id;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public ArrayList<Evento> buscarNombre(String busqueda, int distancia, double latitud, double longitud) {
        try {
            PreparedStatement ps = con.prepareStatement("SELECT event.*, jogo.usuario.foto, event.plazas - (SELECT count(*) FROM jogo.asistir_evento WHERE jogo.asistir_evento.id = event.id)  AS plazas_disponibles FROM jogo.evento  AS `event` INNER JOIN jogo.usuario ON jogo.usuario.nombre = event.nombre_usuario LEFT OUTER JOIN jogo.asistir_evento ON jogo.asistir_evento.id = event.id WHERE jogo.asistir_evento.id IS NULL  AND event.nombre_usuario != ? AND event.nombre LIKE ?;");
            ps.setString(1, Persona.getNombreU());
            ps.setString(2, '%'+busqueda+'%');
            ResultSet rs = ps.executeQuery();
            ArrayList<Evento> eventos = new ArrayList<Evento>();
            while (rs.next()) {
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
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Evento> buscarUbicacion(String busqueda)  {
        try{
            PreparedStatement ps = con.prepareStatement("SELECT event.*, jogo.usuario.foto, event.plazas - (SELECT count(*) FROM jogo.asistir_evento WHERE jogo.asistir_evento.id = event.id)  AS plazas_disponibles FROM jogo.evento  AS `event` INNER JOIN jogo.usuario ON jogo.usuario.nombre = event.nombre_usuario LEFT OUTER JOIN jogo.asistir_evento ON jogo.asistir_evento.id = event.id WHERE jogo.asistir_evento.id IS NULL  AND event.nombre_usuario != ? AND (event.calle LIKE ? OR event.ciudad LIKE ? OR event.localidad LIKE ?);");
            ps.setString(1, Persona.getNombreU());
            ps.setString(2, '%'+busqueda+'%');
            ps.setString(3, '%'+busqueda+'%');
            ps.setString(4, '%'+busqueda+'%');
            ResultSet rs = ps.executeQuery();
            ArrayList<Evento> eventos = new ArrayList<Evento>();
            while(rs.next()){
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
            return eventos;
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public ArrayList<Cuenta> buscarUsuario(String busqueda){
        try{
            ArrayList<Cuenta> cuentas = new ArrayList<Cuenta>();
            PreparedStatement ps = con.prepareStatement("SELECT usuario.nombre, usuario.email, usuario.foto, usuario.descripcion FROM usuario WHERE usuario.nombre LIKE ?");
            ps.setString(1, '%'+busqueda+'%');
            ResultSet rs = ps.executeQuery();
            while(rs.next()){
                Cuenta cuenta = new Cuenta();
                cuenta.setNombreU(rs.getString(1));
                cuenta.setCorreo(rs.getString(2));
                cuenta.setFotoP(rs.getBytes(3));
                cuenta.setDescripcion(rs.getString(4));
                cuentas.add(cuenta);
            }
            return cuentas;
        }catch(Exception e){

        }
        return null;
    }


    public ArrayList<Evento> getEventos(){


        try{
            PreparedStatement ps = con.prepareStatement("SELECT event.*, jogo.usuario.foto, event.plazas - (SELECT count(*) FROM jogo.asistir_evento WHERE jogo.asistir_evento.id = event.id)  AS plazas_disponibles FROM jogo.evento  AS `event` INNER JOIN jogo.usuario ON jogo.usuario.nombre = event.nombre_usuario LEFT OUTER JOIN jogo.asistir_evento ON jogo.asistir_evento.id = event.id WHERE event.nombre_usuario = ?;");
            ps.setString(1, Persona.getNombreU());
            ResultSet rs = ps.executeQuery();
            ArrayList<Evento> eventos = new ArrayList<Evento>();
            while(rs.next()){
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
            return eventos;

            } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return null;

    }


    public void eliminarEvento(Evento evento){
        String sql ="DELETE FROM evento WHERE (evento.id = ?)";
        try {
            PreparedStatement ps = con.prepareStatement(sql);
            ps.setInt(1, evento.getId());
            ps.executeUpdate();

        } catch (SQLException e) {
            System.out.println("===========================ERROR - CLASE CONECTOR========================");
            System.out.println(e.getMessage());
        }finally {
            System.out.println("=======================TODO BIEN ===========================");
        }

    }

    public ArrayList<Evento> eventoPerfil(String perfil){
        try {
            PreparedStatement ps = con.prepareStatement("SELECT event.*, jogo.usuario.foto, event.plazas - (SELECT count(*) FROM jogo.asistir_evento WHERE jogo.asistir_evento.id = event.id)  AS plazas_disponibles FROM jogo.evento  AS `event` INNER JOIN jogo.usuario ON jogo.usuario.nombre = event.nombre_usuario LEFT OUTER JOIN jogo.asistir_evento ON jogo.asistir_evento.id = event.id WHERE  event.nombre_usuario = ?;");
            ps.setString(1, perfil);
            ResultSet resultSet = ps.executeQuery();
            ArrayList<Evento> eventos = new ArrayList<Evento>();
            while(resultSet.next()){
                Evento evento = new Evento();
                evento.setNombre(resultSet.getString(1));
                evento.setCalle(resultSet.getString(2));
                evento.setCiudad(resultSet.getString(3));
                evento.setLocalidad(resultSet.getString(4));
                evento.setDia(resultSet.getDate(5));
                evento.setHora(resultSet.getTime(6));
                evento.setDescripcion(resultSet.getString(7));
                evento.setFotoU(resultSet.getBytes(8));
                eventos.add(evento);
            }
            return eventos;
        }catch(Exception e){
            return null;
        }
    }

    public void asistirEventoP(int id){
        try{
            PreparedStatement ps = con.prepareStatement("INSER INTO asistir_evento (nombre, id, fecha_insercion) VALUES (?, ?, ?)");
            ps.setString(1, Persona.getNombreU());
            ps.setInt(2, id);
            Date fechas = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                fechas = Date.valueOf(LocalDate.now().toString());
            }
            ps.setDate(3, fechas);
            ps.executeUpdate();
        }catch(Exception e){

        }
    }

    public void cambiarNombre(String nombre){
        try{
            PreparedStatement ps = con.prepareStatement("UPDATE FROM jogo.usuario SET jogo.usuario.nombre = ? WHERE jogo.usuario.nombre = ?");
            ps.setString(1, nombre);
            ps.setString(2, Persona.getNombreU());
            ps.executeUpdate();
            Persona.setNombreU(nombre);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void cambiarDescripcion(String descripcion){
        try{
            PreparedStatement ps = con.prepareStatement("UPDATE FROM jogo.usuario SET jogo.usuario.descripcion = ? WHERE jogo.usuario.nombre = ?");
            ps.setString(1, descripcion);
            ps.setString(2, Persona.getNombreU());
            ps.executeUpdate();
            Persona.setDescripcion(descripcion);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void cambiarFoto(byte[] foto){
        try{
            PreparedStatement ps = con.prepareStatement("UPDATE FROM jogo.usuario SET jogo.usuario.foto = ? WHERE jogo.usuario.nombre = ?");
            ps.setBytes(1, foto);
            ps.setString(2, Persona.getNombreU());
            ps.executeUpdate();
            Persona.setFotoP(foto);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public void seguir(String nombre){
        try{
            PreparedStatement ps = con.prepareStatement("INSERT INTO seguidores(nombre, nombre_seguidor) VALUES (?, ?);");
            ps.setString(1,nombre);
            ps.setString(2, Persona.getNombreU());
            ps.executeUpdate();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public boolean comprobarSeguidor(String nombre){
        try{
            PreparedStatement ps = con.prepareStatement("SELECT * FROM seguidores WHERE seguidores.nombre = ? AND seguidores.nombre_seguidor = ?");
            ps.setString(1,nombre);
            ps.setString(2, Persona.getNombreU());
            ResultSet rs = ps.executeQuery();
            if(rs == null){
                return false;
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return true;
    }

    public boolean eliminarSiguiendo(String nombre){
        try{
            PreparedStatement ps = con.prepareStatement("DELETE FROM seguidores WHERE seguidores.nombre = ? AND seguidores.nombre_seguidor = ?");
            ps.setString(1,nombre);
            ps.setString(2, Persona.getNombreU());
            ps.executeUpdate();
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return true;
    }


}
