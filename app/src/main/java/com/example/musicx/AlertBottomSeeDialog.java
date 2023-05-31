package com.example.musicx;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Objects;

public class AlertBottomSeeDialog extends BottomSheetDialogFragment {
    private SeekBar seekBarSpeed;
    private TextView textViewSpeed;
    private OnSpeedChangeListener onSpeedChangeListener;

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if ( context instanceof OnSpeedChangeListener){
            onSpeedChangeListener= ( OnSpeedChangeListener) context;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alert_speed,container,false);
        seekBarSpeed= view.findViewById(R.id.sb_speed_music);
        textViewSpeed = view.findViewById(R.id.speed);
        Objects.requireNonNull(getDialog()).getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT,WindowManager.LayoutParams.WRAP_CONTENT);
        Objects.requireNonNull(getDialog()).getWindow().setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM);
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                BottomSheetDialog d=(BottomSheetDialog) dialog;
                FrameLayout bottomSheet= d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
                assert bottomSheet != null;
                BottomSheetBehavior.from(bottomSheet).setState(BottomSheetBehavior.STATE_EXPANDED);
            }
        });
        return  view;
    }

    @Override
    public void onResume() {
        super.onResume();
        try {
            seekBarSpeed.setMax(200);
            if ((onSpeedChangeListener.getSpeed() * 100) < 0) {
                seekBarSpeed.setProgress(100);
                textViewSpeed.setText("1.0x");
            } else {
                seekBarSpeed.setProgress((int) (onSpeedChangeListener.getSpeed() * 100));
                textViewSpeed.setText(onSpeedChangeListener.getSpeed() + "x");
            }
            seekBarSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    try{
                    if (fromUser) onSpeedChangeListener.setSpeed(progress);
                    textViewSpeed.setText(((float) progress / 100) + "x");
               }catch (Exception e){
                    Toast.makeText(getContext(),e+"alert bottom",Toast.LENGTH_SHORT).show();
                }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {

                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {

                }
            });
        }catch (Exception e){
            Toast.makeText(getContext(),e+"alert bottom92",Toast.LENGTH_SHORT).show();
            onSpeedChangeListener.setSeekBarSpeed(seekBarSpeed);
        }

    }

    interface OnSpeedChangeListener{
        void setSpeed ( int change);
        float getSpeed();
        void setSeekBarSpeed(SeekBar seekBarSpeed);
    }
}
