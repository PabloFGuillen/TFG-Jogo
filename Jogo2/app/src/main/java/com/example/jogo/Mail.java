package com.example.jogo;

import android.os.StrictMode;

import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class Mail {
    /*Esta es la clase encargada de enviar un correo a la cuenta especificada en el momento de la creación de la cuenta
    para la aplicación. Le damos un correo, y una contraseña y envia un mail usando dicho correo a la cuenta
    especificada */
    String correo;
    String contraseña;
    Session session;
    Properties properties;

    public Mail(){
        correo = "jogoeventsapp@gmail.com";
        contraseña = "qmffbyrujcxvandb";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        properties = new Properties();
        properties.put("mail.smtp.host", "smtp.googlemail.com");
        properties.put("mail.smtp.ssl.trust", "smtp.gmail.com");
        properties.put("mail.smtp.starttls", "true");
        properties.put("mail.smtp.socketFactory.port", "465");
        properties.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory");
        properties.put("mail.smtp.auth", "true");
        properties.put("mail.smtp.port", "465");
        try{
            session = javax.mail.Session.getDefaultInstance(properties, new Authenticator() {
                @Override
                protected PasswordAuthentication getPasswordAuthentication() {
                    return new PasswordAuthentication(correo, contraseña);
                }
            });
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void enviarCorreo(String destinatario, String usuario) throws MessagingException {
        if(session != null){
            Message message = new MimeMessage(session);
            message.setFrom(new InternetAddress(correo));
            message.setSubject("Confirmación de cuenta");
            message.setRecipients(Message.RecipientType.TO,InternetAddress.parse(destinatario));
            message.setContent("¡BIENVENIDO A JOGO! \nPulsa sobre el siguiente link para confirmar la cuenta \n http://localhost/Jogo/confirmar.php?usuario="+usuario,"text/html; charset=utf-8");
            Transport.send(message);
        }
    }
}
