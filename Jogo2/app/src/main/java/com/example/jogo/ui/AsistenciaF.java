package com.example.jogo.ui;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.example.jogo.Adaptador;
import com.example.jogo.Conector;
import com.example.jogo.Evento;
import com.example.jogo.Persona;
import com.example.jogo.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AsistenciaF#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AsistenciaF extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public AsistenciaF() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AsistenciaF.
     */
    // TODO: Rename and change types and number of parameters
    public static AsistenciaF newInstance(String param1, String param2) {
        AsistenciaF fragment = new AsistenciaF();
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
        View view = inflater.inflate(R.layout.fragment_asistencia, container, false);
        ListView listView = (ListView) view.findViewById(R.id.listviewA);

        try{
            Conector con = new Conector();
            ArrayList<Evento> eventos = con.asistencias(Persona.getNombreU());
            Adaptador adaptadorEjemplo = new Adaptador(
                    getContext(),
                    R.layout.evento_item,
                    eventos
            );
            listView.setAdapter(adaptadorEjemplo);
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
        return view;
    }
}