package com.example.jogo.ui;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.jogo.Conector;
import com.example.jogo.Editar_Perfil;
import com.example.jogo.Evento;
import com.example.jogo.MiAdaptadorEventos;
import com.example.jogo.Persona;
import com.example.jogo.R;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link PerfilF#newInstance} factory method to
 * create an instance of this fragment.
 */
public class PerfilF extends Fragment implements AdapterView.OnItemClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private Button button;
    private ListView listView;
    private List<String> lista;
    private TextView textViewNumEventos;

    public PerfilF() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment PerfilF.
     */
    // TODO: Rename and change types and number of parameters
    public static PerfilF newInstance(String param1, String param2) {
        PerfilF fragment = new PerfilF();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        getActivity().getWindow().getDecorView().setSystemUiVisibility( View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                | View.SYSTEM_UI_FLAG_FULLSCREEN
                | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY );
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }
    private List<Evento> lista_eventos;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_perfil,
                container, false);
        listView = (ListView) view.findViewById(R.id.ListViewMisEventos);
        Button editar = view.findViewById(R.id.button);
        TextView nombre = (TextView) view.findViewById(R.id.nnn);
        Button button = (Button) view.findViewById(R.id.BSalir);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getContext().getApplicationContext().deleteDatabase("jogo");
                System.exit(0);
            }
        });
        nombre.setText(Persona.getNombreU());
        Conector con;
        try {
            con = new Conector();

            textViewNumEventos = (TextView) view.findViewById(R.id.textViewEventos);
            textViewNumEventos.setText(con.getEventos().size()+"");
            lista_eventos = con.getEventos();
            MiAdaptadorEventos adaptadorEventos = new MiAdaptadorEventos(getActivity(), R.layout.evento_item2, lista_eventos);
            listView.setAdapter(adaptadorEventos);
            listView = (ListView)view.findViewById(R.id.ListViewMisEventos);

        } catch(Exception e){

        }

        editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent t = new Intent(getContext(), Editar_Perfil.class);
                startActivity(t);
            }
        });

        return view;
    }


    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Toast.makeText(getActivity(), "El ejemplo seleccionado es:"+lista.get(i), Toast.LENGTH_LONG).show();
    }



}