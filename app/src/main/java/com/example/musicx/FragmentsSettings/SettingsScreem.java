package com.example.musicx.FragmentsSettings;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.fragment.app.Fragment;

import com.example.musicx.R;
import com.example.musicx.ThemesActivity;

import java.util.Objects;

public class SettingsScreem  extends Fragment implements View.OnClickListener {
LinearLayoutCompat background ;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings_screem_fragment, container, false);
        background = view.findViewById(R.id.background);
        background.setOnClickListener(this);
        return view;
    }

    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.background){
            Intent intent = new Intent(getActivity(), ThemesActivity.class);
            requireActivity().startActivity(intent);
        }
    }
}
