package com.example.musicx.FragmentsSettings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;

import com.example.musicx.DialogSeekBarTime.DialogSeekBarTime;
import com.example.musicx.R;
import com.example.musicx.SettingsPlayerPreferences.SettingsPlayingPreferences;
import com.example.musicx.dialogShowStylePlay.DialogStylesPlaying;

import java.util.Locale;

public class SettingsReproductor extends Fragment implements View.OnClickListener {

    private SwitchCompat modePlay ,modePlay2, modeBackground;
    private SettingsPlayingPreferences settingsPlayingPreferences ;
    private TextView startMusic , endMusic , timeStart , timeEnd , textStyle;
    private OnSettingsReproductorPrefers settingsReproductorPrefers ;

    public SettingsReproductor() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if( context instanceof  OnSettingsReproductorPrefers){
            settingsReproductorPrefers =(OnSettingsReproductorPrefers) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_settings_reproductor, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        textStyle = view.findViewById(R.id.text_style);
        modePlay = view.findViewById(R.id.mode_Play);
        modePlay2 = view.findViewById(R.id.fade_Song);
        modeBackground = view .findViewById(R.id.mode_Background);
        startMusic = view.findViewById(R.id.musicStart);
        endMusic = view.findViewById(R.id.musicEnd);
        timeStart = view.findViewById(R.id.textStart);
        timeEnd = view.findViewById(R.id.textEnd);
        textStyle.setOnClickListener(this);
        startMusic.setOnClickListener(this);
        endMusic.setOnClickListener(this);
        modePlay.setOnClickListener(this);
        modePlay2.setOnClickListener(this);
        modeBackground.setOnClickListener(this);
        settingsPlayingPreferences = new SettingsPlayingPreferences(requireContext());
    }

    @Override
    public void onResume() {
        super.onResume();
        if( settingsPlayingPreferences.getTypePlayer().equals("b")){
            modePlay.setChecked(true);
        }
        if( settingsPlayingPreferences.getTypeFade().equals("b")){
            modePlay2.setChecked(true);
        }
        updateTimeStartPrefers();
        updateTimeEndPrefers();
    }

    private void updateTimeStartPrefers() {
        String textTime = String.format(Locale.US, " %d s", settingsPlayingPreferences.getTimeStart());
        timeStart.setText(textTime);
    }

    private void updateTimeEndPrefers(){
        String  textTime = String.format(Locale.US," %d s",settingsPlayingPreferences.getTimeFinish());
        timeEnd.setText(textTime);
    }

    @Override
    public void onClick(View v) {
         if(v.getId() == R.id.mode_Play){
            if( settingsPlayingPreferences.getTypePlayer().equals("a")){
                settingsPlayingPreferences.setSavedTypePlay('b');
            }else{
                settingsPlayingPreferences.setSavedTypePlay('a');
            }
         }else if( v.getId() == R.id.fade_Song){
             if( settingsPlayingPreferences.getTypeFade().equals("a")){
                 settingsPlayingPreferences.setSavedTypeFade('b');
             }else{
                 settingsPlayingPreferences.setSavedTypeFade('a');
             }
         }else if( v.getId() == R.id.mode_Background){

         } else if (v.getId() ==R.id.musicStart){
             settingsReproductorPrefers.setTextView(timeStart,1);
           showDialogSeekTime();
         }else if( v.getId() == R.id.musicEnd){
             settingsReproductorPrefers.setTextView(timeEnd,1);
           showDialogSeekTime();
        }else if (v.getId() ==R.id.text_style){
             DialogStylesPlaying dialogPlay = new DialogStylesPlaying();
             dialogPlay.show(getParentFragmentManager(),"");
         }
    }

    private void showDialogSeekTime(){
            DialogSeekBarTime dialogSeekBarTime = new DialogSeekBarTime(  );
            dialogSeekBarTime.show(getParentFragmentManager(), "MyDialog");
    }

   public  interface OnSettingsReproductorPrefers{
        void setTextView(TextView textView , int type);
    }

}