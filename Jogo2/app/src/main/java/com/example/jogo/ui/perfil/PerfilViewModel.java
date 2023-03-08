package com.example.jogo.ui.perfil;

import android.view.View;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class PerfilViewModel extends ViewModel {


    public PerfilViewModel() {

    }
    public void salir(View view){
        System.exit(0);
    }
}