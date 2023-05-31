package com.example.musicx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;


public class List_Menu_List_Fragment extends Fragment{

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View vista = inflater.inflate(R.layout.fragment_list__menu__list_, container, false);

        RelativeLayout rl_rec = vista.findViewById(R.id.rl_recientes);
        RelativeLayout rl_fav = vista.findViewById(R.id.rl_favoritos);
        RelativeLayout rl_desc = vista.findViewById(R.id.rl_descargado);
        RelativeLayout rl_URep = vista.findViewById(R.id.rl_ultimosRep);

        int ancho;
        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.R) {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            ancho = (int) (displayMetrics.widthPixels / displayMetrics.density);
        }else {
            DisplayMetrics displayMetrics = getResources().getDisplayMetrics();
            ancho = (int) displayMetrics.widthPixels;
        }
        RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams( ancho/2, 150);
        params.setMargins(12 , 8, 6,0);
        rl_rec. setLayoutParams(params );
        rl_rec.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent intent = new Intent(getContext(), ActivityRecientes.class);
                    startActivity(intent);
            }
        });

        RelativeLayout.LayoutParams paramsF = new RelativeLayout.LayoutParams( ancho/2, 150);
        paramsF.addRule(RelativeLayout.RIGHT_OF, rl_rec. getId() );
        paramsF.setMargins(6,8,12,0);
        rl_fav.setLayoutParams(paramsF);
        rl_fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), ActivityFavoritos.class);
                startActivity(intent);
            }
        });

        RelativeLayout.LayoutParams paramsD = new RelativeLayout.LayoutParams( ancho/2, 150);
        paramsD.addRule(RelativeLayout.BELOW, rl_rec. getId() );
        paramsD.setMargins(12,12,6,0);
        rl_desc.setLayoutParams(paramsD);

        RelativeLayout.LayoutParams paramsUR = new RelativeLayout.LayoutParams( ancho/2, 150);
        paramsUR.addRule(RelativeLayout.BELOW , rl_fav. getId() );
        paramsUR.addRule(RelativeLayout.RIGHT_OF , rl_desc. getId() );
        paramsUR.setMargins(6,12,12,0);
        rl_URep.setLayoutParams(paramsUR);

        return vista;
    }


}