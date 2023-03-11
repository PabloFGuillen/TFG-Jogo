package com.example.jogo.ui;


import android.Manifest;
import android.app.DatePickerDialog;
import android.app.Notification;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jogo.Adaptador;
import com.example.jogo.ComprobacionNominatum;
import com.example.jogo.Conector;
import com.example.jogo.DatePickerFragment;
import com.example.jogo.Evento;
import com.example.jogo.Persona;
import com.example.jogo.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.zxing.BarcodeFormat;
import com.journeyapps.barcodescanner.BarcodeEncoder;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventoF#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class EventoF extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private TextView fecha;
    private TextView hora;
    private TextView creador;
    private String fechaSQL;
    private FusedLocationProviderClient fusedLocationClient;
    int hour = 0, minute = 0;
    double longitud, latitud;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    // TODO: Rename and change types and number of parameters
    public static EventoF newInstance(String param1, String param2) {
        EventoF fragment = new EventoF();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public EventoF() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.fragment_evento,
                container, false);
        // TAB HOST
        TabHost tabHost = (TabHost) view.findViewById(R.id.tab);
        tabHost.setup();
        TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1");
        tab1.setIndicator("EVENTO");
        tab1.setContent(R.id.eventoF);
        TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2");
        tab2.setIndicator("CREAR");
        tab2.setContent(R.id.crear);
        tabHost.addTab(tab1);
        tabHost.addTab(tab2);

        tabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener() {

            @Override
            public void onTabChanged(String tabId) {

                for (int i = 0; i < tabHost.getTabWidget().getChildCount(); i++) {
                    tabHost.getTabWidget().getChildAt(i).setBackgroundColor(Color.parseColor("#FFFBD9")); // unselected
                    TextView tv = (TextView) tabHost.getTabWidget().getChildAt(i).findViewById(android.R.id.title); //Unselected Tabs
                    tv.setTextColor(Color.parseColor("#C6C6C6"));
                }

                tabHost.getTabWidget().getChildAt(tabHost.getCurrentTab()).setBackgroundColor(Color.parseColor("#FFF1C0")); // selected
                TextView tv = (TextView) tabHost.getCurrentTabView().findViewById(android.R.id.title); //for Selected Tab
                tv.setTextColor(Color.parseColor("#6F6A6A"));
            }
        });
        tabHost.setCurrentTab(1);

        // FORMULARIO CREAR EVENTO
        ImageView fotoPerfil = (ImageView) view.findViewById(R.id.fotoCE);
        EditText nombreE = (EditText) view.findViewById(R.id.nombreE);
        EditText calleE = (EditText) view.findViewById(R.id.calleE);
        EditText localidadE = (EditText) view.findViewById(R.id.localidadE);
        EditText comunidad = (EditText) view.findViewById(R.id.comunidadE);
        EditText plazasE = (EditText) view.findViewById(R.id.plazasE);
        EditText descripcionE = (EditText) view.findViewById(R.id.descripcionE);
        this.fecha = (TextView) view.findViewById(R.id.fechaE);
        this.hora = (TextView) view.findViewById(R.id.horaE);
        this.creador = (TextView) view.findViewById(R.id.creadorE);

        // Esto es para poder mostrar el ombre del usuario.
        fotoPerfil.setImageBitmap(Persona.getFotoP());
        this.creador.setText(String.valueOf(Persona.getNombreU()));

        // MOSTRAMOS LA FECHA Y HORA ACTUAL.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            int anio, mes, dia;
            dia = LocalDate.now().getDayOfMonth();
            mes = LocalDate.now().getMonthValue();
            anio = LocalDate.now().getYear();
            fechaSQL = String.valueOf(anio) + "-" + String.valueOf(mes) + "-" + String.valueOf(dia);
            fecha.setText(String.valueOf(dia) + "/" +
                    String.valueOf(mes) + "\n" +
                    String.valueOf(anio));
            int mi = Integer.parseInt(String.valueOf(LocalDateTime.now().getMinute()));
            String minuto = "";
            if (mi >= 0 && mi <= 9) {
                minuto = "0" + String.valueOf(mi);
            } else {
                minuto = String.valueOf(LocalDateTime.now().getMinute());
            }
            hora.setText(String.valueOf(LocalDateTime.now().getHour()) + ":" + minuto);
        }

        // Menú para escoger fecha
        TextView etPlannedDate = (TextView) view.findViewById(R.id.fechaE);
        etPlannedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.fechaE:
                        showDatePickerDialog();
                        break;
                }
            }
        });

        // Menú para escoger hora.
        this.hora.setOnClickListener(new View.OnClickListener() {
                                         @Override
                                         public void onClick(View view) {
                                             TimePickerDialog.OnTimeSetListener onTimeSetListener = new TimePickerDialog.OnTimeSetListener() {
                                                 @Override
                                                 public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                                                     hour = selectedHour;
                                                     minute = selectedMinute;
                                                     hora.setText(String.valueOf(hour) + ":" + String.valueOf(minute));
                                                 }
                                             };
                                             TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), onTimeSetListener, hour, minute, true);
                                             timePickerDialog.setTitle("Select Time");
                                             timePickerDialog.show();
                                         }
                                     }
        );

        // Botón para crear el evento.
        Button crearE = (Button) view.findViewById(R.id.crearE);
        crearE.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Nos aseguramos de que todos los campos estan rellenos
                if (nombreE.getText().toString().equals("") == false && calleE.getText().toString().equals("") == false && localidadE.getText().toString().equals("") == false && comunidad.getText().toString().equals("") == false && descripcionE.getText().toString().equals("") == false) {
                    // Nos aseguramos de que la ubicación escrita existe.
                    ComprobacionNominatum comprobacionNominatum = new ComprobacionNominatum(calleE.getText().toString(), localidadE.getText().toString());
                    boolean comprobacion = false;

                    try {
                        //Si existe, registramos todo en la base datos
                        comprobacion = comprobacionNominatum.comprobar();
                        if (comprobacion == true) {
                            try {
                                // Aquí pasamos a los atributos Time y Date propios de SQL para poder guardarlos en la base de datos
                                Time horas = java.sql.Time.valueOf(hora.getText().toString() + ":00");
                                Date fechas = java.sql.Date.valueOf(fechaSQL);
                                Conector con = new Conector();
                                double[] coordenadas = comprobacionNominatum.latitudL(localidadE.getText().toString());
                                //Esto genera el código QR
                                BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

                                // StringTokenizer con separación '|'
                                Bitmap qr = barcodeEncoder.encodeBitmap(localidadE.getText().toString() + "|" + calleE.getText().toString() + "|" + comunidad.getText().toString() + "|" + horas + "|" + fechas + "|" + nombreE.getText().toString() + "|" + descripcionE.getText().toString() + "|" + Integer.parseInt(plazasE.getText().toString()) + "|" + Persona.getNombreU()+"|"+String.valueOf(coordenadas[0])+"|"+String.valueOf(coordenadas[1]), BarcodeFormat.QR_CODE, 400, 400);

                                //Pasamos de bitmap a longblob
                                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                                qr.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                                byte[] byteQR = byteArrayOutputStream.toByteArray();

                                //Insertamos toda la información en la base de datos.
                                con.crearEvento(localidadE.getText().toString(), calleE.getText().toString(), comunidad.getText().toString(), horas, fechas, nombreE.getText().toString(), descripcionE.getText().toString(), Integer.parseInt(plazasE.getText().toString()), Persona.getNombreU(), byteQR, coordenadas[0], coordenadas[1]);
                                Toast.makeText(getContext(), "Evento Creado Correctamente", Toast.LENGTH_LONG).show();
                                localidadE.setText("");
                                calleE.setText("");
                                comunidad.setText("");
                                plazasE.setText("");
                                descripcionE.setText("");


                            } catch (Exception e) {
                                System.out.println(e.getMessage());
                            }
                        }
                        // En caso contrario, decimos que dicha ubicación no exiset
                        else {
                            Toast.makeText(getContext(), "La ubicación no existe", Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
                // Si no se han rellenado todos los campos obligatorios, pedimos por pantalla que los rellenes
                else {
                    Toast.makeText(getContext(), "Por favor, rellene los campos obligatorios", Toast.LENGTH_LONG).show();
                }
            }
        });
        // TAB DE EVENTOS EN TU ZONA
            //Pedimos permisos de acceder a la ubicación del móvil.
            int MY_PERMISSIONS = 0;
            int permisoSMS = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            if (permisoSMS != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION},
                        MY_PERMISSIONS);
            }
            permisoSMS = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION);
            if(permisoSMS != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{
                                android.Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS);
            }

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            try {
                                latitud = location.getLatitude();
                                longitud = location.getLongitude();
                                ListView listView = (ListView) view.findViewById(R.id.Listview);
                                Conector con = new Conector();
                                List<Evento> lista = con.eventos();
                                Adaptador adaptadorEjemplo = new Adaptador(
                                        getContext(),
                                        R.layout.evento_item,
                                        lista
                                );
                                listView.setAdapter(adaptadorEjemplo);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }
                    });
        return view;
    }



    // Esta funcion es inbocada par apoder realizar la toma de la fecha.
    private void showDatePickerDialog() {
        DatePickerFragment newFragment = DatePickerFragment.newInstance(new DatePickerDialog.OnDateSetListener() {
            @Override

            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                // +1 because January is zero
                final String selectedDate = day + "/" + (month+1) + "\n " + year;
                fecha.setText(selectedDate);
                fechaSQL = String.valueOf(year)+"-"+String.valueOf(month)+"-"+String.valueOf(day);
            }
        });

        newFragment.show(getActivity().getSupportFragmentManager(), "datePicker");
    }

}