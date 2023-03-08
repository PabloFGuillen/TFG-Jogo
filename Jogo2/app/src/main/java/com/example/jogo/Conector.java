package com.example.jogo;

import android.os.StrictMode;
import android.widget.Toast;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Conector {
    Connection con;

    public Conector() throws ClassNotFoundException, SQLException {
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);
        Class.forName("com.mysql.jdbc.Driver");
        con = DriverManager.getConnection("jdbc:mysql://192.168.81.110:3306/jogo","Android","android");
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
        PreparedStatement ps = con.prepareStatement("SELECT nombre FROM usuario WHERE nombre = ?");
        ps.setString(1, usuario);
        ResultSet rs = ps.executeQuery();
        if(rs.next()) {
            encontrado = true;
        }
        return encontrado;
    }

    public void crearUsuario(String usuario, String contrasena, String email) throws SQLException {
        PreparedStatement ps = con.prepareStatement("INSERT INTO usuario(nombre, contraseña, email, validado) VALUES (?,?,?, 0)");
        ps.setString(1, usuario);
        ps.setString(2, contrasena);
        ps.setString(3, email);
        ps.executeUpdate();
    }

    public String nombreUsuario(String usuario, String contrasena) throws SQLException {
        PreparedStatement ps = con.prepareStatement("SELECT nombre FROM usuario WHERE (nombre = ? OR email = ?) AND contraseña = ?");
        ps.setString(1, usuario);
        ps.setString(2, usuario);
        ps.setString(2, contrasena);
        String usua = "";
        ResultSet rs = ps.executeQuery();
        while(rs.next()){
            usua = rs.getString(1);
        }

        return usua;
    }

}