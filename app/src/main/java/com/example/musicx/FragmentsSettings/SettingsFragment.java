package com.example.musicx.FragmentsSettings;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.musicx.R;

public class SettingsFragment extends Fragment {

OnSettingsListener settingsListener;
 RelativeLayout r1 , r2 , r3, r4 , r5;
    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if( context instanceof  OnSettingsListener){
            settingsListener = ( OnSettingsListener) context;
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.settings_fragment, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        r1 = view.findViewById(R.id.optionA);
        r1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsListener.setFragmentSelected(1);
            }
        });

        r2 = view.findViewById(R.id.optionB);
        r2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsListener.setFragmentSelected(2);
            }
        });

        r4 = view.findViewById(R.id.optionD);
        r4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                settingsListener.setFragmentSelected(4);
            }
        });
    }

   public interface OnSettingsListener{
        void setFragmentSelected(int selected);
    }
}
