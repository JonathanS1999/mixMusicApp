package com.example.musicx;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import java.util.Objects;

public class MenuPlayerFragment extends DialogFragment implements View.OnClickListener {
    private TextView t0,t1,t2,t3,t4,t5,t6,t7;
    private String title ;
    private OnMenuPlayer menuPlayer ;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof  OnMenuPlayer){
            menuPlayer = (OnMenuPlayer) context;
        }
    }

    public MenuPlayerFragment (String title){
        this.title=title;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.layout_menu , container);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        try{
        t0=view.findViewById(R.id.item_menu_player_0);
        t1=view.findViewById(R.id.item_menu_player_1);
        t2=view.findViewById(R.id.item_menu_player_2);
        t3=view.findViewById(R.id.item_menu_player_3);
        t4=view.findViewById(R.id.item_menu_player_4);
        t5=view.findViewById(R.id.item_menu_player_5);
        t6=view.findViewById(R.id.item_menu_player_6);
        t7=view.findViewById(R.id.item_menu_player_7);
        t0.setOnClickListener(this);
        t1.setOnClickListener(this);
        t2.setOnClickListener(this);
        t3.setOnClickListener(this);
        t4.setOnClickListener(this);
        t5.setOnClickListener(this);
        t6.setOnClickListener(this);
        t7.setOnClickListener(this);
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        }catch (Exception e){
            Toast.makeText(getContext(),e+"alert bottom",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        try{
        Objects.requireNonNull(getDialog()).getWindow().setGravity(Gravity.BOTTOM);
        }catch (Exception e){
            Toast.makeText(getContext(),e+"alert bottom",Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onClick(View v) {
        dismiss();
        if(v.getId()==R.id.item_menu_player_0){

        }else if(v.getId()==R.id.item_menu_player_1){
            menuPlayer.delete();
        }else if(v.getId()==R.id.item_menu_player_2){

        }else if(v.getId()==R.id.item_menu_player_3){

        }else if(v.getId()==R.id.item_menu_player_4){
            Intent intent = new Intent(getActivity() , WebViewYoutube.class);
            intent.putExtra("url",title);
            startActivity(intent);
        }else if(v.getId()==R.id.item_menu_player_5){
            menuPlayer.showAlertDialogBottom();
        }else if(v.getId()==R.id.item_menu_player_6){

        }else if(v.getId()==R.id.item_menu_player_6){

        }else if(v.getId()==R.id.item_menu_player_7){

        }

    }

    interface OnMenuPlayer{
        void delete();
        void share();
        void showAlertDialogBottom();
   }
}
