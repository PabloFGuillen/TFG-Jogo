package com.example.jogo.ui;


import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.media.Image;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.provider.Settings;
import android.transition.Fade;
import android.transition.Transition;
import android.view.KeyCharacterMap;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.jogo.Adaptador;
import com.example.jogo.ComprobacionNominatum;
import com.example.jogo.Conector;
import com.example.jogo.DatePickerFragment;
import com.example.jogo.Evento;
import com.example.jogo.MainActivity;
import com.example.jogo.Persona;
import com.example.jogo.R;
import com.example.jogo.fragmento_busqueda;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.navigation.NavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.journeyapps.barcodescanner.BarcodeEncoder;



import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.sql.Date;
import java.sql.SQLException;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.StringTokenizer;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EventoF#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class EventoF extends Fragment {

    /* Fragment que sirve para ver los eventos cerca de la ubicación así como crear evento*/
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
    private ListView listView;
    private List<Evento> lista;
    private Transition mFadeTransition = new Fade();
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
        Transition mFadeTransition = new Fade();

        //Tenemos 3 hilos distintos para aminorar la carga de trabajo del hilo principañ

        //Primer hilo
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TAB HOST

                // Especificamos los tabs que van a existirs, así como sus nombres
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
                // Aquí lo que hacemos es cambiar los colores que pasan al seleccionar los tabs
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
            }
        });

        getActivity()
                .runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Cargando");
                progressDialog.setCancelable(false);
                progressDialog.show();

                // FORMULARIO CREAR EVENTO
                ImageView fotoPerfil = (ImageView) view.findViewById(R.id.fotoCE);
                EditText nombreE = (EditText) view.findViewById(R.id.nombreE);
                EditText calleE = (EditText) view.findViewById(R.id.calleE);
                EditText localidadE = (EditText) view.findViewById(R.id.localidadE);
                EditText comunidad = (EditText) view.findViewById(R.id.comunidadE);
                EditText plazasE = (EditText) view.findViewById(R.id.plazasE);
                EditText descripcionE = (EditText) view.findViewById(R.id.descripcionE);

                fecha = (TextView) view.findViewById(R.id.fechaE);
                hora = (TextView) view.findViewById(R.id.horaE);
                creador = (TextView) view.findViewById(R.id.creadorE);

                // Esto es para poder mostrar el ombre del usuario.
                fotoPerfil.setImageBitmap(Persona.getFotoP());
                creador.setText(String.valueOf(Persona.getNombreU()));

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
                progressDialog.dismiss();
                // Menú para escoger fecha
                TextView etPlannedDate = (TextView) view.findViewById(R.id.fechaE);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
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
                    }
                }).start();


                // Menú para escoger hora.
                hora.setOnClickListener(new View.OnClickListener() {
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
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        // Aquí se realiza la tarea que se quiere ejecutar en segundo plano
                        // Esta tarea no puede interactuar directamente con la interfaz de usuario
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
                                                Time horas = Time.valueOf(hora.getText().toString() + ":00");
                                                Date fechas = Date.valueOf(fechaSQL);
                                                LocalDate fecha = null;
                                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                                                    fecha = LocalDate.parse(fechaSQL);
                                                    if(fecha.isBefore(LocalDate.now())){
                                                        Toast.makeText(getContext(), "", Toast.LENGTH_LONG).show();
                                                    }
                                                    else{
                                                        Conector con = new Conector();
                                                        double[] coordenadas = comprobacionNominatum.latitudL(localidadE.getText().toString());
                                                        //Esto genera el código QR
                                                        BarcodeEncoder barcodeEncoder = new BarcodeEncoder();

                                                        // StringTokenizer con separación '|'
                                                        Bitmap qr = barcodeEncoder.encodeBitmap("http://localhost/Jogo/Link-evento.php?idEvento="+con.ultimoID(), BarcodeFormat.QR_CODE, 400, 400);

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
                                                    }
                                                }


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
                    }
                }).start();
            }
        });
        getActivity()
                .runOnUiThread(new Runnable() {
            @Override
            public void run() {
                // TAB DE EVENTOS EN TU ZONA

                // Inicializamos variables
                ProgressDialog progressDialog = new ProgressDialog(getContext());
                progressDialog.setMessage("Cargando Eventos");
                progressDialog.setCancelable(false);

                TextView ubicacion = (TextView) view.findViewById(R.id.km);
                SeekBar distancia = (SeekBar) view.findViewById(R.id.distancia);
                ubicacion.setText(String.valueOf(distancia.getProgress()));


                listView = (ListView) view.findViewById(R.id.Listview);
                Button actualizar = (Button) view.findViewById(R.id.actualizar);


                // Esta funcion muestra eventos según la distancia de radio especificada.
                GPS(view, distancia.getProgress());

                progressDialog.dismiss();

                actualizar.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        progressDialog.show();
                        GPS(view, distancia.getProgress());
                        progressDialog.dismiss();
                    }
                });


                // Aquí hacemos tareas dependiendo de la barra.
                new Thread(new Runnable() {
                    @Override
                    public void run() {

                        EditText buscar = (EditText) view.findViewById(R.id.buscar);
                        FrameLayout frameF = (FrameLayout) view.findViewById(R.id.frameF);
                        ImageView volver = (ImageView) view.findViewById(R.id.volver);
                        volver.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                frameF.setVisibility(View.GONE);
                                volver.setVisibility(View.GONE);
                                listView.setVisibility(View.VISIBLE);
                            }
                        });
                        buscar.setOnKeyListener(new View.OnKeyListener() {
                            @Override
                            public boolean onKey(View view, int i, KeyEvent keyEvent) {
                                if((keyEvent.getAction() == KeyEvent.ACTION_DOWN)){
                                    FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                                    fragmento_busqueda busqueda = new fragmento_busqueda();
                                    busqueda.setBusqueda(buscar.getText().toString());
                                    busqueda.setDistancia(distancia.getProgress());
                                    FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                    fragmentTransaction.replace(R.id.frameF, busqueda);
                                    fragmentTransaction.commit();
                                    frameF.setVisibility(View.VISIBLE);
                                    volver.setVisibility(View.VISIBLE);
                                    listView.setVisibility(View.GONE);
                                    KeyCharacterMap keyCharacterMap = KeyCharacterMap.load(KeyCharacterMap.VIRTUAL_KEYBOARD);
                                    char character = (char) keyCharacterMap.get(i, 0);
                                    String c = String.valueOf(character);
                                    Toast.makeText(getContext(), String.valueOf(i), Toast.LENGTH_SHORT).show();
                                }
                                return false;
                            }
                        });

                        distancia.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                            @Override
                            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                                ubicacion.setText(String.valueOf(distancia.getProgress()));
                            }

                            @Override
                            public void onStartTrackingTouch(SeekBar seekBar) {

                            }

                            @Override
                            public void onStopTrackingTouch(SeekBar seekBar) {
                                progressDialog.show();
                                GPS(view, distancia.getProgress());
                                progressDialog.dismiss();
                            }
                        });
                    }
                }).start();
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

    public void onActivityResult(int requestCode, int resultCode, Intent data){
        // Esto es el escaner de qr que tenemos implementado. Escaneará el qr y te unirá automaticamente al evento.
        super.onActivityResult(requestCode, resultCode, data);
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        String datos = result.getContents();
        //Aqui lanzamos a un activity pa mostar el evento escaneado.
        try {
            Conector con = new Conector();
            con.qrEvento(Integer.parseInt(datos));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void GPS(View view, int distancia){
        // Aquí pedimos permisos al usuario para acceder a la ubicación del gps.
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

        // Aquí recogemos altitud y latitud.
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if(isGpsEnabled == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
            builder.setMessage("Para poder utilizar esta función, debes activar el GPS. ¿Deseas hacerlo ahora?");
            builder.setPositiveButton("Sí", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(intent);
                }
            });
            builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                   Toast.makeText(getContext(), "Para usar esta funcionalidad hay que activar el GPS", Toast.LENGTH_LONG).show();
                   Intent t = new Intent(getContext(), MainActivity.class);
                   startActivity(t);
                }
            });
            AlertDialog dialog = builder.create();
            dialog.show();

        }

        else
        {
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationClient.getLastLocation()
            .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    try {
                        latitud = location.getLatitude();
                        longitud = location.getLongitude();
                        Conector con = new Conector();
                        // Recogemos eventos en un arraylist segund la distancia max, la latitud y la longitud.
                        lista = con.eventos(distancia, latitud, longitud);
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
        }
    }
}