package com.example.jogo;

import android.Manifest;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.sql.SQLException;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link fragmento_busqueda#newInstance} factory method to
 * create an instance of this fragment.
 */
public class fragmento_busqueda extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String busqueda;
    private Double latitud;
    private Double longitud;
    private int distancia;
    ListView listViewUs;
    ListView listViewUb;
    ListView listViewEv;
    private FusedLocationProviderClient fusedLocationClient;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public fragmento_busqueda() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment fragmento_busqueda.
     */
    // TODO: Rename and change types and number of parameters
    public static fragmento_busqueda newInstance(String param1, String param2) {
        fragmento_busqueda fragment = new fragmento_busqueda();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
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
        View view = inflater.inflate(R.layout.fragment_busqueda, container, false);


        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                TabHost tabHost = (TabHost) view.findViewById(R.id.tabF);
                tabHost.setup();
                TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1");
                tab1.setIndicator("USUARIOS");
                tab1.setContent(R.id.Usuario);
                TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2");
                tab2.setIndicator("UBICACIONES");
                tab2.setContent(R.id.Ubicaciones);
                TabHost.TabSpec tab3 = tabHost.newTabSpec("tab3");
                tab3.setIndicator("EVENTOS");
                tab3.setContent(R.id.Eventos);
                tabHost.addTab(tab1);
                tabHost.addTab(tab2);
                tabHost.addTab(tab3);
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
        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {

                GPSEvento(view, distancia);
            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GPSUbicacion(view);
            }
        });

        getActivity().runOnUiThread(new Runnable() {
            @Override
            public void run() {
                GPSUsuario(view);
            }
        });
        return view;
    }

    public void setBusqueda(String busqueda){
        this.busqueda = busqueda;
    }

    public void GPSEvento(View view, int distancia){
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
        if (permisoSMS != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS);
        }

        // Aquí recogemos altitud y latitud.
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isGpsEnabled == false) {
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
        else{
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            try {
                                listViewEv = (ListView) view.findViewById(R.id.LVEvento);

                                latitud = location.getLatitude();
                                longitud = location.getLongitude();
                                if(latitud != null && longitud != null) {
                                    Conector con = new Conector();
                                    ArrayList<Evento> lista = con.buscarNombre(busqueda, distancia, latitud, longitud);
                                    Adaptador adaptadorEjemplo = new Adaptador(
                                            getContext(),
                                            R.layout.evento_item,
                                            lista
                                    );
                                    listViewEv.setAdapter(adaptadorEjemplo);
                                }
                            } catch (ClassNotFoundException e) {

                            } catch (SQLException e) {

                            }
                        }
                    });
        }
    }

    public void GPSUbicacion(View view){
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
        if (permisoSMS != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS);
        }

        // Aquí recogemos altitud y latitud.
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isGpsEnabled == false) {
            GPSEvento(view, distancia);
            Toast.makeText(getContext(), "Activa el GPS para buscar eventos.", Toast.LENGTH_SHORT).show();
        }

        else{
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            try {
                                ListView listViewUb = (ListView) view.findViewById(R.id.LVUbicacion);
                                latitud = location.getLatitude();
                                longitud = location.getLongitude();
                                Conector con = new Conector();
                                ArrayList<Evento> lista = con.buscarUbicacion(busqueda);
                                Adaptador adaptadorEjemplo = new Adaptador(
                                        getContext(),
                                        R.layout.evento_item,
                                        lista
                                );
                                listViewUb.setAdapter(adaptadorEjemplo);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }

    public void GPSUsuario(View view){
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
        if (permisoSMS != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{
                            android.Manifest.permission.ACCESS_COARSE_LOCATION},
                    MY_PERMISSIONS);
        }

        // Aquí recogemos altitud y latitud.
        LocationManager locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);
        boolean isGpsEnabled = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);

        if (isGpsEnabled == false) {
            GPSEvento(view, distancia);
            Toast.makeText(getContext(), "Activa el GPS para buscar eventos.", Toast.LENGTH_SHORT).show();
        }
        else{

            fusedLocationClient = LocationServices.getFusedLocationProviderClient(getContext());
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(getActivity(), new OnSuccessListener<Location>() {
                        @Override
                        public void onSuccess(Location location) {
                            try {
                                ListView listViewUs = (ListView) view.findViewById(R.id.LVUsuario);
                                latitud = location.getLatitude();
                                longitud = location.getLongitude();
                                Conector con = new Conector();
                                ArrayList<Cuenta> lista = con.buscarUsuario(busqueda);
                                AdaptadorUsuario adaptadorEjemplo = new AdaptadorUsuario(
                                        getContext(),
                                        R.layout.usuario_item,
                                        lista
                                );
                                listViewUs.setAdapter(adaptadorEjemplo);
                            } catch (ClassNotFoundException e) {
                                e.printStackTrace();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                        }
                    });
        }
    }
    public void setDistancia(int distancia){
        this.distancia = distancia;
    }

}


/* try{

                    Conector con = new Conector();
                    ArrayList<Evento> lista = con.buscarNombre(busqueda,);
                    Adaptador adaptadorEjemplo = new Adaptador(
                            getContext(),
                            R.layout.evento_item,
                            lista
                    );
                    listViewEv.setAdapter(adaptadorEjemplo);
                }catch(Exception e){
                    System.out.println(e.getMessage());
                }*/