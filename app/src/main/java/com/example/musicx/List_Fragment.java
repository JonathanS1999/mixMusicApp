package com.example.musicx;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

public class List_Fragment extends Fragment {


    private OnShowButtonList showBtn;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if ( context instanceof  OnShowButtonList){
            showBtn =(OnShowButtonList) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View vista = inflater.inflate(R.layout.fragment_list, container, false);

        // fragmento inicio
        List_Menu_List_Fragment list_menu_list_fragment = new List_Menu_List_Fragment();
        // asigno mi fragmento de inicio
       getChildFragmentManager().beginTransaction().add(R.id.contenedor_fragment, list_menu_list_fragment).commit();

        return vista;
    }

    @Override
    public void onResume() {
        super.onResume();
        showBtn.showButton(true);
    }
}