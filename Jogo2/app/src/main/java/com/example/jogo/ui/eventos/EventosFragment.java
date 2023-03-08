package com.example.jogo.ui.eventos;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.jogo.databinding.FragmentEventosBinding;
import com.example.jogo.ui.perfil.PerfilViewModel;

public class EventosFragment extends Fragment {

    private FragmentEventosBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        EventosViewModel homeViewModel =
                new ViewModelProvider(this).get(EventosViewModel.class);

        binding = FragmentEventosBinding.inflate(inflater, container, false);
        View root = binding.getRoot();


        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}