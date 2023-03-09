package com.example.jogo.ui;

import android.graphics.Color;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TabHost;
import android.widget.TextView;

import com.example.jogo.R;

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

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment EventoF.
     */
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

                }});

            TabHost.TabSpec tab1 = tabHost.newTabSpec("tab1");
            tab1.setIndicator("EVENTO");
            tab1.setContent(R.id.eventoF);
            TabHost.TabSpec tab2 = tabHost.newTabSpec("tab2");
            tab2.setIndicator("CREAR");
            tab2.setContent(R.id.crear);
            tabHost.addTab(tab1);
            tabHost.addTab(tab2);

        // FORMULARIO CREAR EVENTO
        EditText nombreE = (EditText) view.findViewById(R.id.nombreE);
        EditText calleE = (EditText) view.findViewById(R.id.calleE);
        EditText localidadE = (EditText) view.findViewById(R.id.localidadE);
        EditText comunidad = (EditText) view.findViewById(R.id.comunidadE);
        EditText plazasE = (EditText) view.findViewById(R.id.plazasE);
        EditText descripcionE = (EditText) view.findViewById(R.id.descripcionE);

        Button crearE = (Button) view.findViewById(R.id.crearE);
        crearE.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {

            }
        });
        return view;

    }
}