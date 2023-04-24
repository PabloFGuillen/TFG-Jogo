package com.example.jogo;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;

public class Cuenta {
    private String nombreU;
    private String descripcion;
    private byte[] fotoP;
    private String correo;

    public Cuenta(){

    }

    public Cuenta(String nombreU, String descripcion, byte[] fotoP, String correo) {
        this.nombreU = nombreU;
        this.descripcion = descripcion;
        this.fotoP = fotoP;
        this.correo = correo;
    }

    public String getNombreU() {
        return nombreU;
    }

    public void setNombreU(String nombreU) {
        this.nombreU = nombreU;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public Bitmap getFotoP() {
        Bitmap bitmap = BitmapFactory.decodeByteArray(fotoP, 0, fotoP.length);
        Bitmap transparentBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(transparentBitmap);
        Paint paint = new Paint();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return bitmap;
    }

    public void setFotoP(byte[] fotoP) {
        this.fotoP = fotoP;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public byte[] getBytes() {
        return this.fotoP;
    }
}
