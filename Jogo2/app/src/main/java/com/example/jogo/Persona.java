package com.example.jogo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class Persona {
    private static String nombreU;
    private  static String descripcion;
    private static byte[] fotoP;
    private  static String correo;

    public static String getNombreU() {
        return nombreU;
    }

    public static void setNombreU(String nombreU) {
        Persona.nombreU = nombreU;
    }

    public static String getDescripcion() {
        return descripcion;
    }

    public static void setDescripcion(String descripcion) {
        Persona.descripcion = descripcion;
    }

    public static Bitmap getFotoP() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(fotoP, 0, fotoP.length);
        Bitmap transparentBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(transparentBitmap);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bitmap;
    }

    public static void setFotoP(byte[] fotoP) {
        Persona.fotoP = fotoP;
    }

    public static String getCorreo() {
        return correo;
    }

    public static void setCorreo(String correo) {
        Persona.correo = correo;
    }
}
